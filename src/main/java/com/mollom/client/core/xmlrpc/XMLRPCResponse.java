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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import redstone.xmlrpc.XmlRpcArray;
import redstone.xmlrpc.XmlRpcStruct;

/**
 * An xmlrpc implementation of MollomResponse.
 *
 * NOTE: Besides the use of XmlRpcStruct and XmlRpcArray, this class is very
 * generic. Perhaps it can serve as MollomResponse if a rest implementation
 * would be similar.
 * 
 * @author Thomas Meire
 */
public class XMLRPCResponse {

	private Object response;

	XMLRPCResponse(Object response) {
		this.response = response;
	}

	/**
	 * Fill out the public fields of an T instance by using objects from the
	 * XmlRpcStruct structure.
	 * 
	 * @param struct the XmlRpcStruct from the response
	 * @param c the Class object of type T
	 * @return a new T instance
	 */
	private <T> T parseStructValue (XmlRpcStruct struct, Class<T> c) {
		T result = null;
		try {
			result = c.newInstance();
		} catch (Exception e) {
			throw new RuntimeException ("Bad data class provided.", e);
		}

		for (Field f : c.getFields()) {
			if ((f.getModifiers() & Modifier.PUBLIC) == Modifier.PUBLIC) {
				Object o = struct.get(f.getName());
				/* skip fields that are not in the response. */
				if (o == null) {
					continue;
				}

				if (o.getClass().equals(f.getType()) || f.getType().isPrimitive()) {
					// FIXME: not really typesafe for primitive fields
					try {
						f.set(result, o);
					} catch (IllegalAccessException iae) {
						// ignore it, we're only accessing public fields
					}
				} else if (f.getType().isEnum()) {
					/* Not the cleanest way to handle things, but it works */
					String representation = o.toString().replace('-', '_').toUpperCase();
					for (Object e : f.getType().getEnumConstants()) {
						if (e.toString().equals(representation)) {
							try {
								f.set(result, e);
								break;
							} catch (IllegalAccessException iae) {}
						}
					}
					// we have an enum, what to do now?
				} else {
					Object x = getValue (o, f.getType());
					try {
						f.set(result, x);
					} catch (IllegalAccessException iae) {
						// ignore it, we're only accessing public fields
					}
				}
			}
		}
		return result;
	}

	/**
	 * Convert an XmlRpcArray to an array of instances of T.
	 * 
	 * @param array the XmlRpcArray from the response
	 * @param c the class for the objects
	 * @return a new array with instances of T
	 */
	private <T> T[] parseArrayValue (XmlRpcArray array, Class<T> c) {
		T[] result = (T[]) Array.newInstance(c, array.size());
		for (int i = 0; i < array.size(); i++) {
			result[i] = getValue(array.get(i), c);
		}
		return result;
	}

	/**
	 * Convert an object to an instance of T.
	 *
	 * Three possible cases:
	 *	- we can return the base object with just a cast
	 *	- we need to return an array of objects
	 *  - we need to return on object created out of a struct
	 *
	 * @param src the object that needs to be converted
	 * @param c the class for the target type T
	 * @return a new instance of T, containing the information of src
	 */
	private <T> T getValue(Object src, Class<T> c) {
		if (src == null) {
			return null;
		}

		if (src.getClass().equals(c)) { // FIXME: need instanceof equiv
			return c.cast(src);
		} else if (c.isArray()) {
			if (src instanceof XmlRpcArray) {
				return (T) parseArrayValue((XmlRpcArray) src, c.getComponentType());
			} else {
				// something's wrong, no such field
				throw new NoSuchFieldError("There was no " + c.getName() + " instance in the response from Mollom.");
			}
		} else {
			if (src instanceof XmlRpcStruct) {
				return parseStructValue((XmlRpcStruct) src, c);
			} else {
				// something's wrong, no such field
				throw new NoSuchFieldError("There was no " + c.getName() + " instance in the response from Mollom.");
			}
		}
	}

	/**
	 * @see MollomResponse#getValue(java.lang.Class)
	 */
	public <T> T getValue(Class<T> c) {
		return getValue(response, c);
	}
}
