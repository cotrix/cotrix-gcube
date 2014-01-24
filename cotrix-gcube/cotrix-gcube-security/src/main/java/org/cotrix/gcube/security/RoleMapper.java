/**
 * 
 */
package org.cotrix.gcube.security;

import java.util.Collection;

import org.cotrix.domain.user.Role;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface RoleMapper {
	
	public Collection<Role> map(Collection<String> roles);

}
