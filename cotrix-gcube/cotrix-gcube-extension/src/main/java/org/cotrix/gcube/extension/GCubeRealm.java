/**
 * 
 */
package org.cotrix.gcube.extension;

import static org.cotrix.action.UserAction.*;
import static org.cotrix.common.Constants.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.Users.*;
import static org.cotrix.repository.UserQueries.*;

import java.util.Collection;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import org.cotrix.common.cdi.BeanSession;
import org.cotrix.common.cdi.Current;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
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
	private PortalProxyProvider safePortalUrlProvider;

	@Inject
	private PortalProxyProvider portalProxyProvider;

	@Inject
	private UserRepository userRepository;

	@Inject
	private RoleMapper roleMapper;
	
	@Inject
	@Current
	private BeanSession session;

	@Override
	public boolean supports(Object token) {

		return token instanceof SessionToken;

	}

	@Override
	public String login(Object token) {

		SessionToken stoken = reveal(token, SessionToken.class);
		
		PortalProxy portalProxy = portalProxyProvider.getPortalProxy(stoken);
		session.add(PortalProxy.class, portalProxy);

		PortalUser external = portalProxy.getPortalUser();

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
		
		//TODO tmp workaround
		User changeset = modifyUser(user).can(VIEW.on(user.id())).build();
		userRepository.update(changeset);

	}

	protected void update(PortalUser external, User internal) {

		logger.trace("updating internal user from external gCube user: {}", external);
		
		Collection<Role> roles = roleMapper.map(external.roles());

		User modified = modifyUser(internal)
							.email(external.email())
							.fullName(external.fullName())
							.isNot(internal.directRoles()) //eliminate older roles first
							.is(roles).build();

		userRepository.update(modified);
	}

	
	@Override
	public void add(String name, String pwd) {
		throw new UnsupportedOperationException("sign up active only through iMarine portal");
	}

}
