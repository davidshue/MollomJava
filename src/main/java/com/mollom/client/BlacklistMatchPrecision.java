package com.mollom.client;

/**
 * How precise a Blacklist entry may match.
 */
public enum BlacklistMatchPrecision {
  EXACT, CONTAINS;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
