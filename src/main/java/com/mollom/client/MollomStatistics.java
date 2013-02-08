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

/**
 * MollomStatistics provides an interface to some usefull statistics. These
 * include the number of accepted/rejected spam messages per day and a list
 * of spam pages on your domain.
 *
 * @see http://mollom.com/api/getStatistics
 * @see http://mollom.com/api/getSpamPages
 *
 * @author Thomas Meire
 */
public class MollomStatistics extends Mollom {

	public MollomStatistics(String publicKey, String privateKey) {
		super (publicKey, privateKey);
	}

	public MollomStatistics(Protocol protocol, String publicKey, String privateKey) {
		super(protocol, publicKey, privateKey);
	}

	private int getStatistics(String type) throws Exception {
		MollomRequest request = createNewRequest("getStatistics");
		request.addParameter("type", type);

		return invoke(request, Integer.class);
	}

	/**
	 * @return the number of days that this site has been protected by Mollom
	 * @throws Exception when something goes wrong while contacting Mollom
	 */
	public int getTotalDays() throws Exception {
		return getStatistics("total_days");
	}

	/**
	 * @return the number of posts that were accepted in total
	 * @throws Exception when something goes wrong while contacting Mollom
	 */
	public int getAcceptedTotal() throws Exception {
		return getStatistics("total_accepted");
	}

	/**
	 * @return the number of posts that were rejected in total
	 * @throws Exception when something goes wrong while contacting Mollom
	 */
	public int getRejectedTotal() throws Exception {
		return getStatistics("total_rejected");
	}

	/**
	 * @return the number of posts that were accepted today
	 * @throws Exception when something goes wrong while contacting Mollom
	 */
	public int getAcceptedToday() throws Exception {
		return getStatistics("today_accepted");
	}

	/**
	 * @return the number of posts that were rejected today
	 * @throws Exception when something goes wrong while contacting Mollom
	 */
	public int getRejectedToday() throws Exception {
		return getStatistics("today_rejected");
	}

	/**
	 * @return the number of posts that were accepted yesterday
	 * @throws Exception when something goes wrong while contacting Mollom
	 */
	public int getAcceptedYesterday() throws Exception {
		return getStatistics("yesterday_accepted");
	}

	/**
	 * @return the number of posts that were rejected yesterday
	 * @throws Exception when something goes wrong while contacting Mollom
	 */
	public int getRejectedYesterday() throws Exception {
		return getStatistics("yesterday_rejected");
	}
}
