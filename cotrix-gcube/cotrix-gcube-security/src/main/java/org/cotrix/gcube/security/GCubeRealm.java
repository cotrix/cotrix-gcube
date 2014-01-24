/**
 * 
 */
package org.cotrix.gcube.security;

import static org.cotrix.common.Constants.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.Users.*;
import static org.cotrix.repository.UserQueries.*;

import java.util.Collection;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
import org.cotrix.gcube.stubs.PortalSession;
import org.cotrix.gcube.stubs.PortalUser;
import org.cotrix.gcube.stubs.SessionToken;
import org.cotrix.repository.UserRepository;
import org.cotrix.security.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Alternative @Priority(RUNTIME)
public class GCubeRealm implements Realm {
	
	private Logger logger = LoggerFactory.getLogger(GCubeRealm.class);
	
	@Inject
	private SessionProvider provider;
	
	@Inject
	private UserRepository userRepository;
	
	@Inject
	private RoleMapper roleMapper;

	@Override
	public boolean supports(Object token) {
		
		return token instanceof SessionToken;
	
	}

	@Override
	public String login(Object token) {
		
		SessionToken stoken = reveal(token, SessionToken.class);
		
		PortalSession session = provider.sessionFor(stoken);
		
		PortalUser remoteUser = session.user();
		
		logger.trace("gcube user: {}", remoteUser);
		
		User user = userRepository.get(userByName(remoteUser.userName()));
		
		logger.trace("repository user: {}", user);
		
		if (user == null) 
			createUser(remoteUser);
		else 
			updateUser(remoteUser, user);
		
		return remoteUser.userName();
	}

	protected void createUser(PortalUser gCubeUser) {
		
		logger.trace("creating user from gcube user: {}", gCubeUser);
		
		Collection<Role> roles = roleMapper.map(gCubeUser.roles());
		
		User user = user().name(gCubeUser.userName())
						  .email(gCubeUser.email())
						  .fullName(gCubeUser.fullName())
						  .is(roles).build();
		
		userRepository.add(user);
		
		logger.trace("user added to repository");
	}
	
	protected void updateUser(PortalUser gCubeUser, User user) {
		
	}

	@Override
	public void signup(String name, String pwd) {
		throw new UnsupportedOperationException("Registration to this Realm shoul be made through iMarine portal");
	}

}
