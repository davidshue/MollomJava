package com.mollom.client;

/**
 * Problem with the request to the Mollom server.
 * Generally means there was a problem on the Mollom side.
 */
public class MollomRequestException extends Exception {
  public MollomRequestException(String message) {
    super(message);
  }

  public MollomRequestException(String message, Throwable cause) {
    super(message, cause);
  }
}
