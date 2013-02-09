package com.mollom.client.datastructures;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Thomas Meire
 */
@XmlRootElement
public class RestResponse {

  public int code;
  public String message;
}
