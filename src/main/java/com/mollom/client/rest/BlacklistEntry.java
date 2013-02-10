/**
 * Copyright (c) 2010-2012 Mollom. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.mollom.client.rest;

import java.io.Serializable;

/**
 *
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
