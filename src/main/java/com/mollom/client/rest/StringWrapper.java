/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mollom.client.rest;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;


  @XmlRootElement
  public class StringWrapper {

    private List<String> values;

    public StringWrapper() {
    }

    /**
     * @return the values
     */
    public List<String> getValues() {
      return values;
    }

    /**
     * @param values the values to set
     */
    public void setValues(List<String> values) {
      this.values = values;
    }
  }
