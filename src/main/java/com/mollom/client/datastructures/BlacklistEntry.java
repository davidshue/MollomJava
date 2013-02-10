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

import com.mollom.client.MollomBlacklist.Context;
import com.mollom.client.MollomBlacklist.Match;
import com.mollom.client.MollomBlacklist.Reason;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class that is used to communicate with Mollom about text blacklists.
 * 
 * @see MollomBlacklist
 * @author Thomas Meire
 */
@XmlRootElement
public class BlacklistEntry {

  /** 
   * The blacklist id.
   */
  public String id;
	/**
   * The blacklisted text.
   */
	public String text;
	/**
   * The context in which it is blacklisted.
   */
	public Context context;
	/**
   * The reason why it is blacklisted.
   */
	public Reason reason;
	/**
   * The way this blacklisted text must be matched.
   */
	public Match match;
	/**
   * A timestamp indicating when this text was added to the blacklist.
   */
	public long created;
  /**
   * The status of this entry.
   */
  public boolean status;
  /**
   * A note about this blacklist entry.
   */
  public String note;
}
