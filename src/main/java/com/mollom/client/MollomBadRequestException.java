package com.mollom.client;

/**
 * Thrown upon a faulty request sent by the client (4xx response code).
 */
public class MollomBadRequestException extends MollomException {
  public MollomBadRequestException(String message) {
    super(message);
  }
}
