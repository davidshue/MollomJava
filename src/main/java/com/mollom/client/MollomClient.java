/**
 * Copyright (c) 2010, Mollom
 * All rights reserved.
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
package com.mollom.client;

import com.mollom.client.core.MollomRequest;
import com.mollom.client.datastructures.CheckContentRequest;
import com.mollom.client.datastructures.CheckContentResponse;
import com.mollom.client.datastructures.GetCaptchaResponse;
import com.mollom.client.datastructures.Language;
import com.mollom.client.datastructures.ReputationResponse;

/**
 * The MollomClient provides in interface to the content checking and captcha
 * functionality of Mollom.
 *
 * @author Thomas Meire
 */
public class MollomClient extends Mollom {

  public MollomClient(String publicKey, String privateKey) {
    super(publicKey, privateKey);
  }

  public MollomClient(Protocol protocol, String publicKey, String privateKey) {
    super(protocol, publicKey, privateKey);
  }

  /* because there is no equiv for php's implode or perls join */
  private <T> String join(T[] items) {
    if (items == null || items.length == 0) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    sb.append(items[0].toString().toLowerCase());
    for (int i = 1; i < items.length; i++) {
      sb.append(",");
      sb.append(items[i].toString().toLowerCase());
    }
    return sb.toString();
  }

  /**
   * The possible checks that can be executed on a piece of text
   *
   *  - SPAM: check the text for spaminess
   *  - PROFANITY: check the text for foul language
   *  - QUALITY: estimate the quality of the text
   *  - LANGUAGE: determine the most likely language for the text
   */
  public enum ContentCheck {

    SPAM, PROFANITY, QUALITY, LANGUAGE, SENTIMENT

  }

  /**
   * @deprecated
   * @see MollomClient#checkContent(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String[], boolean)
   */
  public CheckContentResponse checkContent(String sessionID,
          String postTitle, String postBody, String authorName,
          String authorUrl, String authorMail, String authorOpenID,
          String authorIP, String authorID, String reputation, String[] classifiers) throws Exception {
    return checkContent(sessionID, postTitle, postBody, authorName, authorUrl, authorMail, authorOpenID, authorIP, authorID, reputation, classifiers, false);
  }

  /**
   * @deprecated
   * Check the post for spam.
   *
   * NOTE: There are currently no values specified for the classifier list.
   * Specifying a classifier will result in an error message.
   *
   * @see http://mollom.com/api/checkContent
   *
   * @param sessionID the session id for this post (or null if it's the first submit)
   * @param postTitle	the title of the post
   * @param postBody the body of the post
   * @param authorName the name of the author
   * @param authorUrl the url of the author
   * @param authorMail the mail of the author
   * @param authorOpenID the openid identifier of the author
   * @param authorIP the ip of the author
   * @param authorID the unique site id of the user
   * @param reputation the name of a Mollom reputation model
   * @param classifiers a list of strings identifying classifiers
   * @param testing if mollom needs to be contacted in testing mode
   * @return An object containing a sessionid, ham/spam indicator
   * @throws Exception when something goes wrong while contacting Mollom
   */
  public CheckContentResponse checkContent(String sessionID,
          String postTitle, String postBody, String authorName,
          String authorUrl, String authorMail, String authorOpenID,
          String authorIP, String authorID, String reputation, String[] classifiers,
          boolean testing) throws Exception {
    return checkContent(sessionID, postTitle, postBody, authorName, authorUrl, authorMail, authorOpenID, authorIP, authorID, reputation, classifiers, null, testing);
  }

  /**
   * @deprecated
   * @see MollomClient#checkContent(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String[], com.mollom.client.MollomClient.ContentCheck[], boolean)
   */
  public CheckContentResponse checkContent(String sessionID,
          String postTitle, String postBody, String authorName,
          String authorUrl, String authorMail, String authorOpenID,
          String authorIP, String authorID, String reputation,
          String[] classifiers, ContentCheck[] checks) throws Exception {
    return checkContent(sessionID, postTitle, postBody, authorName, authorUrl, authorMail, authorOpenID, authorIP, authorID, reputation, classifiers, checks, false);
  }

