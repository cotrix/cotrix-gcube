package org.acme;

import static org.cotrix.common.Constants.*;
import static org.cotrix.gcube.extension.PortalRole.*;
import static org.cotrix.gcube.extension.SessionTokenCollector.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Vetoed;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;

import org.cotrix.common.cdi.Current;
import org.cotrix.domain.user.User;
import org.cotrix.gcube.extension.PortalProxy;
import org.cotrix.gcube.extension.PortalProxyProvider;
import org.cotrix.gcube.extension.PortalRole;
import org.cotrix.gcube.stubs.PortalUser;
import org.cotrix.gcube.stubs.SessionToken;
import org.cotrix.repository.UserRepository;
import org.cotrix.security.LoginService;
import org.cotrix.test.ApplicationTest;
import org.junit.Test;

@Priority(TEST)
public class LoginTest extends ApplicationTest {

	@Inject
	@Current
	User currentUser;
	
	@Inject
	LoginService service;
	
	@Inject
	TestPortalProxyProvider provider;
	
	@Inject
	UserRepository repository;
	
	
	@Test
	public void logins() {
	
		PortalUser user = someUserAs(VRE_MANAGER);
		
		provider.proxy.portalUser = user;
		
		User logged = service.login(someRequest());
		
		assertEquals(user.email(),logged.email());
		assertEquals(user.userName(),logged.name());
		assertEquals(user.fullName(),logged.fullName());

		assertTrue(logged.is(VRE_MANAGER.internal));
		
		assertNotNull(repository.lookup(logged.id()));;

		//come back with less role
		
		user = someUserAs();
		
		provider.proxy.portalUser = user;
		
		logged = service.login(someRequest());
		
		System.out.println(logged.directRoles());
		
		assertFalse(logged.is(VRE_MANAGER.internal));
	}
	
	@Test
	public void loginsGetRole() {
	
		PortalUser user = someUserAs();
		
		provider.proxy.portalUser = user;
		
		User logged = service.login(someRequest());
		
		assertEquals(user.email(),logged.email());
		assertEquals(user.userName(),logged.name());
		assertEquals(user.fullName(),logged.fullName());

		assertFalse(logged.is(VRE_MANAGER.internal));
		
		assertNotNull(repository.lookup(logged.id()));;

		//come back with bigger role
		
		user = someUserAs(VRE_MANAGER);
		
		provider.proxy.portalUser = user;
		
		logged = service.login(someRequest());
		
		System.out.println(logged.directRoles());
		
		assertTrue(logged.is(VRE_MANAGER.internal));
	}
	
	
	//helpers
	
	HttpServletRequest someRequest() {
		
		HttpServletRequest req = mock(HttpServletRequest.class);
		
		when(req.getAttribute(URL_TOKEN_ATTRIBUTE_NAME)).thenReturn(someToken().encoded());
		
		return req;
	}
	SessionToken someToken() {
		return new SessionToken("id", "some/scope", "u");
	}
	
	
	PortalUser someUserAs(PortalRole ... roles) {
		List<String> values = new ArrayList<>();
		for (PortalRole role : roles)
			values.add(role.value);
		
		return new PortalUser("user", "some one", "some.one@me.com",values);
	}
	
	static class TestPortalProxy implements PortalProxy {
		
		PortalUser portalUser;

		@Override
		public PortalUser getPortalUser() {
			return portalUser;
		}

		@Override
		public void publish(String news) {
		}
	}
	
	@Vetoed
	static class TestPortalProxyProvider implements PortalProxyProvider {
		
		TestPortalProxy proxy = new TestPortalProxy();

		@Override
		public TestPortalProxy getPortalProxy(SessionToken sessionToken) {
			return proxy;
		}
	}
	
	@Produces @Singleton @Alternative
	static TestPortalProxyProvider provider() {
		return new TestPortalProxyProvider();
	}
}
