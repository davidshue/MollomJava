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
import com.mollom.client.rest.BlacklistEntryResponse;
import com.mollom.client.rest.BlacklistResponse;
import com.mollom.client.rest.RestResponse;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.util.ArrayList;
import java.util.List;
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

	public MollomBlacklist(String publicKey, String privateKey) {
		super (publicKey, privateKey, false);
	}

	public MollomBlacklist(String publicKey, String privateKey, boolean testing) {
		super (publicKey, privateKey, testing);
	}

  private void add (MultivaluedMap<String, String> params, String name, Context value) {
    if (value != null) {
      params.putSingle(name, value.toString());
    }
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

		add(request, "value",    entry.text);
		add(request, "context", entry.context);
		add(request, "match",   entry.match);
		add(request, "reason",  entry.reason);

    BlacklistEntryResponse response = invoke("POST", "/blacklist/" + publicKey, request, BlacklistEntryResponse.class);
    if (response.getCode() != 200) {
      throw new Exception(response.getMessage());
    }
		return toBlacklistEntry(response.getEntry());
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
    RestResponse response = invoke("POST", "/blacklist/" + publicKey + "/" + entry.id + "/delete", new MultivaluedMapImpl(), RestResponse.class);
    if (response.getCode() != 200) {
      throw new Exception(response.getMessage());
    }
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
		BlacklistResponse response = invoke("GET" ,"/blacklist/" + publicKey, new MultivaluedMapImpl(), BlacklistResponse.class);
    List<BlacklistEntry> entries = new ArrayList<BlacklistEntry>(response.getEntries().size());
    for(com.mollom.client.rest.BlacklistEntry entry : response.getEntries()) {
      entries.add(toBlacklistEntry(entry));
    }
    return entries.toArray(new BlacklistEntry[0]);
  }

  private BlacklistEntry toBlacklistEntry(com.mollom.client.rest.BlacklistEntry result) {
    BlacklistEntry entry = new BlacklistEntry();
    entry.text = result.getValue();
    entry.created = result.getCreated();
    entry.context = toContext(result.getContext());
    entry.match = Match.valueOf(result.getMatch().toUpperCase());
    entry.reason = Reason.valueOf(result.getReason().toUpperCase());
    entry.status = result.getStatus() == 1;
    entry.note = result.getNote();
    entry.id = result.getId();
    return entry;
  }

  private Context toContext(String context) {
    if ("allFields".equals(context)) {
      return Context.ALL_FIELDS;
    } else if ("authorName".equals(context)) {
      return Context.AUTHOR_NAME;
    } else if ("authorId".equals(context)) {
      return Context.AUTHOR_ID;
    } else if ("authorIp".equals(context)) {
      return Context.AUTHOR_IP;
    } else if ("authorMail".equals(context)) {
      return Context.AUTHOR_MAIL;
    } else if ("links".equals(context)) {
      return Context.LINKS;
    } else if ("postTitle".equals(context)) {
      return Context.POST_TITLE;
    } else {
      return null;
    }
  }

}