  /**
   * @deprecated
   * Execute one or more checks on the provided text.
   *
   * NOTE: There are currently no values specified for the classifier list.
   * Specifying a classifier will result in an error message.
   *
   * @see http://mollom.com/api/checkContent
   *
   * @param sessionID the session id for this post (or null if it's the first submit)
   * @param postTitle	the title of the post
   * @param postBody the body of the post
   * @param authorName the name of the author
   * @param authorUrl the url of the author
   * @param authorMail the mail of the author
   * @param authorOpenID the openid identifier of the author
   * @param authorIP the ip of the author
   * @param authorID the unique site id of the user
   * @param reputation the name of a Mollom reputation model
   * @param items a list of strings identifying classifiers
   * @param checks a list of checks that need to be executed
   * @param testing if mollom needs to be contacted in testing mode
   * @return An object containing the results of the checks
   * @throws Exception when something goes wrong while contacting Mollom
   */
  public CheckContentResponse checkContent(String sessionID,
          String postTitle, String postBody, String authorName,
          String authorUrl, String authorMail, String authorOpenID,
          String authorIP, String authorID, String reputation,
          String[] classifiers, ContentCheck[] checks, boolean testing) throws Exception {
    MollomRequest request = createNewRequest("checkContent", testing);

    request.addParameter("session_id", sessionID);
    request.addParameter("post_title", postTitle);
    request.addParameter("post_body", postBody);
    request.addParameter("author_name", authorName);
    request.addParameter("author_url", authorUrl);
    request.addParameter("author_mail", authorMail);
    request.addParameter("author_openid", authorOpenID);
    request.addParameter("author_ip", authorIP);
    request.addParameter("author_id", authorID);
    request.addParameter("reputation", reputation);
    request.addParameter("classifier", join(classifiers));
    request.addParameter("checks", join(checks));

    return invoke(request, CheckContentResponse.class);
  }

  /**
   * Execute one or more checks on the provided text.
   *
   * @see http://mollom.com/api/checkContent
   *
   * @param parameters The parameters for mollom.checkContent
   * @return the response of the mollom.checkContent call
   * @throws Exception
   */
  public CheckContentResponse checkContent (CheckContentRequest parameters) throws Exception {
    return checkContent(parameters, false);
  }

  /**
   * Execute one or more checks on the provided text.
   *
   * @see http://mollom.com/api/checkContent
   *
   * @param parameters The parameters for mollom.checkContent
   * @param testing true if this request must be executed in testing mode
   * @return the response of the mollom.checkContent call
   * @throws Exception
   */
  public CheckContentResponse checkContent (CheckContentRequest parameters, boolean testing) throws Exception {
    MollomRequest request = createNewRequest ("checkContent", testing);

    request.addParameter("session_id", parameters.sessionID);
    request.addParameter("honeypot", parameters.honeypot);

    // set the processing parameters
    request.addParameter("checks", join(parameters.checks));
    request.addParameter("reputation", parameters.reputation);
    request.addParameter("classifier", join(parameters.classifiers));
    request.addParameter("strictness", parameters.strictness);

    // post information
    request.addParameter("post_title", parameters.postTitle);
    request.addParameter("post_body", parameters.postBody);

    // author information
    request.addParameter("author_ip", parameters.authorIP);
    request.addParameter("author_id", parameters.authorID);
    request.addParameter("author_openid", parameters.authorOpenID);
    request.addParameter("author_name", parameters.authorName);
    request.addParameter("author_mail", parameters.authorMail);
    request.addParameter("author_url", parameters.authorUrl);

    return invoke(request, CheckContentResponse.class);
  }

  private GetCaptchaResponse getCaptcha(String type, String sessionID, String authorIP, boolean useSSL, boolean testing) throws Exception {
    MollomRequest request = createNewRequest("get" + type + "Captcha", testing);
    request.addParameter("session_id", sessionID);
    request.addParameter("author_ip", authorIP);
    request.addParameter("ssl", useSSL);

    return invoke(request, GetCaptchaResponse.class);
  }

