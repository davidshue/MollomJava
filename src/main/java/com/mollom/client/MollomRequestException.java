package com.mollom.client;

/**
 * Thrown upon a faulty request sent by the client (4xx response code).
 */
public class MollomRequestException extends MollomException {
  public MollomRequestException(String message) {
    super(message);
  }
}
