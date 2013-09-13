package com.mollom.client;

/**
 * The reason for why the value is blacklisted.
 */
public enum BlacklistReason {
  /**
   * Report that the content is spammy
   */
  SPAM,
  /**
   * Report that the content is profane.
   */
  PROFANITY,
  /**
   * Report that the content is low quality.
   */
  QUALITY,
  /**
   * Report that the content is unwanted on the site,
   * but not spam, profane or low quality.
   */
  UNWANTED;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
