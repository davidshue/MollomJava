/**
 * Copyright (c) 2010-2012 Mollom
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.mollom.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.oauth.client.OAuthClientFilter;
import com.sun.jersey.oauth.signature.OAuthParameters;
import com.sun.jersey.oauth.signature.OAuthSecrets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

/**
 * Mollom provides the link between the API methods and the communication
 * libraries. The main responsabilities of this class are:
 *   - using the correct communication protocol (REST or XMLRPC)
 *   - client side load balancing
 *
 * @author Thomas Meire
 */
abstract class Mollom {

  private static final Logger LOGGER = Logger.getLogger("com.mollom.client");

  /**
   * The REST endpoint for production machines.
   */
  private static final String PRODUCTION_ENDPOINT = "http://rest.mollom.com/";

  /**
   * The REST endpoint for testing machines.
   */
  private static final String TESTING_ENDPOINT = "http://dev.mollom.com/";

  /**
   * The version of the REST protocol.
   */
  private static final String VERSION = "v1";

  /**
   * The number of time to retry the request before bailing out.
   */
  private static final int RETRIES = 2;

  /**
   * 
   */
  private static final Client client = Client.create();

  /**
   * The public key that needs to be used in this instance
   */
  protected final String publicKey;

  /**
   * The private key that needs to be used in this instance
   */
  private final String privateKey;

  private WebResource resource;

  /**
   * Create a new Mollom instance for the specified public and private key.
   *
   * When the testing mode is true, dev.mollom.com will be used as endpoint
   * instead of rest.mollom.com See http://mollom.com/api#api-test
   *
   * @param publicKey		the public key for this user
   * @param privateKey	the private key for this user
   * @param testing     set the testing mode
   */
  public Mollom(String publicKey, String privateKey, boolean testing) {
    this.publicKey = publicKey;
    this.privateKey = privateKey;

    // setup the oauth authentication
    OAuthParameters params = new OAuthParameters()
            .signatureMethod("HMAC-SHA1")
            .consumerKey(publicKey)
            .version("1.0");
    OAuthSecrets secrets = new OAuthSecrets().consumerSecret(privateKey);

    ClientFilter oauth = new OAuthClientFilter(client.getProviders(), params, secrets);

    String endpoint = !testing ? PRODUCTION_ENDPOINT : TESTING_ENDPOINT;

    // setup the web resource & add the oauth filter
    resource = client.resource(endpoint).path(VERSION);
    resource.addFilter(oauth);
  }

  protected void add (MultivaluedMap<String, String> params, String name, String value) {
    if (value != null) {
      params.add(name, value);
    }
  }

  protected void add (MultivaluedMap<String, String> params, String name, boolean value) {
    params.add(name, value ? "1" : "0");
  }

  protected <T extends Enum> void add (MultivaluedMap<String, String> params, String name, T value) {
    if (value != null) {
      params.add(name, value.toString().toLowerCase());
    }
  }

  /**
   * Invoke a MollomRequest. This method handles the actual communication and
   * handles the xmlrpc error codes 1100 and 1200.
   *
   * @see http://mollom.com/api/client-side-load-balancing
   *
   * @param request the request to execute
   * @return a MollomResponse containing the answer from Mollom
   * @throws Exception when something goes wrong while contacting Mollom
   */
  protected <T> T invoke(String method, String path, MultivaluedMap<String, String> params, Class<T> klass) throws Exception {
    WebResource endpoint = resource.path(path);

    int i = 0;
    T answer = null;
    while (i < RETRIES && answer == null) {
      try {
        ClientResponse response;
        if ("GET".equals(method)) {
          response = endpoint.queryParams(params).get(ClientResponse.class);
        } else if ("POST".equals(method)) {
          response = endpoint.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, params);
        } else {
          response = endpoint.method(method, ClientResponse.class, params);
        }

        int status = response.getStatus();
        if (status >= 200 && status < 300) {
          answer = response.getEntity(klass);
        } else {
          LOGGER.log(Level.WARNING, "Bad response status {0}: {1}", new Object[]{status, response.getEntity(String.class)});
        }
      } catch (ClientHandlerException che) {
        LOGGER.log(Level.WARNING, "Failed to parse the Mollom request or response.", che);
      } catch (UniformInterfaceException uie) {
        LOGGER.log(Level.WARNING, "Unexpected HTTP response.", uie);
      } finally {
        i += 1;
      }
    }
    if (answer == null) {
      throw new Exception("The Mollom servers failed to reply.");
    }
    return answer;
  }
}