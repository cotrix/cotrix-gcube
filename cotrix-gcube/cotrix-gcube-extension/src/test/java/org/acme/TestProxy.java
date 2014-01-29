/**
 * 
 */
package org.acme;

import java.net.MalformedURLException;

import org.cotrix.gcube.extension.DefaultPortalProxyProvider;
import org.cotrix.gcube.extension.PortalProxy;
import org.cotrix.gcube.stubs.PortalUser;
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
		DefaultPortalProxyProvider portalProxyProvider = new DefaultPortalProxyProvider();
		PortalProxy proxy = portalProxyProvider.getPortalProxy(new SessionToken("FDF298FEA23D9A203AE34434B41CE0C8", "/gcube/devNext/NextNext", "http://localhost:8080"));
		PortalUser user = proxy.getPortalUser();
		System.out.println(user);
		
		proxy.publish("This is a test");

	}

}
