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
package com.mollom.client.datastructures;

import com.mollom.client.MollomClient;
import com.mollom.client.rest.ContentResponse.Content;
import com.mollom.client.rest.ContentResponse.Language;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A class to contain the reponses from mollom.checkContent
 *
 * @see MollomClient#checkContent
 * @author Thomas Meire
 */
@XmlRootElement
public class CheckContentResponse {

	public static final int HAM = 1;
	public static final int SPAM = 2;
	public static final int UNSURE = 3;
	/**
	 * Indicates whether a message was ham or spam
	 * @see #HAM
	 * @see #SPAM
	 * @see #UNSURE
	 */
	public int spam;
	/** Indicator for the quality of the post, between 0 and 1 */
	public Double quality;
	/** Indicator for the profanity of the post, between 0 and 1 */
	public Double profanity;
  /** List of possible languages */
  public List<Language> language;
	/** Session id, identifying this message */
	public String session_id;
  /** Indicator for the sentiment of the post, between 0 and 1 */
  public Double sentiment;

  public CheckContentResponse() {
  }

  public CheckContentResponse(Content content) {
    session_id = content.getSessionId();
    spam = spamStringToInt(content.getSpamResult());
    profanity = content.getProfanityScore();
    quality = content.getQualityScore();
    sentiment = content.getSentimentScore();
    language = content.getLanguages();
  }

  private int spamStringToInt (String spam) {
    System.err.println(spam);
    if ("ham".equals(spam)) {
      return 1;
    } else if ("spam".equals(spam)) {
      return 2;
    } else if ("unsure".equals(spam)) {
      return 3;
    } else {
      return 0;
    }
  }

  /**
   * @return true if the the content was spam
   */
  public boolean isSpam () {
    return spam == SPAM;
  }

  /**
   * @return true if the the content was ham
   */
  public boolean isHam () {
    return spam == HAM;
  }

  /**
   * @return true if the the content was unsure
   */
  public boolean isUnsure () {
    return spam == UNSURE;
  }
}