  /**
   * Get an image captcha for the provided session. All images will be send over http.
   *
   * @see http://mollom.com/api/getImageCaptcha
   *
   * @param sessionID the sessionid of the message
   * @param authorIP the ip address of the author
   * @return a couple with an url for the captcha and a new session id
   * @throws Exception when something goes wrong while contacting Mollom
   */
  public GetCaptchaResponse getImageCaptcha(String sessionID, String authorIP) throws Exception {
    return getCaptcha("Image", sessionID, authorIP, false, false);
  }

  /**
   * Get an image captcha for the provided session.
   *
   * NOTE: The value of the useSSL parameter is only taken into account for
   * 'Mollom Plus' and 'Mollom Premium' users. Captcha's for 'Mollom Free'
   * users will always be send over plain http.
   *
   * @see http://mollom.com/api/getImageCaptcha
   *
   * @param sessionID the sessionid of the message
   * @param authorIP the ip address of the author
   * @param testing if mollom needs to be contacted in testing mode
   * @return a couple with an url for the captcha and a new session id
   * @throws Exception when something goes wrong while contacting Mollom
   */
  public GetCaptchaResponse getImageCaptcha(String sessionID, String authorIP, boolean useSSL) throws Exception {
    return getCaptcha("Image", sessionID, authorIP, useSSL, false);
  }

  /**
   * Get an image captcha for the provided session.
   *
   * NOTE: The value of the useSSL parameter is only taken into account for
   * 'Mollom Plus' and 'Mollom Premium' users. Captcha's for 'Mollom Free'
   * users will always be send over plain http.
   *
   * @see http://mollom.com/api/getImageCaptcha
   *
   * @param sessionID the sessionid of the message
   * @param authorIP the ip address of the author
   * @param useSSL boolean to indicating if we need to use ssl to serve the captcha
   * @param testing if mollom needs to be contacted in testing mode
   * @return a couple with an url for the captcha and a new session id
   * @throws Exception when something goes wrong while contacting Mollom
   */
  public GetCaptchaResponse getImageCaptcha(String sessionID, String authorIP, boolean ssl, boolean testing) throws Exception {
    return getCaptcha("Image", sessionID, authorIP, ssl, testing);
  }

  /**
   * Get an audio captcha for the provided session.
   * The captcha will always be send over plain http.
   *
   * @see http://mollom.com/api/getAudioCaptcha
   *
   * @param sessionID the sessionid of the message
   * @param authorIP the ip address of the author
   * @return a couple with an url for the captcha and a new session id
   * @throws Exception when something goes wrong while contacting Mollom
   */
  public GetCaptchaResponse getAudioCaptcha(String sessionID, String authorIP) throws Exception {
    return getCaptcha("Audio", sessionID, authorIP, false, false);
  }

  /**
   * Get an audio captcha for the provided session.
   * The captcha will always be send over plain http.
   *
   * NOTE: The value of the useSSL parameter is only taken into account for
   * 'Mollom Plus' and 'Mollom Premium' users. Captcha's for 'Mollom Free'
   * users will always be send over plain http.
   *
   * @see http://mollom.com/api/getAudioCaptcha
   *
   * @param sessionID the sessionid of the message
   * @param authorIP the ip address of the author
   * @param useSSL boolean to indicating if we need to use ssl to serve the captcha
   * @return a couple with an url for the captcha and a new session id
   * @throws Exception when something goes wrong while contacting Mollom
   */
  public GetCaptchaResponse getAudioCaptcha(String sessionID, String authorIP, boolean useSSL) throws Exception {
    return getCaptcha("Audio", sessionID, authorIP, useSSL, false);
  }

