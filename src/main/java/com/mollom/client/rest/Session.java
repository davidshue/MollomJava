/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
  
package com.mollom.client.rest;
  
/**
 *
 * @author johan
 */
public class Session {
  
  private String hash;
  private boolean captchaSolved;
  
  /**
   * @return the captchaSolved
   */
  public boolean isCaptchaSolved() {
    return captchaSolved;
  }
  
  /**
   * @param captchaSolved the captchaSolved to set
   */
  public void setCaptchaSolved(boolean captchaSolved) {
    this.captchaSolved = captchaSolved;
  }
  
  /**
   * @return the hash
   */
  public String getHash() {
    return hash;
  }
  
  /**
   * @param hash the hash to set
   */
  public void setHash(String hash) {
    this.hash = hash;
  }
  
}
