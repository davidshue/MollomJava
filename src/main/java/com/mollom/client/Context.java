package com.mollom.client;


/**
 * Where a Blacklist or Whitelist entry may match.
 */
public enum Context {
  /**
   * Not available for whitelist
   * Default for blacklist
   */
  ALLFIELDS("allFields"),
  AUTHORNAME("authorName"),
  AUTHORMAIL("authorMail"),
  AUTHORIP("authorIp"),
  /**
   * Default for whitelist
   */
  AUTHORID("authorId"),
  /**
   * Not available for whitelist
   */
  LINKS("links"),
  /**
   * Not available for whitelist
   */
  POSTTITLE("postTitle");

  private final String name;

  private Context(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
}
