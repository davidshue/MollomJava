package com.mollom.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A single piece of Content to send to Mollom for classification.
 */
@XmlRootElement(name = "content")
@XmlAccessorType(XmlAccessType.FIELD)
public class Content {
  /**
   * Content ID is a unique identifier to Mollom.
   */
  private String id;
  
  // Fields injected by Mollom
  private double spamScore;
  private String spamClassification;
  private double profanityScore;
  private double qualityScore;
  private String reason;
  @XmlElementWrapper(name = "languages")
  @XmlElement(name = "language")
  private Language[] languages;
  
  // Fields used for classification; set by the user
  private String postTitle;
  private String postBody;
  private String authorName;
  private String authorUrl;
  private String authorMail;
  private String authorIp;
  private String authorId;
  private String[] authorOpenIds;
  private String honeypot;
  
  // Fields used to configure how Mollom behaves
  private Check[] checks;
  private boolean allowUnsure;
  private Strictness strictness;

  // Mollom content moderation platform specific fields managed by the library
  private boolean stored;
  private String url; // Absolute URL to the stored content
  private String contextUrl; // Absolute URL to parent/context content of the stored content
  private String contextTitle; // The title of the parent/context content of the stored content
  
  /**
   * Constructor used to create a new Content to be checked by Mollom.
   * Configures all of the default properties.
   */
  public Content() {
    allowUnsure = true;
    strictness = Strictness.NORMAL;
    checks = new Check[] { Check.SPAM };
    stored = false;
    spamScore = -1;
    profanityScore = -1;
    qualityScore = -1;
  }
  
  /**
   * @return Unique content ID assigned by Mollom.
   */
  public String getId() {
    return id;
  }

  /**
   * Values are floating point values with a precision of 2, ranging between 0.00 and 1.00.
   * 0.0 means ham
   * 1.0 means spam
   * 0.5 means unsure
   * Depending on the Mollom subscription plan, the returned scores can be real values between 0 and 1.
   * 
   * @return The determined spaminess of the content.
   * @throws MollomException
   *           If a QUALITY check was not performed beforehand.
   */
  public double getSpamScore() {
    if (spamScore == -1) {
      throw new MollomIllegalUsageException("Spam score cannot be determined without doing a SPAM check first.");
    }
    return spamScore;
  }

  /**
   * Values are floating point values with a precision of 2, ranging between 0.00 and 1.00.
   * 0.0 means not profane
   * 1.0 means profane
   * Depending on the Mollom subscription plan, the returned scores can be real values between 0 and 1.
   * 
   * @return The determined profanity of the content.
   * @throws MollomException
   *           If a PROFANITY check was not performed beforehand.
   */
  public double getProfanityScore() {
    if (profanityScore == -1) {
      throw new MollomIllegalUsageException("Profanity score cannot be determined without doing a PROFANITY check first.");
    }
    return profanityScore;
  }

  /**
   * Values are floating point values with a precision of 2, ranging between 0.00 and 1.00.
   * 0.0 means low quality content
   * 1.0 means high quality content
   * Only accessible if the content was checked with the QUALITY check.
   * Depending on the Mollom subscription plan, the returned scores can be real values between 0 and 1.
   * 
   * @return The determined quality of the content.
   * @throws MollomException
   *           If a QUALITY check was not performed beforehand.
   */
  public double getQualityScore() {
    if (qualityScore == -1) {
      throw new MollomIllegalUsageException("Quality score cannot be determined without doing a QUALITY check first.");
    }
    return qualityScore;
  }

  /**
   * @return A single-word string denoting the reason for the content classification.
   */
  public String getReason() {
    return reason;
  }

  /**
   * @return Detected language(s) for the content.
   * @throws MollomException
   *           If a LANGUAGE check was not performed beforehand.
   */
  public Language[] getLanguages() {
    if (languages == null) {
      throw new MollomIllegalUsageException("Language cannot be determined without doing a LANGUAGES check first.");
    }
    return languages;
  }

  String getPostTitle() {
    return postTitle;
  }

