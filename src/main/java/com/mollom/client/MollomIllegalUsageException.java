package com.mollom.client;

/**
 * Incorrect usage of the library. Returned message details the incorrect usage of the library.
 * This means a problem on the user's side.
 */
public class MollomIllegalUsageException extends RuntimeException {
  public MollomIllegalUsageException(String message) {
    super(message);
  }
}
