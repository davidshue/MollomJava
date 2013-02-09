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

import com.mollom.client.datastructures.BlacklistEntry;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.MultivaluedMap;

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
public class MollomBlacklist extends Mollom {

	/**
	 * An enum indicating the reason why a text entry is blocked
	 */
	public static enum Reason {

		SPAM, PROFANITY, QUALITY, UNWANTED
	};

	/**
	 * An enum indicating how a text entry should be matched
	 */
	public static enum Match {
		EXACT, CONTAINS
	}

	/**
	 * An enum indicating in which context a text entry is applicable
	 */
	public static enum Context {

		ALL_FIELDS("allFields"), LINKS("links"),
    AUTHOR_NAME("authorName"),
    AUTHOR_MAIL("authorMail"),
    AUTHOR_ID("authorId"),
    AUTHOR_IP("authorIp"),
    POST_TITLE("postTitle");

    private String value;

    Context (String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return value;
    }
  };

  private void add (MultivaluedMap<String, String> params, String name, Context value) {
    if (value != null) {
      params.putSingle(name, value.toString());
    }
  }

	public MollomBlacklist(String publicKey, String privateKey) {
		super (publicKey, privateKey);
	}

	/**
	 * Add a text entry to the blacklist for this keypair
	 *
	 * @see http://mollom.com/api/addBlacklistText
	 *
	 * @param entry the entry to add to the blacklist
	 * @throws Exception when something goes wrong while contacting Mollom
	 */
	public BlacklistEntry add(BlacklistEntry entry) throws Exception {
    MultivaluedMap<String, String> request = new MultivaluedMapImpl();

		add(request, "text",    entry.text);
		add(request, "context", entry.context);
		add(request, "match",   entry.match);
		add(request, "reason",  entry.reason);

		return invoke("POST", "/blacklist/" + publicKey, request, BlacklistEntry.class);
	}

	/**
	 * Remove a text entry from the blacklist for this keypair
	 *
	 * @see http://mollom.com/api/removeBlacklistText
	 *
	 * @param entry the entry to remove from the blacklist
	 * @throws Exception when something goes wrong while contacting Mollom
	 */
	public void remove(BlacklistEntry entry) throws Exception {
    invoke("POST", "/blacklist/" + publicKey + "/" + entry.id + "/delete", new MultivaluedMapImpl(), null);
	}

	/**
	 * List all text entries in the blacklist for this keypair
	 *
	 * @see http://mollom.com/api/listBlacklistText
	 *
	 * @return a list of all text entries
	 * @throws Exception when something goes wrong while contacting Mollom
	 */
	public BlacklistEntry[] list() throws Exception {
		return invoke("GET" ,"/blacklist/" + publicKey, new MultivaluedMapImpl(), BlacklistEntry[].class);
	}
}
