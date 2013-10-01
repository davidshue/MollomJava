package com.mollom.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.oauth.client.OAuthClientFilter;
import com.sun.jersey.oauth.signature.OAuthParameters;
import com.sun.jersey.oauth.signature.OAuthSecrets;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

/**
 * The initial entry point for the Mollom API library.
 * Used to build a MollomClient to interact with the Mollom services.
 */
public class MollomClientBuilder {
  private static final String PRODUCTION_ENDPOINT = "http://rest.mollom.com/";
  private static final String TESTING_ENDPOINT = "http://dev.mollom.com/";
  private static final String DEFAULT_API_VERSION = "v1";
  private static final int DEFAULT_RETRIES = 1;
  private static final int DEFAULT_CONNECTION_TIMEOUT = 1500;
  private static final int DEFAULT_READ_TIMEOUT = 1500;
  private static final String DEFAULT_CLIENT_NAME = "com.mollom.client";
  private static final String DEFAULT_CLIENT_VERSION = "2.0";

  // Client behavior settings
  private boolean testing;
  private String apiVersion;
  private int retries;
  private int connectionTimeout;
  private int readTimeout;

  // Client information sent to Mollom for debugging / statistics
  private String clientName;
  private String clientVersion;
  private String platformName;
  private String platformVersion;

  public static MollomClientBuilder create() {
    return new MollomClientBuilder();
  }

  MollomClientBuilder() {
    testing = false;
    apiVersion = DEFAULT_API_VERSION;
    retries = DEFAULT_RETRIES;
    connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
    readTimeout = DEFAULT_READ_TIMEOUT;
    clientName = DEFAULT_CLIENT_NAME;
    clientVersion = DEFAULT_CLIENT_VERSION;
  }
  
  /**
   * Optional property. Default value: false
   * Enable the use of the dev.mollom.com endpoint to test your REST API client implementation.
   * 
   * Testing mode differences:
   * checkContent only reacts to literal strings "spam", "ham" and "unsure" in the postTitle and postBody parameters
   * if none of the literal strings is contained, and also no blacklist or whitelist values matched,
   * the final spamClassification will be "unsure"
   * checkCaptcha accepts "correct" for image captchas and "demo" for audio captchas as the correct solution
   * the captchaId must be a valid captchaId returned from a getCaptcha request
   */
  public MollomClientBuilder withTesting(boolean testing) {
    this.testing = testing;
    return this;
  }
  
  /**
   * Optional property. Default value: v1
   * Set the version of the Mollom API to use.
   * We only support one API version at the moment, but added for the sake of completeness
   */
  public MollomClientBuilder withApiVersion(String apiVersion) {
    // TODO: Add additional checks when we support multiple API versions
    if (!"v1".equals(apiVersion)) {
      throw new MollomConfigurationException("Property `apiVersion` must be one of ('v1')");
    }

    this.apiVersion = apiVersion;
    return this;
  }

  /**
   * Optional property. Default value: 1
   * Set the maximum number of times the client will retry a request to the Mollom service.
   */
  public MollomClientBuilder withRetries(int retries) {
    if (retries < 0) {
      throw new MollomConfigurationException("Property `retries` must be greater than or equal to 0.");
    }

    this.retries = retries;
    return this;
  }

  /**
   * Optional property. Default value: 1500 (ms)
   * Set the timeout in making the initial connection to the Mollom service.
   * 
   * Setting this value to 0 results in an infinite timeout.
   */
  public MollomClientBuilder withConnectionTimeout(int connectionTimeout) {
    if (connectionTimeout < 0) {
      throw new MollomConfigurationException("Property `connectionTimeout` must be greater than or equal to 0.");
    }

    this.connectionTimeout = connectionTimeout;
    return this;
  }

  /**
   * Optional property. Default value: 1500 (ms)
   * Set the timeout on waiting to read data from the Mollom service.
   * 
   * Setting this value to 0 results in an infinite timeout.
   */
  public MollomClientBuilder withReadTimeout(int readTimeout) {
    if (readTimeout < 0) {
      throw new MollomConfigurationException("Property `readTimeout` must be greater than or equal to 0.");
    }

    this.readTimeout = readTimeout;
    return this;
  }

