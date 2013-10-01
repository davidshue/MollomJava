package com.mollom.client;

/**
 * The Mollom server returned an unexpected response.
 * This could mean a 5xx status code was returned or we were unable to parse the response.
 */
public class MollomUnexpectedResponseException extends MollomException {
  public MollomUnexpectedResponseException(String message) {
    super(message);
  }
  public MollomUnexpectedResponseException(String message, Throwable e) {
    super(message, e);
  }
}
