/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mollom.client.rest;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author johan
 */
@XmlRootElement
public class SiteResponse extends RestResponse {
  
  private Site site;

  /**
   * @return the site
   */
  public Site getSite() {
    return site;
  }

  /**
   * @param site the site to set
   */
  public void setSite(Site site) {
    this.site = site;
  }
  
  
}