  String getPostBody() {
    return postBody;
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

  String getAuthorIp() {
    return authorIp;
  }

  String getAuthorId() {
    return authorId;
  }

  String[] getAuthorOpenIds() {
    return authorOpenIds;
  }

  /**
   * @return Whether the content is "ham" or desirable.
   * @throws MollomException
   *           If a SPAM check was not performed beforehand.
   */
  public boolean isHam() {
    if (spamClassification == null) {
      throw new MollomIllegalUsageException("Spamminess of content cannot be determined without doing a SPAM check first.");
    }
    return "ham".equals(spamClassification);
  }

  /**
   * @return Whether the content is "spam" or undesirable.
   * @throws MollomException
   *           If a SPAM check was not performed beforehand.
   */
  public boolean isSpam() {
    if (spamClassification == null) {
      throw new MollomIllegalUsageException("Spamminess of content cannot be determined without doing a SPAM check first.");
    }
    return "spam".equals(spamClassification);
  }

  /**
   * If the Content is determined to be unsure, it is recommended that a CAPTCHA is presented to the user.
   * 
   * @return Whether the content is "unsure" or Mollom isn't sure whether it is desirable or not.
   * @throws MollomException
   *           If a SPAM check was not performed beforehand.
   */
  public boolean isUnsure() {
    if (spamClassification == null) {
      throw new MollomIllegalUsageException("Spamminess of content cannot be determined without doing a SPAM check first.");
    }
    return "unsure".equals(spamClassification);
  }

  /**
   * @param postTitle The title of the content.
   */
  public void setPostTitle(String postTitle) {
    this.postTitle = postTitle;
  }

  /**
   * @param postBody The body of the content.
   */
  public void setPostBody(String postBody) {
    this.postBody = postBody;
  }

  /**
   * @param authorName The name of the content author.
   */
  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }

  /**
   * @param authorUrl The homepage/website URL of the content author.
   */
  public void setAuthorUrl(String authorUrl) {
    this.authorUrl = authorUrl;
  }

  /**
   * @param authorMail The email address of the content author.
   */
  public void setAuthorMail(String authorMail) {
    this.authorMail = authorMail;
  }

  /**
   * @param authorIp The IP address of the content author.
   */
  public void setAuthorIp(String authorIp) {
    this.authorIp = authorIp;
  }

  /**
   * @param authorId The local user ID on the client site of the content author.
   */
  public void setAuthorId(String authorId) {
    this.authorId = authorId;
  }

  /**
   * @param authorOpenIds Open IDs of the content author
   */
  public void setAuthorOpenIds(String[] authorOpenIds) {
    this.authorOpenIds = authorOpenIds;
  }

  /**
   * The value of a client-side honeypot form element, if non-empty.
   * The honeypot is a trap to detect automated attackers and will generally result in a 
   * SPAM classification if filled out.
   */
  public void setHoneypot(String honeypot) {
    this.honeypot = honeypot;
  }

  /**
   * Configure the checks to request Mollom to perform. Defaults to spam only.
   */
  public void setChecks(Check... checks) {
    this.checks = checks;
  }

  /**
   * Whether or not to allow Mollom to return an unsure classification.
   * Defaults to true. (highly recommended)
   */
  public void setAllowUnsure(boolean allowUnsure) {
    this.allowUnsure = allowUnsure;
  }

  /**
   * Configure the strictness of Mollom spaminess checks.
   */
  public void setStrictness(Strictness strictness) {
    this.strictness = strictness;
  }

  String getSpamClassification() {
    return spamClassification;
  }

  String getHoneypot() {
    return honeypot;
  }

  Check[] getChecks() {
    return checks;
  }

  boolean isAllowUnsure() {
    return allowUnsure;
  }

  Strictness getStrictness() {
    return strictness;
  }

  boolean isStored() {
    return stored;
  }

  String getUrl() {
    return url;
  }

  String getContextUrl() {
    return contextUrl;
  }

  String getContextTitle() {
    return contextTitle;
  }

  void setId(String id) {
    this.id = id;
  }

  void setSpamScore(double spamScore) {
    this.spamScore = spamScore;
  }

  void setSpamClassification(String spamClassification) {
    this.spamClassification = spamClassification;
  }

  void setProfanityScore(double profanityScore) {
    this.profanityScore = profanityScore;
  }

  void setQualityScore(double qualityScore) {
    this.qualityScore = qualityScore;
  }

  void setReason(String reason) {
    this.reason = reason;
  }

  void setLanguages(Language[] languages) {
    this.languages = languages;
  }

  void setStored(boolean stored) {
    this.stored = stored;
  }

  void setUrl(String url) {
    this.url = url;
  }

  void setContextUrl(String contextUrl) {
    this.contextUrl = contextUrl;
  }

  void setContextTitle(String contextTitle) {
    this.contextTitle = contextTitle;
  }
}
