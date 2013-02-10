package com.mollom.client.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author johan
 */
@XmlRootElement(name="captcharesponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class CaptchaResponse extends RestResponse {
  
  public CaptchaResponse() {}
    
  private Captcha captcha;
  
  public CaptchaResponse (String id, int solved, String reason) {
    this.captcha = new Captcha(id, solved, reason);
  }
  
  public String getId() {
    return captcha.id;
  }

  public boolean isSolved () {
    return captcha.solved == 1;
  }

  public String getReason () {
    return captcha.reason;
  }
  
  @XmlAccessorType(XmlAccessType.FIELD)
  static class Captcha {
      
    public Captcha() {}
    
    public Captcha (String id, int solved, String reason) {
      this.id = id;
      this.solved = solved;
      this.reason = reason;
    }
    
    private String id;
    private int solved;
    private String reason;
  }
}
