package com.mollom.client;

/**
 * Thrown upon configuration problem on the client-side.
 */
public class MollomConfigurationException extends RuntimeException {
  public MollomConfigurationException(String message) {
    super(message);
  }

  public MollomConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }
}
