/**
 * 
 */
package org.cotrix.gcube.stubs;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ServiceStub {

	protected static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
	protected static final String SERVICE_PATH = "cotrix-gcube-portlet/service";

	protected final URL serviceUrl;
	protected final GCubeSessionMarshaller marshaller;


	public ServiceStub(String portalUrl) {
		try {
			serviceUrl = new URL(portalUrl + SERVICE_PATH);
		} catch(MalformedURLException e) {
			throw new RuntimeException("service url creation failed", e);
		}
		
		marshaller = new GCubeSessionMarshaller();
	}

	public GCubeSession getSession(String sessionId) {
		try {
			String response = makeRequest(serviceUrl, sessionId);
			System.out.println("response: "+response);
			return marshaller.deserialize(response);
		} catch(Exception e) {
			throw new RuntimeException("Failed retrieving user session from url "+serviceUrl, e);
		}
	}

	protected String makeRequest(URL url, String sessionId) throws IOException
	{
		StringBuilder cookie = new StringBuilder();
		cookie.append("JSESSIONID=").append(sessionId);
		
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestProperty("Cookie", cookie.toString());
		InputStream is = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(is);
		StringWriter writer = new StringWriter();
		copy(reader, writer);
		reader.close();
		is.close();

		return writer.toString();
	}

	/**
	 * Copy the input reader into the output writer.
	 * @param input the input reader.
	 * @param output the output writer.
	 * @return the number of char copied.
	 * @throws IOException if an error occurs during the copy.
	 */
	protected static long copy(Reader input, Writer output) throws IOException {
		char[] buffer = new char[DEFAULT_BUFFER_SIZE];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

}
