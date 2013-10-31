package com.mollom.client;

/**
 * Denotes the strictness of a SPAM check.
 */
public enum Strictness {

  /**
   * Content is more likely classified as spam.
   */
  STRICT,

  /**
   * Default behavior.
   */
  NORMAL,

  /**
   * Content is more likely classified as ham.
   */
  RELAXED;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
