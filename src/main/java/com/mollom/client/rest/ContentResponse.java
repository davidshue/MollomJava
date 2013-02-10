/**
 * Copyright (c) 2010-2012 Mollom. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
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

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author johan
 */
@XmlRootElement(name = "contentresponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContentResponse extends RestResponse {

  public static final String SPAM_HAM = "ham";
  public static final String SPAM_SPAM = "spam";
  public static final String SPAM_UNSURE = "unsure";
  public static final Double SCORE_HAM = 0.;
  public static final Double SCORE_SPAM = 1.;
  public static final Double SCORE_UNSURE = .5;

  private Content content;

  /**
   * @return the content
   */
  public Content getContent() {
    return content;
  }

  /**
   * @param content the content to set
   */
  public void setContent(Content content) {
    this.content = content;
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Content {

    private String id;
    private Double spamScore;
    private Double qualityScore;
    private Double profanityScore;
    private Double sentimentScore;
    @XmlElementWrapper(name = "languages")
    @XmlElement(name = "language")
    private List<Language> languages;
    private String reason;
    private String type;
    private String postTitle;
    private String postBody;
    private String authorName;
    private String authorUrl;
    private String authorMail;
    private String authorIp;
    private String authorId;
    private String authorOpenid;
    private String spamClassification;

    /**
     * @return the id
     */
    public String getSessionId() {
      return id;
    }

    /**
     * @param id the id to set
     */
    public void setSessionId(String sessionId) {
      this.id = sessionId;
    }

    /**
     * @return the spamScore
     */
    public Double getSpamScore() {
      return spamScore;
    }

    /**
     * @param spamScore the spamScore to set
     */
    public void setSpamScore(Double spam) {
      this.spamScore = spam;
    }

    /**
     * @return the qualityScore
     */
    public Double getQualityScore() {
      return qualityScore;
    }

    /**
     * @param qualityScore the qualityScore to set
     */
    public void setQualityScore(Double quality) {
      this.qualityScore = quality;
    }

    /**
     * @return the profanityScore
     */
    public Double getProfanityScore() {
      return profanityScore;
    }

    /**
     * @param profanityScore the profanityScore to set
     */
    public void setProfanityScore(Double profanity) {
      this.profanityScore = profanity;
    }

    /**
     * @return the sentimentScore
     */
    public Double getSentimentScore() {
      return sentimentScore;
    }

    /**
     * @param sentimentScore the sentimentScore to set
     */
    public void setSentimentScore(Double sentiment) {
      this.sentimentScore = sentiment;
    }

    /**
     * @return the languages
     */
    public List<Language> getLanguages() {
      return languages;
    }

    /**
     * @param languages the languages to set
     */
    public void setLanguages(List<Language> languages) {
      this.languages = languages;
    }

    /**
     * @return the reason
     */
    public String getReason() {
      return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(String reason) {
      this.reason = reason;
    }

    /**
     * @return the content type
     */
    public String getType() {
      return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
      this.type = type;
    }

    /**
     * @return the postTitle
     */
    public String getPostTitle() {
      return postTitle;
    }

    /**
     * @param postTitle the postTitle to set
     */
    public void setPostTitle(String postTitle) {
      this.postTitle = postTitle;
    }

    /**
     * @return the postBody
     */
    public String getPostBody() {
      return postBody;
    }

    /**
     * @param postBody the postBody to set
     */
    public void setPostBody(String postBody) {
      this.postBody = postBody;
    }

    /**
     * @return the authorName
     */
    public String getAuthorName() {
      return authorName;
    }

    /**
     * @param authorName the authorName to set
     */
    public void setAuthorName(String authorName) {
      this.authorName = authorName;
    }

    /**
     * @return the authorUrl
     */
    public String getAuthorUrl() {
      return authorUrl;
    }

    /**
     * @param authorUrl the authorUrl to set
     */
    public void setAuthorUrl(String authorUrl) {
      this.authorUrl = authorUrl;
    }

    /**
     * @return the authorMail
     */
    public String getAuthorMail() {
      return authorMail;
    }

    /**
     * @param authorMail the authorMail to set
     */
    public void setAuthorMail(String authorMail) {
      this.authorMail = authorMail;
    }

    /**
     * @return the authorIp
     */
    public String getAuthorIp() {
      return authorIp;
    }

    /**
     * @param authorIp the authorIp to set
     */
    public void setAuthorIp(String authorIp) {
      this.authorIp = authorIp;
    }

    /**
     * @return the authorId
     */
    public String getAuthorId() {
      return authorId;
    }

    /**
     * @param authorId the authorId to set
     */
    public void setAuthorId(String authorId) {
      this.authorId = authorId;
    }

    /**
     * @return the authorOpenid
     */
    public String getAuthorOpenid() {
      return authorOpenid;
    }

    /**
     * @param authorOpenid the authorOpenid to set
     */
    public void setAuthorOpenid(String authorOpenid) {
      this.authorOpenid = authorOpenid;
    }

    /**
     * @return the spamClassification
     */
    public String getSpamResult() {
      return spamClassification;
    }

    /**
     * @param spamClassification the spamClassification to set
     */
    public void setSpamResult(String spamResult) {
      this.spamClassification = spamResult.toLowerCase();
    }
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Language {

    public static String LANGUAGE_UNKOWN = "zxx";
    public static String LANGUAGE_UNDEFINED = "und";
    private String languageCode;
    @XmlElement(name = "languageScore")
    private double confidence;

    public Language(String code, double conf) {
      languageCode = code;
      confidence = conf;
    }

    public Language() {
    }

    /**
     * @return the languageCode
     */
    public String getLanguageCode() {
      return languageCode;
    }

    /**
     * @param languageCode the languageCode to set
     */
    public void setLanguageCode(String languageCode) {
      this.languageCode = languageCode;
    }

    /**
     * @return the confidence
     */
    public double getConfidence() {
      return confidence;
    }

    /**
     * @param confidence the confidence to set
     */
    public void setConfidence(double confidence) {
      this.confidence = confidence;
    }
  }
}
