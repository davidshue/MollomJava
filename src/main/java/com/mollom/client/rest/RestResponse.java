package com.mollom.client.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is the base response for all Mollom REST calls. It contains a status
 * code and a message.<br>
 * <dl>
 *  <dt>code</dt>
 *  <dd>A status code returned in addition to the (corresponding, if possible) HTTP status code.
 *    <ol>
 *      <li>0: Success</li>
 *      <li>1: Failure, configuration/validation error (like code 1000 in old API)</li>
 *    </ol>
 *  </dd>
 *  <dt>message</dt>
 *  <dd>An error message clarifying the error status code, if any.</dd>
 * </dl>
 */
@XmlRootElement(name="response")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class RestResponse {

  public static final int CODE_OK = 200;
  public static final int CODE_NOT_FOUND = 404;
  public static final int CODE_FAILURE = 1000;

  private int code = CODE_OK;

  private String message;

  public RestResponse () {}

  public RestResponse (int code, String message) {
    this.code = code;
    this.message = message;
  }

  /**
   * @return the code
   */
  public int getCode() {
    return code;
  }

  /**
   * @param code the code to set
   */
  public void setCode(int code) {
    this.code = code;
  }

  public String getMessage () {
    return message;
  }

  public void setMessage (String message) {
    this.message = message;
  }
}
