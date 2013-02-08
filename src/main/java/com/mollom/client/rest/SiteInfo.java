/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mollom.client.rest;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

 @XmlRootElement
  public  class SiteInfo {

    private String publicKey;
    private String privateKey;

    public SiteInfo() {
    }

    /**
     * @return the publicKey
     */
    public String getPublicKey() {
      return publicKey;
    }

    /**
     * @param publicKey the publicKey to set
     */
    public void setPublicKey(String publicKey) {
      this.publicKey = publicKey;
    }

    /**
     * @return the privateKey
     */
    public String getPrivateKey() {
      return privateKey;
    }

    /**
     * @param privateKey the privateKey to set
     */
    public void setPrivateKey(String privateKey) {
      this.privateKey = privateKey;
    }
  }
