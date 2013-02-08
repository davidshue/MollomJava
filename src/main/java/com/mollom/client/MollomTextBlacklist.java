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
import com.mollom.client.datastructures.TextBlacklistEntry;

/**
 * MollomTextBlacklist provides an interface for a sites text blacklist. Text
 * entries in this blacklist are blocked, in addition to the standard blacklist
 * which is maintained internally by Mollom.
 * The text entries can be applied to the author field, to links in posts or to
 * everything. Text entries can be matched completly or be contained in larger
 * strings. For each text entry, there is also a reason field which could enable
 * Mollom to learn from custom text blacklists.
 *
 * @author Thomas Meire
 */
public class MollomTextBlacklist extends Mollom {

	public MollomTextBlacklist(String publicKey, String privateKey) {
		super (publicKey, privateKey);
	}

	public MollomTextBlacklist (Protocol protocol, String publicKey, String privateKey) {
		super (protocol, publicKey, privateKey);
	}

	/**
	 * An enum indicating in which context a text entry is applicable
	 */
	public enum Context {

		EVERYTHING, LINKS, AUTHOR
	};

	private String contextToString(Context context) {
		switch (context) {
			case AUTHOR:
				return "author";
			case LINKS:
				return "links";
			case EVERYTHING:
			default:
				return "everything";
		}
	}

	/**
	 * An enum indicating the reason why a text entry is blocked
	 */
	public enum Reason {

		SPAM, PROFANITY, LOW_QUALITY, UNWANTED
	};

	private String reasonToString(Reason reason) {
		switch (reason) {
			case LOW_QUALITY:
				return "low-quality";
			case PROFANITY:
				return "profanity";
			case SPAM:
				return "spam";
			case UNWANTED:
			default:
				return "unwanted";
		}
	}

	/**
	 * An enum indicating how a text entry should be matched
	 */
	public enum Match {
		EXACT, CONTAINS
	}

	private String matchToString (Match match) {
		switch (match) {
			case EXACT:
				return "exact";
			case CONTAINS:
			default:
				return "contains";
		}
	}

	private void doCall(String method, TextBlacklistEntry entry) throws Exception {
		MollomRequest request = createNewRequest(method);

		request.addParameter("text", entry.text);
		request.addParameter("context", contextToString(entry.context));
		request.addParameter("match", matchToString(entry.match));
		request.addParameter("reason", reasonToString(entry.reason));

		invoke(request, Boolean.class);
	}

	/**
	 * Add a text entry to the blacklist for this keypair
	 *
	 * @see http://mollom.com/api/addBlacklistText
	 *
	 * @param TextBlacklistEntry the entry to add to the blacklist
	 * @throws Exception when something goes wrong while contacting Mollom
	 */
	public void add(TextBlacklistEntry TextBlacklistEntry) throws Exception {
		doCall("addBlacklistText", TextBlacklistEntry);
	}

	/**
	 * Remove a text entry from the blacklist for this keypair
	 *
	 * @see http://mollom.com/api/removeBlacklistText
	 *
	 * @param TextBlacklistEntry the entry to remove from the blacklist
	 * @throws Exception when something goes wrong while contacting Mollom
	 */
	public void remove(TextBlacklistEntry TextBlacklistEntry) throws Exception {
		doCall("removeBlacklistText", TextBlacklistEntry);
	}

	/**
	 * List all text entries in the blacklist for this keypair
	 *
	 * @see http://mollom.com/api/listBlacklistText
	 *
	 * @return a list of all text entries
	 * @throws Exception when something goes wrong while contacting Mollom
	 */
	public TextBlacklistEntry[] list() throws Exception {
		return invoke (createNewRequest("listBlacklistText"), TextBlacklistEntry[].class);
	}
}
