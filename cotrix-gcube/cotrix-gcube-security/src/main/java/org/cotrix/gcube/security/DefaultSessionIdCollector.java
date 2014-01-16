package org.cotrix.gcube.security;

import javax.servlet.http.HttpServletRequest;

import org.cotrix.security.Token;
import org.cotrix.security.TokenCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DefaultSessionIdCollector implements TokenCollector {

	public static final String SESSION_ID_ATTRIBUTE_NAME = "GCUBE_SESSION_ID";
	private Logger logger = LoggerFactory.getLogger(DefaultSessionIdCollector.class);
	
	@Override
	public Token token(HttpServletRequest request) {
		
		logger.trace("token request: {}", request);
		
		String sessionId = (String) request.getAttribute(SESSION_ID_ATTRIBUTE_NAME);
		logger.trace("sessionId: {}", sessionId);
		
		return sessionId!=null ? new SessionId(sessionId):null;
	}
}
