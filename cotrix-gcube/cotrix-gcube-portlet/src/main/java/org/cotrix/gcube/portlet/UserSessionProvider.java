/**
 * 
 */
package org.cotrix.gcube.portlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.gcube.application.cotrix.proxydomain.User;
import org.gcube.application.cotrix.proxydomain.UserSession;
import org.gcube.application.cotrix.proxydomain.UserSessionMarshaller;
import org.gcube.application.framework.core.session.ASLSession;
import org.gcube.application.framework.core.session.SessionManager;
import org.gcube.portal.custom.scopemanager.scopehelper.ScopeHelper;
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
public class UserSessionProvider {

	protected static Logger logger = LoggerFactory.getLogger(UserSessionProvider.class);

	protected static UserSessionMarshaller marshaller = new UserSessionMarshaller();
	protected static UserManager userManager = new LiferayUserManager();
	protected static RoleManager roleManager = new LiferayRoleManager();

	public static String getUserSession(HttpSession httpSession) {
		String username = (String) httpSession.getAttribute(ScopeHelper.USERNAME_ATTRIBUTE);
		logger.trace("username: {}", username);
		logger.trace("sessionId: {}", httpSession.getId());
		ASLSession aslSession = SessionManager.getInstance().getASLSession(httpSession.getId(), username);

		User user = new User();
		user.setUsername(aslSession.getUsername());
		user.setFullname(aslSession.getUserFullName());
		user.setEmail(aslSession.getUserEmailAddress());

		List<String> roles = getUserRoles(aslSession);
		user.setRoles(roles);

		UserSession userSession = new UserSession();
		userSession.setUser(user);
		userSession.setScope(aslSession.getScopeName());

		logger.trace("userSession: {}", userSession);
		String response = marshaller.serialize(userSession);
		logger.trace("response: {}", response);
		return response;
	}

	protected static List<String> getUserRoles(ASLSession session) {
		try {
			List<RoleModel> userRoles = roleManager.listRolesByUser(userManager.getUserId(session.getUsername()));

			if (userRoles.isEmpty()) return Collections.emptyList();

			List<String> roles = new ArrayList<String>(userRoles.size());
			for (RoleModel userRole:userRoles) roles.add(userRole.getRoleName());

			return roles;
		} catch (UserManagementSystemException e) {
			logger.error("Error retrieving the users roles", e);
			return Collections.emptyList();
		}
	}

}
