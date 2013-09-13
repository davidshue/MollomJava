package com.mollom.client;

/**
 * The possible checks that can be executed on a piece of text.
 */
public enum Check {
  /**
   * Check the text for spamminess (default)
   * Will allow for isSpam()/isHam()/isUnsure() and getSpamScore()
   */
  SPAM,
  /**
   * Check the text for foul language
   * Will allow for getProfanityScore()
   */
  PROFANITY,
  /**
   * Estimate the quality of the text
   * Will allow for getQualityScore()
   */
  QUALITY,
  /**
   * Determine the most likely language(s) for the text
   * Will allow for getLanguages()
   */
  LANGUAGE;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
