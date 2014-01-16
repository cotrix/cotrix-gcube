package org.cotrix.gcube.security;

import org.cotrix.security.Token;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SessionId implements Token {

	private String sessionId;

	/**
	 * @param sessionId
	 */
	public SessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SessionId [sessionId=");
		builder.append(sessionId);
		builder.append("]");
		return builder.toString();
	}
}
