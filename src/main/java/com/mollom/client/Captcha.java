package com.mollom.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A Captcha to be checked.
 */
@XmlRootElement(name = "captcha")
@XmlAccessorType(XmlAccessType.FIELD)
public class Captcha {
  // Fields injected by Mollom upon creation of the captcha resource
  private String id; // Captcha ID
  private String url;

  // Fields injected by the user for verification
  private String solution;
  private String authorIp;
  private String authorId;

  // Fields injected by Mollom upon captcha verification
  private int solved;
  private String reason;

  public Captcha() {
    solved = -1;
  }

  public String getId() {
    return id;
  }

  public String getUrl() {
    return url;
  }

  String getSolution() {
    return solution;
  }

  String getAuthorIp() {
    return authorIp;
  }

  String getAuthorId() {
    return authorId;
  }

  public String getReason() {
    return reason;
  }

  public void setSolution(String solution) {
    this.solution = solution;
  }

  public void setAuthorIp(String authorIp) {
    this.authorIp = authorIp;
  }

  public void setAuthorId(String authorId) {
    this.authorId = authorId;
  }

  void setId(String id) {
    this.id = id;
  }

  void setSolved(int solved) {
    this.solved = solved;
  }

  void setReason(String reason) {
    this.reason = reason;
  }

  public boolean isSolved() {
    if (solved == -1) {
      throw new MollomIllegalUsageException("Cannot check whether or not a CAPTCHA is solved without calling checkCaptcha first.");
    }

    return solved == 1;
  }
}
