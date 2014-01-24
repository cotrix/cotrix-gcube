/**
 * 
 */
package org.cotrix.gcube.portlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cotrix.gcube.stubs.PortalSession;
import org.cotrix.gcube.stubs.PortalUser;
import org.gcube.application.framework.core.session.ASLSession;
import org.gcube.application.framework.core.session.SessionManager;
import org.gcube.vomanagement.usermanagement.RoleManager;
import org.gcube.vomanagement.usermanagement.UserManager;
import org.gcube.vomanagement.usermanagement.exception.UserManagementSystemException;
import org.gcube.vomanagement.usermanagement.impl.liferay.LiferayRoleManager;
import org.gcube.vomanagement.usermanagement.impl.liferay.LiferayUserManager;
import org.gcube.vomanagement.usermanagement.model.RoleModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SessionProvider {

	protected static Logger logger = LoggerFactory.getLogger(SessionProvider.class);

	private static UserManager userManager = new LiferayUserManager();
	private static RoleManager roleManager = new LiferayRoleManager();

	public static PortalSession sessionFor(String id, String username) {
		
		logger.trace("representing session {} for {}", id, username);
		
		ASLSession aslSession = SessionManager.getInstance().getASLSession(id, username);

		PortalUser user = new PortalUser(aslSession.getUsername(),
									   aslSession.getUserFullName(),
									   aslSession.getUserEmailAddress(),
									   getUserRoles(aslSession));
	
		return new PortalSession(user,aslSession.getScopeName());

	}
	
	
	//helper

	private static List<String> getUserRoles(ASLSession session) {
		
		try {
			
			List<RoleModel> roles = roleManager.listRolesByUser(userManager.getUserId(session.getUsername()));

			if (roles.isEmpty()) 
				return Collections.emptyList();

			List<String> names = new ArrayList<String>(roles.size());
			
			for (RoleModel role:roles) 
				names.add(role.getRoleName());

			return names;
			
		} catch (UserManagementSystemException e) {
		
			throw new RuntimeException("cannot retrieve roles for "+session.getUsername(), e);
			
		}
	}

}