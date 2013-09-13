package com.mollom.client;


/**
 * Where a Blacklist or Whitelist entry may match.
 */
public enum Context {
  ALLFIELDS("allFields"),
  AUTHORNAME("authorName"),
  AUTHORMAIL("authorMail"),
  AUTHORIP("authorIp"),
  AUTHORID("authorId"),
  LINKS("links"),
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
