package com.mollom.client;

/**
 * Trouble communicating with the Mollom servers.
 * Generally thrown when Mollom is experiencing service downtimes.
 */
public class MollomNoResponseException extends MollomException {
  public MollomNoResponseException(String message) {
    super(message);
  }
}
