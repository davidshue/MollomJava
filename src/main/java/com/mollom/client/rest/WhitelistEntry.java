
package com.mollom.client.rest;

/**
 *
 * @author thomas
 */
public class WhitelistEntry {
  private String id;
  private String value;
  private String context;
  private int status;
  private String note;
  private long created;
  private long lastMatch;
  private int matchCount;

  public String getContext() {
    return context;
  }

  public void setContext(String context) {
    this.context = context;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setCreated (long created) {
    this.created = created;
  }

  public long getCreated () {
    return created;
  }

  public long getLastMatch() {
    return lastMatch;
  }

  public void setLastMatch(long lastMatch) {
    this.lastMatch = lastMatch;
  }

  public int getMatchCount() {
    return matchCount;
  }

  public void setMatchCount(int matchCount) {
    this.matchCount = matchCount;
  }
}
