package com.mollom.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Defines the blacklist entry entity.
 *
 * @see http://mollom.com/api#blacklist
 */
@XmlRootElement(name = "entry")
@XmlAccessorType(XmlAccessType.FIELD)
public class BlacklistEntry {

  private String id;
  private int created;

  private int status;
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
   * @return Unix timestamp (seconds) of when the entry was created.
   */
  public int getCreated() {
    return created;
  }

  public boolean isEnabled() {
    return status == 1;
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

  public void disable() {
    status = 0;
  }

  public void enable() {
    status = 1;
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
   * @param context Where the entry's value may match.
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
}
