package com.mollom.client;

/**
 * Base exception thrown in any Mollom API request/response failure event.
 */
public class MollomException extends Exception {
  public MollomException(String message) {
    super(message);
  }

  public MollomException(String message, Throwable cause) {
    super(message, cause);
  }
}
