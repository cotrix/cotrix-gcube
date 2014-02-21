package org.cotrix.gcube.extension;

import org.cotrix.domain.dsl.Roles;
import org.cotrix.domain.user.Role;

public enum PortalRole {

	VRE_Designer("VRE-Designer", Roles.MANAGER),
	VRE_MANAGER("VRE-Manager",Roles.MANAGER),
	VO_ADMIN("VO-Admin",Roles.MANAGER);
	
	public final String value;
	public final Role internal;
	
	PortalRole(String value,Role internal) {
		this.value=value;
		this.internal=internal;
	}
	
	
}
