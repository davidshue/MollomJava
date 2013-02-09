/**
 * Copyright (c) 2010-2012 Mollom
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

import com.mollom.client.datastructures.CheckContentRequest;
import com.mollom.client.datastructures.CheckContentResponse;
import com.mollom.client.datastructures.GetCaptchaResponse;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.MultivaluedMap;

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

  /**
   * An enum indicating the feedback type for a message.
   */
  public static enum Feedback {

    APPROVE, SPAM, PROFANITY, QUALITY, UNWANTED, DELETE
  };

  /**
   * The possible checks that can be executed on a piece of text.
   *
   *  - SPAM: check the text for spaminess
   *  - PROFANITY: check the text for foul language
   *  - QUALITY: estimate the quality of the text
   *  - LANGUAGE: determine the most likely language for the text
   */
  public static enum ContentCheck {

    SPAM, PROFANITY, QUALITY, LANGUAGE
  };

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
    String path = "/content";
    if (parameters.sessionID != null) {
      path += "/" + parameters.sessionID;
    }

    MultivaluedMap<String, String> request = new MultivaluedMapImpl();
    add(request, "honeypot", parameters.honeypot);

    // set the processing parameters
    for (String check : parameters.checks) {
      add(request, "checks", check);
    }
    add(request, "strictness", parameters.strictness);

    // post information
    add(request, "postTitle", parameters.postTitle);
    add(request, "postBody", parameters.postBody);

    // author information
    add(request, "authorIp", parameters.authorIP);
    add(request, "authorId", parameters.authorID);
    add(request, "authorOpenid", parameters.authorOpenID);
    add(request, "authorName", parameters.authorName);
    add(request, "authorMail", parameters.authorMail);
    add(request, "authorUrl", parameters.authorUrl);

    return invoke("POST", path, request, CheckContentResponse.class);
  }

  private GetCaptchaResponse getCaptcha(String type, String contentId, boolean useSSL) throws Exception {
    String path = "/captcha";

    MultivaluedMap<String, String> request = new MultivaluedMapImpl();

    add(request, "contentId", contentId);
    add(request, "type", type);
    add(request, "ssl", useSSL);

    return invoke("POST", path, request, GetCaptchaResponse.class);
  }

  /**
   * Get an image captcha for the provided session. All images will be send over http.
   *
   * @see http://mollom.com/api/getImageCaptcha
   *
   * @param sessionID the sessionid of the message
   * @return a couple with an url for the captcha and a new session id
   * @throws Exception when something goes wrong while contacting Mollom
   */
  public GetCaptchaResponse getImageCaptcha(String sessionID) throws Exception {
    return getCaptcha("image", sessionID, false);
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
   * @return a couple with an url for the captcha and a new session id
   * @throws Exception when something goes wrong while contacting Mollom
   */
  public GetCaptchaResponse getImageCaptcha(String sessionID, boolean useSSL) throws Exception {
    return getCaptcha("image", sessionID, useSSL);
  }

  /**
   * Get an audio captcha for the provided session.
   * The captcha will always be send over plain http.
   *
   * @see http://mollom.com/api/getAudioCaptcha
   *
   * @param sessionID the sessionid of the message
   * @return a couple with an url for the captcha and a new session id
   * @throws Exception when something goes wrong while contacting Mollom
   */
  public GetCaptchaResponse getAudioCaptcha(String sessionID) throws Exception {
    return getCaptcha("audio", sessionID, false);
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
   * @param useSSL boolean to indicating if we need to use ssl to serve the captcha
   * @return a couple with an url for the captcha and a new session id
   * @throws Exception when something goes wrong while contacting Mollom
   */
  public GetCaptchaResponse getAudioCaptcha(String sessionID, boolean useSSL) throws Exception {
    return getCaptcha("Audio", sessionID, useSSL);
  }

  /**
   * Validate the visitors CAPTCHA answer.
   *
   * @see http://mollom.com/api/checkCaptcha
   *
   * @param captchaId the sessionID of the captcha
   * @param solution the visitors captcha solution
   * @param authorIP the visitors ip address
   * @return true if the solution is correct, false if incorrect.
   * @throws Exception when something goes wrong while contacting Mollom
   */
  public boolean checkCaptcha(String captchaId, String solution, String authorIP) throws Exception {
    if (captchaId == null || captchaId.isEmpty()) {
      return false;
    }
    
    String path = "/captcha/" + captchaId;

    MultivaluedMap<String, String> request = new MultivaluedMapImpl();
    add(request, "solution", solution);
    add(request, "authorIp", authorIP);

    return invoke("POST", path, request, Boolean.class);
  }

  /**
   * Send feedback to Mollom about the message that corresponds with the provided
   * session_id. Using this method, a message can be marked as one of the
   * five classes: 'ham', 'spam', 'profanity', 'low-quality' or 'unwanted'.
   * The 'ham' feedback is only available for Mollom Premium customers.
   *
   * @see http://mollom.com/api/sendFeedback
   *
   * @param contentId the id identifying the message
   * @param feedback an indicator with feedback about the message
   * @throws Exception when something goes wrong while contacting Mollom
   */
  public void sendFeedback(String contentId, String captchaId, Feedback feedback) throws Exception {
    if (contentId == null || contentId.isEmpty() || captchaId == null || captchaId.isEmpty()) {
      return;
    }

    MultivaluedMap<String, String> request = new MultivaluedMapImpl();
    add(request, "contentId", contentId);
    add(request, "captchaId", captchaId);
    add(request, "feedback", feedback);

    // discard the result, always true
    invoke("POST", "/feedback", request, Boolean.class);
  }
}
