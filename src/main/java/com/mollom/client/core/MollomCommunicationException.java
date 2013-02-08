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

/**
 * Exception class to hold the Mollom error code.
 * Known error codes:
 *	<dl>
 *		<dt>1000</dt><dd>Internal error</dd>
 *		<dt>1100</dt><dd>Refresh serverlist</dd>
 *		<dt>1200</dt><dd>Server busy</dd>
 *	</dl>
 *
 * @see http://mollom.com/api/error-handling
 * @author Thomas Meire
 */
public class MollomCommunicationException extends Exception {

	/** The error code */
	public int code;

	/**
	 * Create a new MollomCommunicationException
	 *
	 * @param code the code from the exception
	 * @param parent the exception that triggered this exception
	 */
	public MollomCommunicationException (int code, Exception parent) {
		super(parent);
		this.code = code;
	}

	/**
	 * Create a new MollomCommunicationException
	 *
	 * @param code the code from the exception
	 * @param parent the exception that triggered this exception
	 */
	public MollomCommunicationException (int code, String message) {
		super(message);
		this.code = code;
	}
}
