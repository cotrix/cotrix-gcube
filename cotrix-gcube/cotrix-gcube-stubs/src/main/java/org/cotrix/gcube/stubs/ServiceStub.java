/**
 * 
 */
package org.cotrix.gcube.stubs;

import static org.gcube.resources.discovery.icclient.ICFactory.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.cotrix.gcube.stubs.TokenEncoder.Token;
import org.gcube.common.resources.gcore.ServiceEndpoint;
import org.gcube.common.resources.gcore.ServiceEndpoint.AccessPoint;
import org.gcube.common.scope.api.ScopeProvider;
import org.gcube.common.scope.impl.ScopeBean;
import org.gcube.resources.discovery.client.api.DiscoveryClient;
import org.gcube.resources.discovery.client.queries.api.SimpleQuery;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ServiceStub {

	private static final String PORTAL_ENDPOINT_CATEGORY = "Portal";
	private static final String ACCESS_POINT_NAME = "Base URI";
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
	private static final String SERVICE_PATH = "/cotrix-gcube-portlet/service";

	private final GCubeSessionMarshaller marshaller;
	private Logger logger = LoggerFactory.getLogger(ServiceStub.class);

	public ServiceStub() {
		marshaller = new GCubeSessionMarshaller();
	}

	public GCubeSession getSession(Token token) {
		try {
			String portalUrl = retrievePortalUrl(token.getScope(), token.getPortalUrl());
			logger.trace("portalUrl: {}", portalUrl);
			URL serviceUrl = new URL(portalUrl + SERVICE_PATH);
			logger.trace("serviceUrl: {}", serviceUrl);
			String response = makeRequest(serviceUrl, token.getSessionId());
			return marshaller.deserialize(response);
		} catch(Exception e) {
			throw new RuntimeException("Failed retrieving user session", e);
		}
	}

	private String retrievePortalUrl(String scope, String portalUrl) throws MalformedURLException {
		logger.trace("retrievePortalUrl scope {}, portalUrl: {}", scope, portalUrl);
		
		String infrastructureScope = getInfrastructureScope(new ScopeBean(scope));
		ScopeProvider.instance.set(infrastructureScope);
		logger.trace("infrastructureScope {}", infrastructureScope);
		
		URL url = new URL(portalUrl);
		StringBuilder exptectedUrlBuilder = new StringBuilder(url.getHost());
		if (url.getPort()!=-1) exptectedUrlBuilder.append(':').append(url.getPort());
		String expectedUrl = exptectedUrlBuilder.toString();
		logger.trace("expectedUrl {}", expectedUrl);

		SimpleQuery query = queryFor(ServiceEndpoint.class);

		query.addCondition(String.format("$resource/Profile/Category/text() eq '%1$s'",PORTAL_ENDPOINT_CATEGORY));

		DiscoveryClient<ServiceEndpoint> client = clientFor(ServiceEndpoint.class);

		List<ServiceEndpoint> resources = client.submit(query);

		if (resources.isEmpty()) throw new IllegalStateException("Portal resource not found");

		for (ServiceEndpoint endpoint:resources) {

			for (AccessPoint accessPoint:endpoint.profile().accessPoints()) {
				if (accessPoint.name().equals(ACCESS_POINT_NAME) && expectedUrl.equals(accessPoint.address())) return portalUrl;
			}
		}

		throw new IllegalStateException("Portal Service Endpoint for url "+expectedUrl+" not found in scope "+infrastructureScope);
	}

	private String getInfrastructureScope(ScopeBean scope)
	{
		ScopeBean enclosing = scope.enclosingScope();
		if (enclosing == null) return scope.toString();
		return getInfrastructureScope(enclosing);
	}

	private String makeRequest(URL url, String sessionId) throws IOException
	{
		try {
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
		} catch(Exception e) {
			throw new RuntimeException("Request to service in "+url+" with session "+sessionId+" failed", e);
		}
	}

	/**
	 * Copy the input reader into the output writer.
	 * @param input the input reader.
	 * @param output the output writer.
	 * @return the number of char copied.
	 * @throws IOException if an error occurs during the copy.
	 */
	private static long copy(Reader input, Writer output) throws IOException {
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
