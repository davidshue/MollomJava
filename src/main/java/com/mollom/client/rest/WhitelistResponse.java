package com.mollom.client.rest;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author johan
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WhitelistResponse extends RestResponse {
  
  @XmlElementWrapper(name="list")
  @XmlElement(name="entry")
  private List<WhitelistEntry> entries;

  /**
   * @return the entries
   */
  public List<WhitelistEntry> getEntries() {
    return entries;
  }

  /**
   * @param entries the entries to set
   */
  public void setEntries(List<WhitelistEntry> entries) {
    this.entries = entries;
  }

}
