package com.mollom.client;

/**
 * Problem with the request to the Mollom service.
 * The server returned a 4xx status.
 */
public class MollomBadRequestException extends MollomException {
  public MollomBadRequestException(String message) {
    super(message);
  }
}
