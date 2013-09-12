package com.mollom.client;

/**
 * Problem with the configuration of the MollomClient.
 * Generally means there was a problem on the user's side.
 */
public class MollomConfigurationException extends RuntimeException {
  public MollomConfigurationException(String message) {
    super(message);
  }

  public MollomConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }
}
