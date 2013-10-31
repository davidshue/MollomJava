package com.mollom.client;

/**
 * Reason for giving feedback to Mollom.
 */
public enum FeedbackReason {

  /**
   * Report that the content is ham.
   */
  APPROVE,

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
