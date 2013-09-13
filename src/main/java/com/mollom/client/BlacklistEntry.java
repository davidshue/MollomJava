package com.mollom.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Mollom automatically blocks unwanted content and learns from all participating sites to improve its filters.
 * On top of automatic filtering, you can define a custom blacklist.
 * Upon matching a blacklist item, content is blocked.
 */
@XmlRootElement(name = "entry")
@XmlAccessorType(XmlAccessType.FIELD)
public class BlacklistEntry {
  private String id;
  private int created;
  private int status;
  private int lastMatch;
  private long matchCount;
  private String value;
  private String reason;
  private String context;
  private String match;
  private String note;

  public String getId() {
    return id;
  }

  public int getCreated() {
    return created;
  }

  public int getLastMatch() {
    return lastMatch;
  }

  public long getMatchCount() {
    return matchCount;
  }

  public String getValue() {
    return value;
  }

  public String getReason() {
    return reason;
  }

  public String getContext() {
    return context;
  }

  public String getMatch() {
    return match;
  }

  public String getNote() {
    return note;
  }

  void setId(String id) {
    this.id = id;
  }

  void setCreated(int created) {
    this.created = created;
  }

  void setStatus(int status) {
    this.status = status;
  }

  void setLastMatch(int lastMatch) {
    this.lastMatch = lastMatch;
  }

  void setMatchCount(long matchCount) {
    this.matchCount = matchCount;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public void setContext(String context) {
    this.context = context;
  }

  public void setMatch(String match) {
    this.match = match;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public boolean isEnabled() {
    return status == 1;
  }

  public void disable() {
    status = 0;
  }

  public void enable() {
    status = 1;
  }
}
