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

import com.mollom.client.core.MollomRequest;
import com.mollom.client.datastructures.KeySet;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * MollomReseller provides an interface to manage the information about sites of
 * your clients. The functions in this class are only available to Mollom resellers.
 * Normal users will receive an error when trying to use these functions
 * For more information on Mollom resellers and how to become one, see the FAQ.
 *
 * @see <a href="http://mollom.com/faq/reselling-mollom-services">FAQ: Reselling Mollom Services</a>
 * @author Thomas Meire
 */
public class MollomReseller extends Mollom {

	public MollomReseller(String publicKey, String privateKey) {
		super (publicKey, privateKey);
	}

	/**
	 * Get a list of all sites which are managed by this key pair. The sites are
	 * identified by their public key.
	 * 
	 * @see http://mollom.com/api/listSites
	 *
	 * @return a list of public keys
	 * @throws Exception when something goes wrong while contacting Mollom
	 *
	public String[] list() throws Exception {
		return invoke(createNewRequest("listSites"), String[].class);
	}*/

	private void infoToRequest(MollomRequest request, SiteInfo info) throws Exception {
		String type = info.type;
		if (!type.equals("customer") && !type.equals("company") && !type.equals("personal") && !type.equals("non-profit")) {
			throw new Exception("Site type must be one of 'personal', 'company', 'non-profit' or 'customer'");
		}

		request.addParameter("client_key", info.pubkey);
		request.addParameter("url", info.url);
		request.addParameter("mail", info.mail);
		request.addParameter("status", info.status);
		request.addParameter("testing", info.testing);
		request.addParameter("language", info.language);
		request.addParameter("type", info.type);
	}

	/**
	 * Create a new site. All information to create the new site is encapsulated
	 * in the SiteInfo object. When the site is created, the public and private
	 * key for the new site will be added to the SiteInfo object.
	 * 
	 * @see http://mollom.com/api/createSite
	 *
	 * @param info an object with all information about the site
	 * @return a SiteInfo object containing the keypair along with the other info.
	 * @throws Exception when something goes wrong while contacting Mollom
	 *
	public SiteInfo create(SiteInfo info) throws Exception {
		MollomRequest request = createNewRequest("createSite");
		infoToRequest(request, info);

		info.setKeys(invoke(request, KeySet.class));
		return info;
	}*/

	/**
	 * Get all information for a site managed by this keypair. The site is
	 * identified by its public key.
	 *
	 * @see http://mollom.com/api/getSite
	 *
	 * @param pubkey the public key for the site
	 * @return a SiteInfo object with all config options for the site
	 * @throws Exception when something goes wrong while contacting Mollom
	 *
	public SiteInfo get(String pubkey) throws Exception {
		MollomRequest request = createNewRequest("getSite");

		request.addParameter("client_key", pubkey);

		return invoke(request, SiteInfo.class);
	}*/

	/**
	 * Update the information about a site managed by this keypair. To be able
	 * to update the information, the SiteInfo object must at least contain the
	 * public key. It is recommended to reuse a SiteInfo object that you
	 * received from the get function.
	 *
	 * <pre>
	 * {@code
	 * SiteInfo info = reseller.get (myPubKey);
	 * info.mail = "mymail@example.com";
	 * reseller.update(info.mail);
	 * }
	 * </pre>
	 *
	 * @see #get
	 * @see http://mollom.com/api/updateSite
	 *
	 * @param info An object containing the updated information.
	 * @return true if the update succeeded, else false
	 * @throws Exception when something goes wrong while contacting Mollom
	 *
	public boolean update(SiteInfo info) throws Exception {
		MollomRequest request = createNewRequest("updateSite");
		infoToRequest(request, info);

		return invoke(request, Boolean.class);
	}*/

	/**
	 * Delete the site with the specified public key. Be sure about what you're
	 * doing with this function, this can't be undone.
	 *
	 * @see http://mollom.com/api/deleteSite
	 *
	 * @param pubkey the public key for the site to delete
	 * @return true if the deletion succeeded, else false.
	 * @throws Exception when something goes wrong while contacting Mollom
	 *
	public boolean delete(String pubkey) throws Exception {
		MollomRequest request = createNewRequest("deleteSite");
		request.addParameter("client_key", pubkey);

		return invoke(request, Boolean.class);
	}*/

	/**
	 * A container class that holds all information about a site. It's used to
	 * both give information to and get information from the user.
	 *
	 * @see #create
	 * @see #update
	 * @see #get
	 */
	@XmlRootElement
  public static class SiteInfo {

		/** The public key for this site */
		private String pubkey;
		/** The private key for this site */
		private String privkey;
		/** The url of this site */
		public String url;
		/** The contact email for this site */
		public String mail;
		/** The status for this site (enabled or not?)*/
		public boolean status;
		/**
		 * Is this site in testing mode?
		 * See <a href="http://mollom.com/api/testing-mollom">Testing Mollom</a> for more information.
		 */
		public boolean testing;
		/** The type of the site: one of 'personal', 'company', 'customer', 'non-profit' */
		public String type;
		/** The language of the site in iso639-1 format */
		public String language;

		public SiteInfo() {
		}

		public SiteInfo(String url, String mail, boolean status, boolean testing) {
			this(url, mail, status, testing, null, null);
		}

		public SiteInfo(String pubkey, String url, String mail, boolean status, boolean testing) {
			this(url, mail, status, testing, null, null);
			this.pubkey = pubkey;
		}

		public SiteInfo(String url, String mail, boolean status, boolean testing, String type, String language) {
			this.url = url;
			this.mail = mail;
			this.status = status;
			this.testing = testing;
			this.type = type;
			this.language = language;
		}

		private void setKeys(KeySet keys) {
			this.pubkey = keys.public_key;
			this.privkey = keys.private_key;
		}

		public String getPrivkey() {
			return privkey;
		}

		public String getPubkey() {
			return pubkey;
		}
	}
}
