/**
 * 
 */
package org.acme;

import java.net.MalformedURLException;

import org.cotrix.gcube.security.DefaultSessionProvider;
import org.cotrix.gcube.stubs.PortalSession;
import org.cotrix.gcube.stubs.SessionToken;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class TestProxy {

	/**
	 * @param args
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) {
		DefaultSessionProvider stub = new DefaultSessionProvider();
		PortalSession userSession = stub.sessionFor(new SessionToken("00A39CA7BD7CB6CC6B6649915763B29D", "/gcube/devesec", ""));
		System.out.println(userSession);

	}

}