  /**
   * Get an audio captcha for the provided session.
   *
   * NOTE: The value of the useSSL parameter is only taken into account for
   * 'Mollom Plus' and 'Mollom Premium' users. Captcha's for 'Mollom Free'
   * users will always be send over plain http.
   *
   * @see http://mollom.com/api/getAudioCaptcha
   *
   * @param sessionID the sessionid of the message
   * @param authorIP the ip address of the author
   * @param useSSL boolean to indicating if we need to use ssl to serve the captcha
   * @param testing if mollom needs to be contacted in testing mode
   * @return a couple with an url for the captcha and a new session id
   * @throws Exception when something goes wrong while contacting Mollom
   */
  public GetCaptchaResponse getAudioCaptcha(String sessionID, String authorIP, boolean useSSL, boolean testing) throws Exception {
    return getCaptcha("Audio", sessionID, authorIP, useSSL, testing);
  }

  /**
   * Validate the visitors CAPTCHA answer.
   *
   * @see http://mollom.com/api/checkCaptcha
   *
   * @param sessionID the sessionID of the captcha
   * @param solution the visitors captcha solution
   * @param authorIP the visitors ip address
   * @return true if the solution is correct, false if incorrect.
   * @throws Exception when something goes wrong while contacting Mollom
   */
  public boolean checkCaptcha(String sessionID, String solution, String authorIP) throws Exception {
    return checkCaptcha(sessionID, solution, authorIP, false);
  }

  /**
   * Validate the visitors CAPTCHA answer.
   *
   * @see http://mollom.com/api/checkCaptcha
   *
   * @param sessionID the sessionID of the captcha
   * @param solution the visitors captcha solution
   * @param authorIP the visitors ip address
   * @param testing if mollom needs to be contacted in testing mode
   * @return true if the solution is correct, false if incorrect.
   * @throws Exception when something goes wrong while contacting Mollom
   */
  public boolean checkCaptcha(String sessionID, String solution, String authorIP, boolean testing) throws Exception {
    MollomRequest request = createNewRequest("checkCaptcha", testing);
    request.addParameter("session_id", sessionID);
    request.addParameter("solution", solution);
    request.addParameter("author_ip", authorIP);

    return invoke(request, Boolean.class);
  }

  /**
   * @see MollomClient#detectLanguage(java.lang.String, boolean) 
   */
  @Deprecated
  public Language[] detectLanguage(String text) throws Exception {
    return detectLanguage(text, false);
  }

  /**
   * @deprecated This method is replaced by the checks parameter of the checkContent method
   * @see MollomClient#checkContent
   * 
   * Detect the language of the provided text. To have a higher confidence in
   * the detection, the text should be at least 15 characters long. It also
   * should not contain any markup, like html or bbcode.
   *
   * This method will return an array with couples (language, confidence):
   *	- language: an iso-639-1 code indicating a language, 'zxx' if the content
   *              is not linguistic or 'und' when the language could not be determined
   *	- confidence: a double between 0 and 1
   *
   * @see http://mollom.com/api/detectLanguage
   *
   * @param text The text that needs to be analysed.
   * @param testing if mollom needs to be contacted in testing mode
   * @return An array with pairs (language, confidence)
   * @throws Exception when something goes wrong while contacting Mollom
   */
  @Deprecated
  public Language[] detectLanguage(String text, boolean testing) throws Exception {
    MollomRequest request = createNewRequest("detectLanguage", testing);
    request.addParameter("text", text);

    return invoke(request, Language[].class);
  }

  /** An enum indicating the feedback type for a message */
  public enum Feedback {

    HAM, SPAM, PROFANITY, LOW_QUALITY, UNWANTED

  };

  /**
   * Send feedback to Mollom about the message that corresponds with the provided
   * session_id. Using this method, a message can be marked as one of the
   * five classes: 'ham', 'spam', 'profanity', 'low-quality' or 'unwanted'.
   * The 'ham' feedback is only available for Mollom Premium customers.
   *
   * @see http://mollom.com/api/sendFeedback
   *
   * @param sessionID the id identifying the message
   * @param feedback an indicator with feedback about the message
   * @throws Exception when something goes wrong while contacting Mollom
   */
  public void sendFeedback(String sessionID, Feedback feedback) throws Exception {
    sendFeedback(sessionID, feedback, false);
  }

