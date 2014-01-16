/**
 * 
 */
package org.cotrix.gcube.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
import org.cotrix.gcube.stubs.GCubeUser;
import org.cotrix.gcube.stubs.ServiceStub;
import org.cotrix.gcube.stubs.GCubeSession;
import org.cotrix.repository.UserRepository;
import org.cotrix.security.Realm;
import org.cotrix.security.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.cotrix.repository.UserQueries.*;
import static org.cotrix.common.Constants.*;
import static org.cotrix.domain.dsl.Users.*;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Alternative @Priority(RUNTIME)
public class GCubeRealm implements Realm<SessionId> {
	
	private Logger logger = LoggerFactory.getLogger(GCubeRealm.class);
	
	private ServiceStub proxyStub = new ServiceStub("http://localhost:8080/");
	
	@Inject
	private UserRepository userRepository;

	@Override
	public boolean supports(Token token) {
		return token instanceof SessionId;
	}

	@Override
	public String login(SessionId token) {
		logger.trace("login token: {}", token);
		
		String sessionId = token.getSessionId();
		GCubeSession session = proxyStub.getSession(sessionId);
		GCubeUser gCubeUser = session.getUser();
		logger.trace("gcube user: {}", gCubeUser);
		
		User user = userRepository.get(userByName(gCubeUser.getUsername()));
		logger.trace("repository user: {}", user);
		
		if (user == null) createUser(gCubeUser);
		else updateUser(gCubeUser, user);
		
		return gCubeUser.getUsername();
	}

	protected void createUser(GCubeUser gCubeUser) {
		logger.trace("creating user from gcube user: {}", gCubeUser);
		Collection<Role> roles = mapRoles(gCubeUser);
		User user = user().name(gCubeUser.getUsername()).email(gCubeUser.getEmail()).fullName(gCubeUser.getFullname()).is(roles).build();
		userRepository.add(user);
		logger.trace("user added to repository");
	}
	
	protected void updateUser(GCubeUser gCubeUser, User user) {
		
	}
	
	protected Collection<Role> mapRoles(GCubeUser gCubeUser) {
		//TODO complete
		List<Role> roles = Collections.singletonList(GCubeRoles.USER);
		return roles;
	}

	@Override
	public void signup(String name, String pwd) {
		throw new UnsupportedOperationException("Registration to this Realm shoul be made through iMarine portal");
	}

}
