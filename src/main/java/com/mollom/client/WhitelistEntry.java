package com.mollom.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Defines the whitelist entry entity.
 *
 * @see http://mollom.com/api#whitelist
 */
@XmlRootElement(name = "entry")
@XmlAccessorType(XmlAccessType.FIELD)
public class WhitelistEntry {

  private String id;
  private int created;

  private int status;
  private String value;
  private String context;
  private String note;

  public WhitelistEntry() {
    context = Context.AUTHORID.toString();
    status = 1;
  }

  /**
   * @return Unique whitelist entry ID assigned by Mollom.
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
   * @return The string/value to whitelist.
   */
  public String getValue() {
    return value;
  }

  /**
   * @return Where the entry's value may match.
   */
  public Context getContext() {
    return Context.valueOf(context.toUpperCase());
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
   * @param value The string/value to whitelist.
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * @param context Where the entry's value may match.
   */
  public void setContext(Context context) {
    this.context = context.toString();
  }

  /**
   * @param note A custom string explaining the entry. Useful in a multi-moderator scenario.
   */
  public void setNote(String note) {
    this.note = note;
  }
}
