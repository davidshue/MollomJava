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
  private String authorName;
  private String authorUrl;
  private String authorMail;
  private String authorIp;
  private String authorId;
  private String[] authorOpenIds;
  private String honeypot;

  private int rateLimit;

  // Fields injected by Mollom upon captcha verification
  private int solved;
  private String reason;

  public Captcha() {
    solved = -1;
    rateLimit = 0;
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

  String getAuthorName() {
    return authorName;
  }

  String getAuthorUrl() {
    return authorUrl;
  }

  String getAuthorMail() {
    return authorMail;
  }

  String[] getAuthorOpenIds() {
    return authorOpenIds;
  }

  String getHoneypot() {
    return honeypot;
  }

  int getRateLimit() {
    return rateLimit;
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

  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }

  public void setAuthorUrl(String authorUrl) {
    this.authorUrl = authorUrl;
  }

  public void setAuthorMail(String authorMail) {
    this.authorMail = authorMail;
  }

  public void setAuthorOpenIds(String[] authorOpenIds) {
    this.authorOpenIds = authorOpenIds;
  }

  public void setHoneypot(String honeypot) {
    this.honeypot = honeypot;
  }

  /**
   * Seconds that must have passed by for the same author to post again.
   * Defaults to 0 seconds.
   */
  public void setRateLimit(int rateLimit) {
    this.rateLimit = rateLimit;
  }
}
