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
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @see http://www.mollom.com/api/checkContent
 * @author Thomas Meire
 */
@XmlRootElement
public class CheckContentRequest {
  // For more information about these parameters, see the mollom documentation

  public String sessionID = null;

  // post information
  public String postTitle;

  public String postBody;

  // author information
  public String authorName;

  public String authorUrl;

  public String authorMail;

  public String authorIP;

  public String authorID;

  public String authorOpenID;

  // hidden honeypot field to trap bots
  public String honeypot;

  // What reputation model do we need to use?
  public String reputation;

  // Specify a custom classifier chain
  public String[] classifiers;

  // specify what checks need to be executed
  public MollomClient.ContentCheck[] checks;

  // specify how strict mollom needs to be on classifying content
  public Strictness strictness;

  public enum Strictness {STRICT, NORMAL, RELAXED}
}
