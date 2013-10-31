package com.mollom.client;

/**
 * Checks that can be executed on a piece of content.
 */
public enum Check {

  /**
   * Check the text for spam (default).
   *
   * Enables isHam(), isSpam(), isUnsure(), and getSpamScore().
   */
  SPAM,

  /**
   * Check the text for profanity.
   *
   * Enables getProfanityScore().
   */
  PROFANITY,

  /**
   * Determine the quality of the text.
   *
   * Enables getQualityScore().
   */
  QUALITY,

  /**
   * Determine the languages of the text.
   *
   * Enables getLanguages().
   */
  LANGUAGE;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
