package com.mollom.client;

/**
 * Type of content to check with Mollom.
 */
public enum ContentType {
  /**
   * Content from a registration form.
   */
  USER,
  /**
   * Normal content, e.g. comments, blog posts, etc.
   */
  CONTENT;

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
