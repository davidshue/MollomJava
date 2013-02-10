package com.mollom.client.rest;

import java.io.Serializable;

/**
 *
 * @author johan
 */
public class BlacklistEntry implements Serializable {

  private String id;
  private String value;
  private String context;
  private String reason;
  private String match;
  private int status;
  private String note;
  private long created;

  @Override
  public String toString() {
    return "entry with text = " + value + ", context = " + context + ", reason = " + reason + ", match = " + match;
  }

  /**
   * @return the value
   */
  public String getValue() {
    return value;
  }

  /**
   * @param value the value to set
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * @return the context
   */
  public String getContext() {
    return context;
  }

  /**
   * @param context the context to set
   */
  public void setContext(String context) {
    this.context = context;
  }

  /**
   * @return the reason
   */
  public String getReason() {
    return reason;
  }

  /**
   * @param reason the reason to set
   */
  public void setReason(String reason) {
    this.reason = reason;
  }

  /**
   * @return the match
   */
  public String getMatch() {
    return match;
  }

  /**
   * @param match the match to set
   */
  public void setMatch(String match) {
    this.match = match;
  }

  /**
   * @return the created
   */
  public long getCreated() {
    return created;
  }

  /**
   * @param created the created to set
   */
  public void setCreated(long created) {
    this.created = created;
  }

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }
}
