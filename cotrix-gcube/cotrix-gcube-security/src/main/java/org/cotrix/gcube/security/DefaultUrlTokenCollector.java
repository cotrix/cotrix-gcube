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
public class DefaultUrlTokenCollector implements TokenCollector {

	public static final String URL_TOKEN_ATTRIBUTE_NAME = "TOKEN";
	private Logger logger = LoggerFactory.getLogger(DefaultUrlTokenCollector.class);
	
	@Override
	public Token token(HttpServletRequest request) {
		
		logger.trace("urltoken request: {}", request);
		
		String urltoken = (String) request.getAttribute(URL_TOKEN_ATTRIBUTE_NAME);
		logger.trace("urltoken: {}", urltoken);
		
		return urltoken!=null ? new UrlToken(urltoken):null;
	}
}
