package org.cotrix.gcube.security;

import org.cotrix.domain.dsl.Roles;
import org.cotrix.domain.user.Role;

public enum PortalRole {

	VRE_MANAGER("VRE-Manager",Roles.MANAGER),
	VO_ADMIN("VO-Admin",Roles.ROOT);
	
	public final String value;
	public final Role internal;
	
	PortalRole(String value,Role internal) {
		this.value=value;
		this.internal=internal;
	}
	
	
}
