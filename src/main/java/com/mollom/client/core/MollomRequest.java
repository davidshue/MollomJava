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
package com.mollom.client.core;

import java.util.HashMap;
import java.util.Map;

/**
 * A class containing information about a request.
 * It contains following information:
 *	- the server
 *  - the method
 *  - the method parameters
 *
 * @author Thomas Meire
 */
public class MollomRequest {

	private String server;
  private String version;
	private String method;
	private Map<String, String> params;

	public MollomRequest(String method) {
		this.method = method;
		this.params = new HashMap<String, String>();

		server = null;
	}

	/**
	 * Add a String parameter to the request
	 *
	 * @param key the name of the parameter
	 * @param value the value of the parameter
	 */
	public void addParameter(String key, String value) {
		if (key != null && value != null) {
			params.put(key, value);
		}
	}

	/**
	 * Add a String parameter to the request
	 *
	 * @param key the name of the parameter
	 * @param value the value of the parameter
	 */
	public <T extends Enum> void addParameter(String key, T value) {
		if (key != null && value != null) {
			params.put(key, value.toString().toLowerCase());
		}
	}


	/**
	 * Add a boolean parameter to the request
	 *
	 * @param key the name of the parameter
	 * @param value the value of the parameter
	 */
	public void addParameter(String key, boolean value) {
		addParameter(key, value ? "1" : "0");
	}

	public Map<String, String> getParams() {
		return params;
	}

	/**
	 * Set the server that needs to be used for this request
	 *
	 * @param server the server that needs to be used
	 */
	public void setServer(String server) {
		this.server = server;
	}

	/**
	 * @return the server that needs to be used for this request
	 */
	public String getServer () {
		return server;
	}

	/**
	 * @return the method that needs to be called
	 */
	public String getMethod () {
		return method;
	}

  public void setApiVersion(String version) {
    this.version = version;
  }

  public String getApiVersion() {
    return version;
  }
}
