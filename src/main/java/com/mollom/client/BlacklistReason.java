package com.mollom.client;

/**
 * Why a post with positive match is blacklisted.
 */
public enum BlacklistReason {

  /**
   * Spam, unsolicited advertising.
   */
  SPAM,

  /**
   * Profane, obscene, violent.
   */
  PROFANITY,

  /**
   * Low-quality.
   */
  QUALITY,

  /**
   * Unwanted, taunting, off-topic.
   */
  UNWANTED;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
