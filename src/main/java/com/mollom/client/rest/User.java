/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mollom.client.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * Representation of a User in mollom (not in HMS).
 * A User is uniquely identified by its <code>userId</code>
 * 
 *
 * @author johan
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="GenericUser")
public class User {
  
  public static String version = "1";
  
  @XmlElement(name="uid")
  private String userId;
  @XmlElement(name="mail")
  private String email;
  @XmlElement(name="name")
  private String name;
  private String password;

  /**
   * @return the name
   */
  public String getUserId() {
    return userId;
  }

  /**
   * @param name the name to set
   */
  public void setUserId(String userId) {
    this.userId = userId;
  }

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @param email the email to set
   */
  public void setEmail(String email) {
    this.email = email;
  }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }
  
}