  /**
   * Optional property. Default value: com.mollom.client
   * Set the Mollom client name. Change this value if you're using a modified version of the client.
   * 
   * This information is used to speed up support requests and technical
   * inquiries. The data may also be aggregated to help the Mollom staff to make
   * decisions on new features or the necessity of back-porting improved
   * functionality to older versions.
   */
  public MollomClientBuilder withClientName(String clientName) {
    this.clientName = clientName;
    return this;
  }

  /**
   * Optional property. Default value: 2.0
   * Set the Mollom client version. Change this value if you're using a modified version of the client.
   * 
   * This information is used to speed up support requests and technical
   * inquiries. The data may also be aggregated to help the Mollom staff to make
   * decisions on new features or the necessity of back-porting improved
   * functionality to older versions.
   */
  public MollomClientBuilder withClientVersion(String clientVersion) {
    this.clientVersion = clientVersion;
    return this;
  }

  /**
   * Optional property. Default value: n/a
   * Set the platform/framework name; e.g. Spring.
   * 
   * This information is used to speed up support requests and technical
   * inquiries. The data may also be aggregated to help the Mollom staff to make
   * decisions on new features or the necessity of back-porting improved
   * functionality to older versions.
   */
  public MollomClientBuilder withPlatformName(String platformName) {
    this.platformName = platformName;
    return this;
  }

  /**
   * Optional property. Default value: n/a
   * Set the platform/framework version; e.g. 3.1
   * 
   * This information is used to speed up support requests and technical
   * inquiries. The data may also be aggregated to help the Mollom staff to make
   * decisions on new features or the necessity of back-porting improved
   * functionality to older versions.
   */
  public MollomClientBuilder withPlatformVersion(String platformVersion) {
    this.platformVersion = platformVersion;
    return this;
  }

  /**
   * Builds the MollomClient object as configured.
   * 
   * @throws MollomConfigurationException If could not authenticate with the Mollom service.
   */
  public MollomClient build(String publicKey, String privateKey) {
    Client client = new Client();
    client.setConnectTimeout(connectionTimeout);
    client.setReadTimeout(readTimeout);

    OAuthParameters oauthParams = new OAuthParameters().signatureMethod("HMAC-SHA1").consumerKey(publicKey).version("1.0");
    OAuthSecrets oauthSecrets = new OAuthSecrets().consumerSecret(privateKey);
    ClientFilter oauthFilter = new OAuthClientFilter(client.getProviders(), oauthParams, oauthSecrets);
    client.addFilter(oauthFilter);

    String rootUrl = testing ? TESTING_ENDPOINT : PRODUCTION_ENDPOINT;
    WebResource rootResource = client.resource(rootUrl).path(apiVersion);

    // Verify the public private keys
    if(publicKey == null) {
      throw new MollomConfigurationException("The property `publicKey` must be configured.");
    }
    if(privateKey == null) {
      throw new MollomConfigurationException("The property `privateKey` must be configured.");
    }
    MultivaluedMap<String, String> postParams = new MultivaluedMapImpl();
    postParams.putSingle("platformName", platformName);
    postParams.putSingle("platformVersion", platformVersion);
    postParams.putSingle("clientName", clientName);
    postParams.putSingle("clientVersion", clientVersion);
    ClientResponse response = rootResource.path("site").path(publicKey)
      .accept(MediaType.APPLICATION_XML).type(MediaType.APPLICATION_FORM_URLENCODED)
      .post(ClientResponse.class, postParams);
    if (response.getStatus() != 200) {
      throw new MollomConfigurationException("Invalid public/private key.");
    }

    // Initialize the resources
    WebResource contentResource = rootResource.path("content");
    WebResource captchaResource = rootResource.path("captcha");
    WebResource feedbackResource = rootResource.path("feedback");
    WebResource blacklistResource = rootResource.path("blacklist").path(publicKey);
    WebResource whitelistResource = rootResource.path("whitelist").path(publicKey);
    
    MollomClient mollomClient = new MollomClient(client, contentResource, captchaResource, feedbackResource, blacklistResource, whitelistResource, retries);
    return mollomClient;
  }
}
