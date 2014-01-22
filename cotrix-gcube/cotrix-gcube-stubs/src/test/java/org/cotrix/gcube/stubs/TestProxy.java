/**
 * 
 */
package org.cotrix.gcube.stubs;

import java.net.MalformedURLException;

import org.cotrix.gcube.stubs.ServiceStub;
import org.cotrix.gcube.stubs.GCubeSession;
import org.cotrix.gcube.stubs.TokenEncoder.Token;

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
		ServiceStub stub = new ServiceStub();
		GCubeSession userSession = stub.getSession(new Token("CAD90443A31AD9E98AF1BEBB35536B1E","/gcube/devsec/devVRE","https://dev.d4science.org:443"));
		System.out.println(userSession);

	}

}
