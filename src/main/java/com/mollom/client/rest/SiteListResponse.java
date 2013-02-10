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
public class SiteListResponse extends RestResponse {

  @XmlElementWrapper(name="list")
  @XmlElement(name="site")
  private List<Site> sites;

  /**
   * @return the entries
   */
  public List<Site> getSites() {
    return sites;
  }

  /**
   * @param sites the entries to set
   */
  public void setSites(List<Site> sites) {
    this.sites = sites;
  }
}
