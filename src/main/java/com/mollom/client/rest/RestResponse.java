/**
 * Copyright (c) 2010-2012 Mollom. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. * Redistributions in
 * binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
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
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class RestResponse {

  public static final int CODE_OK = 200;
  public static final int CODE_NOT_FOUND = 404;
  public static final int CODE_FAILURE = 1000;
  private int code = CODE_OK;
  private String message;

  public RestResponse() {
  }

  public RestResponse(int code, String message) {
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

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
