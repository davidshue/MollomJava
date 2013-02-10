/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mollom.client.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author johan
 */
@XmlRootElement(name="blacklistentryresponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class BlacklistEntryResponse extends RestResponse {
  
  private BlacklistEntry entry;

  /**
   * @return the entry
   */
  public BlacklistEntry getEntry() {
    return entry;
  }

  /**
   * @param entry the entry to set
   */
  public void setEntry(BlacklistEntry entry) {
    this.entry = entry;
  }
  
}
