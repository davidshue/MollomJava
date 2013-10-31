package com.mollom.client;

/**
 * Thrown upon unexpected/incorrect usage of the client library.
 *
 * The exception message explains the usage.
 */
public class MollomIllegalUsageException extends RuntimeException {
  public MollomIllegalUsageException(String message) {
    super(message);
  }
}
