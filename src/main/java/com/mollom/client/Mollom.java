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
package com.mollom.client;

import com.mollom.client.core.MollomCommunicationException;
import com.mollom.client.core.MollomCommunicator;
import com.mollom.client.core.MollomRequest;
import com.mollom.client.core.rest.RESTCommunicator;
import com.mollom.client.core.xmlrpc.XMLRPCCommunicator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Encoder;

/**
 * Mollom provides the link between the API methods and the communication
 * libraries. The main responsabilities of this class are:
 *   - using the correct communication protocol (REST or XMLRPC)
 *   - client side load balancing
 *
 * @author Thomas Meire
 */
abstract class Mollom {

  private static final Logger logger;

  /**
   * A map containing the list of servers for each public key. This map is
   * static to be able to reuse it in the different subclasses of Mollom.
   */
  private static Map<String, List<String>> serverList;

  /** The public key that needs to be used in this instance */
  private String publicKey;

  /** The private key that needs to be used in this instance */
  private String privateKey;

  /** A boolean indicating whether to use client-side load-balancing */
  private boolean useLoadBalancing;

  /** An enum defining the known protocols */
  public enum Protocol {

    REST, XMLRPC

  };
  private MollomCommunicator communicator;

  private static final String VERSION = "1.0";

  /** A string containing the url of the bootstrap Mollom instance*/
  private static final List<String> BOOTSTRAP_SERVERS;

  private static boolean TESTING;

  private static Protocol DEFAULT_PROTOCOL;

  static {
    logger = Logger.getLogger("com.mollom");

    // setup the default bootstrap servers
    String servers = System.getProperty("mollom.testing.servers");
    if (servers != null && !servers.equals("")) {
      BOOTSTRAP_SERVERS = Arrays.asList(servers.split(","));
    } else {
      BOOTSTRAP_SERVERS = Arrays.asList(new String[]{
                "http://wdc-api02.mollom.com",
                "http://wdc-api03.mollom.com",
                "http://xmlrpc2.mollom.com"});
    }

    logger.log(Level.INFO, "Using bootstrap servers:");
    for (String server : BOOTSTRAP_SERVERS) {
      logger.log(Level.INFO, "\t{0}", server);
    }

    serverList = new HashMap<String, List<String>>();

    // check if we're in testing mode
    TESTING = System.getProperty("mollom.testing", "false").equals("true");
    if (TESTING) {
      logger.info("Mollom client is running testing mode.");
    }

    String protocol = System.getProperty("mollom.protocol");
    DEFAULT_PROTOCOL = Protocol.XMLRPC;
    if (protocol != null && protocol.equals("rest")) {
      DEFAULT_PROTOCOL = Protocol.REST;
    }
  }

  /**
   * Create a new Mollom instance for the specified public and private key.
   *
   * @param publicKey		the public key for this user
   * @param privateKey	the private key for this user
   */
  public Mollom(String publicKey, String privateKey) {
    this(DEFAULT_PROTOCOL, publicKey, privateKey);
  }

  /**
   * Create a new Mollom instance for the specified public and private key.
   *
   * @param protocol		the protocol that needs to be used
   * @param publicKey		the public key for this user
   * @param privateKey	the private key for this user
   */
  public Mollom(Protocol protocol, String publicKey, String privateKey) {
    switch (protocol) {
      case REST:
        communicator = new RESTCommunicator();
        break;
      case XMLRPC:
      default:
        communicator = new XMLRPCCommunicator();
    }

    this.publicKey = publicKey;
    this.privateKey = privateKey;

    if (serverList == null) {
      serverList = new HashMap<String, List<String>>();
    }
    // add the bootstrap servers for this public key
    List<String> servers = new ArrayList<String>();
    servers.addAll(BOOTSTRAP_SERVERS);
    serverList.put(publicKey, servers);

    useLoadBalancing = System.getProperty("mollom.load_balancing", "true").equals("true");
    logger.log(Level.INFO, "Using client-side load-balancing: {0}", useLoadBalancing);
  }

  /**
   * @param balance whether to use load balancing or not
   */
  protected void setUseLoadBalancing(boolean balance) {
    useLoadBalancing = balance;
    logger.log(Level.INFO, "Using client-side load-balancing: {0}", useLoadBalancing);
  }

  /**
   * Create a hmac based on a timestamp, nonce and the private key of the user.
   * This mac is created using following formula:
   *
   * sha1(privkey ^ 0x5c + sha1(privkey ^ 0x36 + time + ":" + nonce + ":" + privkey))
   *
   * See <a href="http://mollom.com/api/authentication">the API documentation</a> for more information.
   *
   * @see http://mollom.com/api/authentication
   * @param time the timestamp that needs hashing
   * @param nonce the nonce that will be hashed
   * @return an hmac for the string "time:nonce:privatekey"
   */
  private String getHash(String time, String nonce) {
    SecretKeySpec signingKey = new SecretKeySpec(privateKey.getBytes(), "HmacSHA1");

    String content = time + ":" + nonce + ":" + privateKey;

    byte[] rawHmac;
    try {
      Mac mac = Mac.getInstance("HmacSHA1");
      mac.init(signingKey);

      rawHmac = mac.doFinal(content.getBytes());
    }
    catch (Exception e) {
      logger.warning(e.getMessage());
      rawHmac = new byte[0];
    }
    return new BASE64Encoder().encode(rawHmac);
  }

