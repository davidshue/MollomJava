/**
 * Copyright (c) 2010, Mollom
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
package com.mollom.client.core.rest;

import com.mollom.client.core.MollomCommunicationException;
import com.mollom.client.core.MollomCommunicator;
import com.mollom.client.core.MollomRequest;


/**
 * RESTCommunicator implements a REST version of MollomCommunicator.
 *
 * NOTE: This code is not stable yet!
 * 
 * @author Thomas Meire
 */
public class RESTCommunicator implements MollomCommunicator {

  public <T> T execute(MollomRequest request, Class<T> c) throws MollomCommunicationException {
    throw new UnsupportedOperationException("Not supported yet.");
  }
/*
  private static final String GET = "GET";

  private static final String PUT = "PUT";

  private static final String POST = "POST";

  private static final String DELETE = "DELETE";

  private static final Map<String, RestMethod> endpoints = new HashMap<String, RestMethod>();

  static {
    endpoints.put("mollom.getServerList", new RestMethod("site/serverlist", GET));
    endpoints.put("mollom.verifyKey", new RestMethod("1.0/key/{public_key}", GET));
    endpoints.put("mollom.listBlacklistText", new RestMethod("blacklist/{public_key}", GET));
    endpoints.put("mollom.addBlacklistText", new RestMethod("blacklist/{public_key}", POST));
    endpoints.put("mollom.removeBlacklistText", new RestMethod("blacklist/{public_key}/{item}", DELETE));
    endpoints.put("mollom.checkContent", new RestMethod("content", POST));
    endpoints.put("mollom.getImageCaptcha", new RestMethod("captcha/image", POST));
    endpoints.put("mollom.getAudioCaptcha", new RestMethod("captcha/audio", POST));
    endpoints.put("mollom.checkCaptcha",    new RestMethod("captcha/verify/{session_id}", POST));
  //  endpoints.put("mollom.listBlacklistText", new RestMethod("blacklist/")"");
    endpoints.put("mollom.getReputation",   new RestMethod("user/reputation", GET));
   // endpoints.put("mollom.resetReputation", "captcha/verify/{session_id}.xml");
  }

  private String fillPathParams(String path, Map<String, String> params) {
    Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, String> entry = it.next();
      if (path.contains("{" + entry.getKey() + "}")) {
        path = path.replace("{" + entry.getKey() + "}", entry.getValue());
        it.remove();
      }
    }
    return  path;
  }

  private MultivaluedMap<String, String> toMultiValuedMap(Map<String, String> map) {
    MultivaluedMap<String, String> mmap = new MultivaluedMapImpl();

    for (Map.Entry<String, String> entry : map.entrySet()) {
      mmap.add(entry.getKey(), entry.getValue());
    }
    return mmap;
  }

  public WebResource.Builder toResource(String endpoint, MollomRequest request) {
    Client client = Client.create();

    String path = fillPathParams(endpoint, request.getParams());
    MultivaluedMap<String, String> params = toMultiValuedMap(request.getParams());

    return client.resource(request.getServer()).path(path).entity(params);
  }

  public <T> T execute(MollomRequest request, Class<T> c) throws MollomCommunicationException {
    // server shouldn't be null, be check to make sure
    if (request.getServer() == null) {
      return null;
    }
    RestMethod endpoint = endpoints.get(request.getMethod());
    if (endpoint == null) {
      throw new MollomCommunicationException(1000, "Unknown method " + request.getMethod() + " for rest call!");
    }
    WebResource.Builder resource = toResource(endpoint.format, request);

    try {
      ClientResponse response = resource.method(endpoint.method, ClientResponse.class);

      try {
        return response.getEntity(c);
      } catch (UniformInterfaceException uie) {
        // throw a new proper exception from Mollom
        RESTException ex = response.getEntity(RESTException.class);
        throw new MollomCommunicationException(ex.faultCode, ex);
      }
    } catch (Exception e) {
      // throw a new unknown exception with code 1000
      throw new MollomCommunicationException(1000, e);
    }
  }

  private static class RestMethod {

    public String format;
    public String method;

    public RestMethod(String path, String method) {
      this.format = path;
      this.method = method;
    }

  }

  public static class RESTException extends Exception {

    public int faultCode;
    public String faultString;
  }*/
}
