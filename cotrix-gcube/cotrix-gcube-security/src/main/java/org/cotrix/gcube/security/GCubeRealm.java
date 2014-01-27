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
@Alternative
@Priority(RUNTIME)
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

		PortalUser external = session.user();

		User internal = userRepository.get(userByName(external.userName()));

		if (internal == null)
			intern(external);
		else
			update(external, internal);

		return external.userName();
	}

	protected void intern(PortalUser external) {

		logger.info("interning external gCube user: {}", external);

		Collection<Role> roles = roleMapper.map(external.roles());

		User user = user().name(external.userName()).email(external.email()).fullName(external.fullName()).is(roles).build();

		userRepository.add(user);

	}

	protected void update(PortalUser external, User internal) {

		logger.trace("updating internal user from external gCube user: {}", external);
		
		Collection<Role> roles = roleMapper.map(external.roles());

		User modified = modifyUser(internal).email(external.email()).fullName(external.fullName()).is(roles).build();

		userRepository.update(modified);
	}

	
	@Override
	public void add(String name, String pwd) {
		throw new UnsupportedOperationException("sign up active only through iMarine portal");
	}

}
