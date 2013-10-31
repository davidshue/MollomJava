package com.mollom.client;

/**
 * How precise a blacklist entry matches.
 */
public enum BlacklistMatchPrecision {

  /**
   * Matches any string contained within any other string.
   */
  CONTAINS,

  /**
   * Requires term to be enclosed by word boundaries.
   */
  EXACT;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
