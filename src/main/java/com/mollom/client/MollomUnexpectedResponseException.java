package com.mollom.client;

/**
 * Thrown upon unexpected response from Mollom API.
 *
 * Can be due to a 5xx response code or an unexpected response that could not
 * be parsed.
 */
public class MollomUnexpectedResponseException extends MollomException {
  public MollomUnexpectedResponseException(String message) {
    super(message);
  }

  public MollomUnexpectedResponseException(String message, Throwable e) {
    super(message, e);
  }
}
