/**
 * 
 */
package org.cotrix.gcube.portlet;

import javax.servlet.http.HttpSession;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CotrixUrlProvider {
	
	private static String COTRIX_URL = "http://localhost:8081/cotrix-web-0.0.1-SNAPSHOT/CodeListManager.html";
	
	private static final String SESSIONID_PARAMETER_NAME = "sessionId";
	private static final String SCOPE_PARAMETER_NAME = "scope";
	
	public static String getCotrixUrl(HttpSession session) {
		return COTRIX_URL+getParameters(session);
	}
	
	protected static String getParameters(HttpSession session) {
		StringBuilder parameters = new StringBuilder("?");
		
		parameters.append(SESSIONID_PARAMETER_NAME).append('=').append(session.getId());
		parameters.append('&').append(SCOPE_PARAMETER_NAME).append('=').append("");
		
		return parameters.toString();
	}

}
