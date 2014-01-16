/**
 * 
 */
package org.cotrix.gcube.security;

import static org.cotrix.action.CodelistAction.*;
import static org.cotrix.action.MainAction.*;
import static org.cotrix.action.ResourceType.*;
import static org.cotrix.domain.dsl.Users.*;

import org.cotrix.domain.user.Role;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class GCubeRoles {
	
	public static final Role USER = user().name("user").noMail().fullName("User Role").can(VIEW, ACCESS_ADMIN_AREA).buildAsRoleFor(application);


}
