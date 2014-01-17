/**
 * 
 */
package org.cotrix.gcube.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.cotrix.domain.dsl.Roles;
import org.cotrix.domain.user.Role;
import org.cotrix.gcube.stubs.GCubeUser;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DefaultRoleMapper implements RoleMapper {

	@Override
	public Collection<Role> mapRoles(GCubeUser gCubeUser) {
		List<Role> roles = Collections.singletonList(Roles.USER);
		return roles;
	}

}
