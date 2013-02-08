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
package com.mollom.client.core.xmlrpc;

import com.mollom.client.core.MollomCommunicationException;
import com.mollom.client.core.MollomCommunicator;
import com.mollom.client.core.MollomRequest;
import java.net.MalformedURLException;
import redstone.xmlrpc.XmlRpcClient;
import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcFault;

/**
 * An XMLRPC implementation of MollomCommunicator
 *
 * @see MollomCommunicator
 * @author Thomas Meire
 */
public class XMLRPCCommunicator implements MollomCommunicator {

	public <T> T execute(MollomRequest request, Class<T> c) throws MollomCommunicationException {
		// server shouldn't be null, be check to make sure
		if (request.getServer() == null) {
			throw new MollomCommunicationException(1000, "Could not execute request, no server given.");
		}

		Object[] params = new Object[1];
		params[0] = request.getParams();

		Object result = null;
		try {
			XmlRpcClient client = new XmlRpcClient(request.getServer() + "/" + request.getApiVersion(), false);
			result = client.invoke(request.getMethod(), params);
		} catch (MalformedURLException mue) {
			throw new MollomCommunicationException(1000, mue);
		} catch (XmlRpcException xre) {
			throw new MollomCommunicationException(9000, xre);
		} catch (XmlRpcFault xrf) {
			throw new MollomCommunicationException(xrf.errorCode, xrf);
		}
		return new XMLRPCResponse(result).getValue(c);
	}
}
