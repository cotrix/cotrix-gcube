/**
 * 
 */
package org.cotrix.gcube.stubs;

import java.net.MalformedURLException;

import org.cotrix.gcube.stubs.ServiceStub;
import org.cotrix.gcube.stubs.GCubeSession;

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
		ServiceStub stub = new ServiceStub("http://localhost:8080/");
		GCubeSession userSession = stub.getSession("00A39CA7BD7CB6CC6B6649915763B29D");
		System.out.println(userSession);

	}

}
