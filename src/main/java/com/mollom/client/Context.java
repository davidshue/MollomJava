package com.mollom.client;

/**
 * In which fields a Blacklist or Whitelist entry may match.
 */
public enum Context {
  // Blacklist only; default.
  ALLFIELDS("allFields"),

  AUTHOR("author"),
  AUTHORIP("authorIp"),
  // Default for whitelist.
  AUTHORID("authorId"),
  AUTHORNAME("authorName"),
  AUTHORMAIL("authorMail"),

  // Blacklist only.
  POST("post"),
  POSTTITLE("postTitle"),
  LINKS("links");

  private final String name;

  private Context(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
}
