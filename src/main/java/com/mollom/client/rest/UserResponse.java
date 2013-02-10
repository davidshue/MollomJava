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
public class UserResponse  extends RestResponse{
  
  private User user;

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }
  
}
