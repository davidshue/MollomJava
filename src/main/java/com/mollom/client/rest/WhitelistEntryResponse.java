
package com.mollom.client.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author johan
 */
@XmlRootElement(name="whitelistentryresponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class WhitelistEntryResponse extends RestResponse {

  private WhitelistEntry entry;

  /**
   * @return the entry
   */
  public WhitelistEntry getEntry() {
    return entry;
  }

  /**
   * @param entry the entry to set
   */
  public void setEntry(WhitelistEntry entry) {
    this.entry = entry;
  }
  
}
