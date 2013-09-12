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
   * Report that the content is spam.
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
  UNWANTED
}
