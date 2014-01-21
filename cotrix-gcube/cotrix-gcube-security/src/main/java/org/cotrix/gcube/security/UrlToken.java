package org.cotrix.gcube.security;

import org.cotrix.security.Token;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UrlToken implements Token {

	private String token;

	/**
	 * @param token
	 */
	public UrlToken(String token) {
		this.token = token;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UrlToken [token=");
		builder.append(token);
		builder.append("]");
		return builder.toString();
	}
}
