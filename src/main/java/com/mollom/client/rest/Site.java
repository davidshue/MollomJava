package com.mollom.client.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author johan
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Site {

  private String publicKey;
  private String privateKey;
  private String url;
  private String email;
  private String languages;
  private String subscriptionType;
  private String platformName;
  private String platformVersion;
  private String clientName;
  private String clientVersion;
  private String ownerId;

  /**
   * @return the publicKey
   */
  public String getPublicKey() {
    return publicKey;
  }

  /**
   * @param publicKey the publicKey to set
   */
  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  /**
   * @return the privateKey
   */
  public String getPrivateKey() {
    return privateKey;
  }

  /**
   * @param privateKey the privateKey to set
   */
  public void setPrivateKey(String privateKey) {
    this.privateKey = privateKey;
  }

  /**
   * @return the url
   */
  public String getUrl() {
    return url;
  }

  /**
   * @param url the url to set
   */
  public void setUrl(String url) {
    this.url = url;
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
   * @return the languages
   */
  public String getLanguages() {
    return languages;
  }

  /**
   * @param languages the languages to set
   */
  public void setLanguages(String languages) {
    this.languages = languages;
  }

  /**
   * @return the subscriptionType
   */
  public String getSubscriptionType() {
    return subscriptionType;
  }

  /**
   * @param subscriptionType the subscriptionType to set
   */
  public void setSubscriptionType(String subscriptionType) {
    this.subscriptionType = subscriptionType;
  }

  /**
   * @return the platformName
   */
  public String getPlatformName() {
    return platformName;
  }

  /**
   * @param platformName the platformName to set
   */
  public void setPlatformName(String platformName) {
    this.platformName = platformName;
  }

  /**
   * @return the platformVersion
   */
  public String getPlatformVersion() {
    return platformVersion;
  }

  /**
   * @param platformVersion the platformVersion to set
   */
  public void setPlatformVersion(String platformVersion) {
    this.platformVersion = platformVersion;
  }

  /**
   * @return the clientName
   */
  public String getClientName() {
    return clientName;
  }

  /**
   * @param clientName the clientName to set
   */
  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

  /**
   * @return the clientVersion
   */
  public String getClientVersion() {
    return clientVersion;
  }

  /**
   * @param clientVersion the clientVersion to set
   */
  public void setClientVersion(String clientVersion) {
    this.clientVersion = clientVersion;
  }

    /**
     * @return the owner
     */
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
