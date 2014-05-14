/**
 * 
 */
package org.cotrix.gcube.extension;

import static org.cotrix.action.UserAction.*;
import static org.cotrix.common.Constants.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.Users.*;
import static org.cotrix.repository.UserQueries.*;
import static org.virtualrepository.CommonProperties.*;

import java.util.Collection;

import javax.annotation.Priority;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.common.cdi.ApplicationEvents.EndRequest;
import org.cotrix.common.cdi.ApplicationEvents.StartRequest;
import org.cotrix.common.cdi.BeanSession;
import org.cotrix.common.cdi.Current;
import org.cotrix.domain.user.Role;
import org.cotrix.domain.user.User;
import org.cotrix.gcube.stubs.PortalUser;
import org.cotrix.gcube.stubs.SessionToken;
import org.cotrix.io.CloudService;
import org.cotrix.repository.UserRepository;
import org.cotrix.security.Realm;
import org.gcube.common.scope.api.ScopeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtual.workspace.WorkspacePlugin;
import org.virtualrepository.Context;
import org.virtualrepository.RepositoryService;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 * 
 */
@Alternative
@Priority(RUNTIME)
@Singleton
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
	private CloudService cloud;
	
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
			internal = intern(external);
		else
			update(external, internal);

		initSession(stoken,internal);
		
		return external.userName();
	}
	
	void onStartRequest(@Observes StartRequest start){
		initRequest(session.get(SessionToken.class), session.get(User.class));
	}
	
	void onEndRequest(@Observes EndRequest end){
		endRequest();
	}
	
	
	private void initRequest(SessionToken token, User user) {
		
		Context.properties().add(USERNAME.property(user.name()));
		
		ScopeProvider.instance.set(token.scope());
		
	}
	
	private void endRequest() {
		
		ScopeProvider.instance.reset();
		
	}
	
	
	
	
	private void initSession(SessionToken token, User user) {
		
		initRequest(token, user);
		
		//refresh cloud with "personal" repos
		for (RepositoryService service : cloud.repositories())
			if (service.name().equals(WorkspacePlugin.name)) {
				cloud.discover(service);
				break;
			}
		
		//remember external session details
		session.add(SessionToken.class, token);
				
	}

	private User intern(PortalUser external) {

		logger.info("interning external gCube user: {}", external);

		Collection<Role> roles = roleMapper.map(external.roles());

		User user = user().name(external.userName()).fullName(external.fullName()).email(external.email()).is(roles).build();

		userRepository.add(user);
		
		//TODO tmp workaround
		User changeset = modifyUser(user).can(VIEW.on(user.id())).build();
		userRepository.update(changeset);
		
		return user;

	}

	private void update(PortalUser external, User internal) {

		logger.trace("updating internal user from external gCube user: {}", external);
		
		Collection<Role> roles = roleMapper.map(external.roles());

		User modified = modifyUser(internal)
							.fullName(external.fullName())
							.email(external.email())
							.isNoLonger(internal.directRoles()) //eliminate older roles first
							.is(roles).build();

		userRepository.update(modified);
	}

	
	@Override
	public void add(String name, String pwd) {
		throw new UnsupportedOperationException("sign up active only through iMarine portal");
	}

}
