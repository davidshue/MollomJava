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

  public BlacklistEntry() {
    reason = BlacklistReason.UNWANTED.toString();
    context = Context.ALLFIELDS.toString();
    match = BlacklistMatchPrecision.CONTAINS.toString();
    status = 1;
  }

  /**
   * @return Unique blacklist entry ID assigned by Mollom.
   */
  public String getId() {
    return id;
  }

  /**
   * @return Unix timestamp (seconds) of when the blacklist entry was created.
   */
  public int getCreated() {
    return created;
  }

  /**
   * @return Unix timestamp (seconds) of when the last time this blacklist entry matched.
   */
  public int getLastMatch() {
    return lastMatch;
  }

  /**
   * @return Number of times this blacklist entry has matched content
   */
  public long getMatchCount() {
    return matchCount;
  }

  /**
   * @return The string/value to blacklist.
   */
  public String getValue() {
    return value;
  }

  /**
   * @return The reason for why the value is blacklisted.
   */
  public BlacklistReason getReason() {
    return BlacklistReason.valueOf(reason.toUpperCase());
  }

  /**
   * @return Where the entry's value may match.
   */
  public Context getContext() {
    return Context.valueOf(context.toUpperCase());
  }

  /**
   * @return How precise the entry's value may match.
   */
  public BlacklistMatchPrecision getMatch() {
    return BlacklistMatchPrecision.valueOf(match.toUpperCase());
  }

  /**
   * @return A custom string explaining the entry. Useful in a multi-moderator scenario.
   */
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

  /**
   * @param value The string/value to blacklist.
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * @param reason The reason for why the value is blacklisted.
   */
  public void setReason(BlacklistReason reason) {
    this.reason = reason.toString();
  }

  /**
   * @oaram context Where the entry's value may match.
   */
  public void setContext(Context context) {
    this.context = context.toString();
  }

  /**
   * @param match How precise the entry's value may match.
   */
  public void setMatch(BlacklistMatchPrecision match) {
    this.match = match.toString();
  }

  /**
   * @param note A custom string explaining the entry. Useful in a multi-moderator scenario.
   */
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
