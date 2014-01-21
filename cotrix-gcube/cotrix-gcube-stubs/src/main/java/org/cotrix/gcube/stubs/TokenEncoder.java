/**
 * 
 */
package org.cotrix.gcube.stubs;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.google.gson.Gson;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class TokenEncoder {

	private static final String ENCODING = "UTF-8";
	private static final Gson GSON = new Gson();

	public static String encode(String sessionId, String scope, String portalUrl) {
		return encode(new Token(sessionId, scope, portalUrl));
	}

	public static String encode(Token token) {
		String json = GSON.toJson(token);
		System.out.println(json);
		try {
			return URLEncoder.encode(json, ENCODING);
		} catch(UnsupportedEncodingException e) {
			throw new RuntimeException("Encoding failed", e);
		}
	}

	public static Token decode(String token) {
		try {
			String json = URLDecoder.decode(token, ENCODING);
			return GSON.fromJson(json, Token.class);
		} catch(UnsupportedEncodingException e) {
			throw new RuntimeException("Dencoding failed", e);
		}
	}

	public static class Token {
		private String sessionId;
		private String scope;
		private String portalUrl;

		public Token(String sessionId, String scope, String portalUrl) {
			this.sessionId = sessionId;
			this.scope = scope;
			this.portalUrl = portalUrl;
		}

		/**
		 * @return the sessionId
		 */
		public String getSessionId() {
			return sessionId;
		}

		/**
		 * @return the scope
		 */
		public String getScope() {
			return scope;
		}

		/**
		 * @return the portalUrl
		 */
		public String getPortalUrl() {
			return portalUrl;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Token [sessionId=");
			builder.append(sessionId);
			builder.append(", scope=");
			builder.append(scope);
			builder.append(", portalUrl=");
			builder.append(portalUrl);
			builder.append("]");
			return builder.toString();
		}
	}
}
