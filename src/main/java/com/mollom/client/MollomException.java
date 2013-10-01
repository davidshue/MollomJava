package com.mollom.client;

/**
 * Problem with the request to the Mollom server.
 * Generic superclass for more specific exceptions.
 */
public class MollomException extends Exception {
  public MollomException(String message) {
    super(message);
  }

  public MollomException(String message, Throwable cause) {
    super(message, cause);
  }
}
