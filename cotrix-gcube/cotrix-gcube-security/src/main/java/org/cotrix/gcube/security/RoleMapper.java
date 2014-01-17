/**
 * 
 */
package org.cotrix.gcube.security;

import org.cotrix.domain.user.Role;
import org.cotrix.gcube.stubs.GCubeUser;

import java.util.Collection;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface RoleMapper {
	
	public Collection<Role> mapRoles(GCubeUser gCubeUser);

}