  /**
   * Send feedback to Mollom about the message that corresponds with the provided
   * session_id. Using this method, a message can be marked as one of the
   * five classes: 'ham', 'spam', 'profanity', 'low-quality' or 'unwanted'.
   * The 'ham' feedback is only available for Mollom Premium customers.
   *
   * @see http://mollom.com/api/sendFeedback
   *
   * @param sessionID the id identifying the message
   * @param feedback an indicator with feedback about the message
   * @param testing if mollom needs to be contacted in testing mode
   * @throws Exception when something goes wrong while contacting Mollom
   */
  public void sendFeedback(String sessionID, Feedback feedback, boolean testing) throws Exception {
    MollomRequest request = createNewRequest("sendFeedback", testing);
    request.addParameter("session_id", sessionID);

    switch (feedback) {
      case HAM:
        request.addParameter("feedback", "ham");
      case SPAM:
        request.addParameter("feedback", "spam");
      case PROFANITY:
        request.addParameter("feedback", "profanity");
      case LOW_QUALITY:
        request.addParameter("feedback", "low-quality");
      case UNWANTED:
      default:
        request.addParameter("feedback", "unwanted");
    }

    // discard the result, always true
    invoke(request, Boolean.class);
  }

  /**
   * @see MollomClient#getAuthorReputation(java.lang.String, java.lang.String, java.lang.String, boolean) 
   */
  public ReputationResponse getAuthorReputation(String authorIP, String authorID, String authorOpenID) throws Exception {
    return getAuthorReputation(authorIP, authorID, authorOpenID, false);
  }

  /**
   * Get the reputation of an author. If authorIP and authorOpenID are given,
   * the reputation is based on the behaviour of the author on all sites
   * protected by Mollom.
   * The reputation consists of a string, a score and a confidence. The string
   * is either "good", "average" or "poor". The score is a double between 0 and
   * 1, where 0 is a spammer and 1 means trusted. If an author has no reputation
   * yet, the string is "unknown" and the score is 0.5
   *
   * @param authorIP the ip of the author
   * @param authorID the id of the author
   * @param authorOpenID the openid identifier of the author
   * @param testing if mollom needs to be contacted in testing mode
   * @return an object containing a reputation-string, the score and the confidence.
   * @throws Exception when something goes wrong while contacting Mollom
   */
  public ReputationResponse getAuthorReputation(String authorIP, String authorID, String authorOpenID, boolean testing)
          throws Exception {
    MollomRequest request = createNewRequest("getReputation", testing);
    request.addParameter("author_ip", authorIP);
    request.addParameter("author_id", authorID);
    request.addParameter("author_openid", authorOpenID);

    return invoke(request, ReputationResponse.class);
  }

  /**
   * @see MollomClient#resetAuthorReputation(java.lang.String, java.lang.String, java.lang.String, boolean)
   */
  public void resetAuthorReputation(String authorIP, String authorID, String authorOpenID) throws Exception {
    resetAuthorReputation(authorIP, authorID, authorOpenID, false);
  }

  /**
   * Reset the reputation of an author. Unless you're contacting a dedicated
   * Mollom server, only the authorID will be taken into account.
   *
   * @param authorIP the ip of the author
   * @param authorID the id of the author
   * @param authorOpenID the openid identifier of the author
   * @param testing if mollom needs to be contacted in testing mode
   * @throws Exception when something goes wrong while contacting Mollom
   */
  public void resetAuthorReputation(String authorIP, String authorID, String authorOpenID, boolean testing) throws Exception {
    MollomRequest request = createNewRequest("resetReputation", testing);
    request.addParameter("author_ip", authorIP);
    request.addParameter("author_id", authorID);
    request.addParameter("author_openid", authorOpenID);

    /* always returns true, discard response */
    invoke(request, Boolean.class);
  }
}
