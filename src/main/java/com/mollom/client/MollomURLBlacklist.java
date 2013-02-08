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
import com.mollom.client.datastructures.URLBlacklistEntry;

/**
 * MollomURLBlacklist provides an interface for a sites URL blacklist. URLs in
 * this blacklist are blocked, in addition to the standard blacklist which is
 * maintained internally by Mollom.
 * 
 * @author Thomas Meire
 */
@Deprecated
public class MollomURLBlacklist extends Mollom {

	public MollomURLBlacklist(String publicKey, String privateKey) {
		super (publicKey, privateKey);
	}

	public MollomURLBlacklist(Protocol protocol, String publicKey, String privateKey) {
		super(protocol, publicKey, privateKey);
	}

	private void doCall(String method, String url) throws Exception {
		MollomRequest request = createNewRequest(method);
		request.addParameter("url", url);

		invoke(request, Boolean.class);
	}

	/**
	 * Add an URL to the blacklist of this site.
	 *
	 * @see http://mollom.com/api/addBlacklistURL
	 *
	 * @param url the url to add to the blacklist
	 * @throws Exception when something goes wrong while contacting Mollom
	 */
	public void add(String url) throws Exception {
		doCall("addBlacklistURL", url);
	}

	/**
	 * Remove an URL from the blacklist of this site.
	 *
	 * @see http://mollom.com/api/removeBlacklistURL
	 *
	 * @param url the url to remove from the blacklist
	 * @throws Exception when something goes wrong while contacting Mollom
	 */
	public void remove(String url) throws Exception {
		doCall("removeBlacklistURL", url);
	}

	/**
	 * List all url entries in the blacklist of this site.
	 *
	 * @see http://mollom.com/api/listBlacklistURL
	 *
	 * @return A list of couples (url, created)
	 * @throws Exception when something goes wrong while contacting Mollom
	 */
	public URLBlacklistEntry[] list() throws Exception {
		return invoke(createNewRequest("listBlacklistURL"), URLBlacklistEntry[].class);
	}
}
