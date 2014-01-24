package org.acme;

import static org.cotrix.common.Constants.*;
import static org.cotrix.gcube.security.PortalRole.*;
import static org.cotrix.gcube.security.SessionTokenCollector.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Vetoed;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;

import org.cotrix.common.cdi.Current;
import org.cotrix.domain.user.User;
import org.cotrix.gcube.security.PortalRole;
import org.cotrix.gcube.security.SessionProvider;
import org.cotrix.gcube.stubs.PortalSession;
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
	TestSessionProvider provider;
	
	@Inject
	UserRepository repository;
	
	
	@Test
	public void login() {
	
		PortalUser user = someUserAs(VRE_MANAGER);
		
		provider.session = sessionFor(user);
		
		User logged = service.login(someRequest());
		
		assertEquals(user.email(),logged.email());
		assertEquals(user.userName(),logged.name());
		assertEquals(user.fullName(),logged.fullName());

		assertTrue(logged.is(VRE_MANAGER.internal));
		
		assertNotNull(repository.lookup(logged.id()));;
		
		
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
		
		return new PortalUser(UUID.randomUUID().toString(), "some one", "some.one@me.com",values);
	}
	
	PortalSession sessionFor(PortalUser user) {
		
		return new PortalSession(user, "some/scope");
	}
	
	
	@Vetoed
	static class TestSessionProvider implements SessionProvider {
		
		PortalSession session;
		
		@Override
		public PortalSession sessionFor(SessionToken token) {
			return session;
		}
	}
	
	@Produces @Singleton @Alternative
	static TestSessionProvider provider() {
		return new TestSessionProvider();
	}
}
