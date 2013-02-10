/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mollom.client.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author johan
 */
@XmlRootElement(name = "captcharesource")
@XmlAccessorType(XmlAccessType.FIELD)
public class CaptchaResource extends RestResponse {

  private Captcha captcha;

  public CaptchaResource() {
  }

  public CaptchaResource(String id, String url) {
    captcha = new Captcha(id, url);
  }

  public Captcha getCaptcha() {
    return captcha;
  }

  public String getId() {
    return captcha.getId();
  }

  public String getUrl() {
    return captcha.getUrl();
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  static class Captcha {

    private String id;
    private String url;

    Captcha() {
    }

    private Captcha(String id, String url) {
      this.id = id;
      this.url = url;
    }

    public String getId() {
      return id;
    }

    public String getUrl() {
      return url;
    }
  }
}
