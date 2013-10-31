package com.mollom.client;

/**
 * Thrown in case the Mollom REST API is down or unreachable.
 */
public class MollomNoResponseException extends MollomException {
  public MollomNoResponseException(String message) {
    super(message);
  }
}