  /**
   * @return a timestamp in the format specified by Mollom
   */
  private String getTime() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    return dateFormat.format(Calendar.getInstance().getTime());
  }

  /**
   * Create a new MollomRequest for the specified method. This function will
   * automatically select the right protocol for the request. It also adds the
   * standard parameters (i.e. the public_key, timestamp, nonce and hash)
   *
   * If the system is in global testing mode, the 'testing' parameter will be
   * added to the request too.
   *
   * @param method the method to create a request for
   * @return the newly created request
   */
  protected MollomRequest createNewRequest(String method) {
    return createNewRequest(method, false);
  }

  /**
   * Create a new MollomRequest for the specified method. This function will
   * automatically select the right protocol for the request. It also adds the
   * standard parameters (i.e. the public_key, timestamp, nonce and hash)
   *
   * If the system is in global testing mode or the testing parameter is true,
   * the 'testing' parameter will be added to the request too.
   *
   * @param method the method to create a request for
   * @param testing if mollom needs to be contacted in testing mode
   * @return the newly created request
   */
  protected MollomRequest createNewRequest(String method, boolean testing) {
    MollomRequest request = new MollomRequest("mollom." + method);

    String time = getTime();
    String nonce = Long.toHexString(new Random().nextLong());

    request.setApiVersion(VERSION);
    request.addParameter("public_key", publicKey);
    request.addParameter("time", time);
    request.addParameter("nonce", nonce);
    request.addParameter("hash", getHash(time, nonce));

    if (TESTING || testing) {
      request.addParameter("testing", true);
    }
    return request;
  }

  /**
   * Validate the public and private key pair
   *
   * @see http://mollom.com/api/verifyKey
   *
   * @throws Exception when something goes wrong while contacting Mollom
   */
  public boolean verifyKey() throws Exception {
    return invoke(createNewRequest("verifyKey", false), Boolean.class);
  }

  /**
   * Get the list of available servers for this key pair.
   *
   * @see http://mollom.com/api/getServerList
   *
   * @return a list of available servers
   * @throws Exception when something goes wrong while contacting Mollom
   */
  public String[] getServerList() throws Exception {
    return getServerList(false);
  }

  /**
   * Get the list of available servers for this key pair. If useSSL is true,
   * the servers in the list will be able to accept secured connections. This
   * parameter is only effective for Mollom Premium users.
   *
   * @see http://mollom.com/api/getServerList
   *
   * @param useSSL get a list of secured servers
   * @return a list of available servers
   * @throws Exception when something goes wrong while contacting Mollom
   */
  public String[] getServerList(boolean useSSL) throws Exception {
    MollomRequest request = createNewRequest("getServerList", false);
    request.addParameter("ssl", useSSL);

    return invoke(request, String[].class);
  }

  /**
   * Update the list of servers for this key pair. The process is bootstrapped
   * by using http://xmlrpc.mollom.com as primary server.
   *
   * @todo I guess we'll need a uniform bootstrap server for all protocols (xmlrpc + rest)
   *
   * @param servers the current list of available servers
   * @return the new list of available servers
   */
  private List<String> updateServerList(List<String> servers) {
    if (servers == null) {
      servers = new ArrayList<String>();
    }
    servers.addAll(BOOTSTRAP_SERVERS);
    // store the serverlist for further use...
    serverList.put(publicKey, servers);

    try {
      servers = new ArrayList<String>();

      // get the new serverlist and store it
      servers.addAll(Arrays.asList(getServerList()));

      if (servers.isEmpty()) {
        servers.addAll(BOOTSTRAP_SERVERS);
      }
    }
    catch (Exception e) {
      servers = new ArrayList<String>();
      servers.addAll(BOOTSTRAP_SERVERS);
    }
    // store the serverlist for further use...
    serverList.put(publicKey, servers);
    return servers;
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
  protected <T> T invoke(MollomRequest request, Class<T> c) throws Exception {
    if (!useLoadBalancing) {
      return invokeNoBalancing(request, c);
    }

    List<String> servers = serverList.get(publicKey);

    if (servers == null || servers.isEmpty()) {
      servers = updateServerList(servers);
    }

    int i = 0;
    T response = null;
    while (i < servers.size() && response == null) {
      request.setServer(servers.get(i));

      try {
        response = communicator.execute(request, c);
      }
      catch (MollomCommunicationException mce) {
        logger.log(Level.WARNING, "Failed call to Mollom.", mce.getCause());
        switch (mce.code) {
          case 1000: // Mollom internal exception
            logger.log(Level.WARNING, "ErrorCode 1000: {0}", mce.getMessage());
            throw new Exception("Mollom returned an error message: '" + mce.getCause() + "'.");
          case 1100: // Reload serverlist & start from the front
            logger.log(Level.WARNING, "ErrorCode {0}: reloading server list", mce.code);
            servers = updateServerList(servers);
            i = 0;
            continue;
          case 1200: // Server busy
          case 9000: // Networking error
          default:   // Unknown error
            logger.log(Level.WARNING, "ErrorCode 1200: trying next server");
            response = null;
        }
      }
      i++;
    }

    if (response == null) {
      servers.clear();
      serverList.put(publicKey, servers);
      logger.log(Level.SEVERE, "All Mollom servers are down!");
      throw new Exception("All Mollom servers are down.");
    }
    return response;
  }

  private <T> T invokeNoBalancing(MollomRequest request, Class<T> c) throws Exception {
    // we don't have any load balancing, so no fallbacks either
    String server = BOOTSTRAP_SERVERS.get(0);
    request.setServer(server);

    // we don't care here what the exception was, just throw it up the chain
    return communicator.execute(request, c);
  }
}
