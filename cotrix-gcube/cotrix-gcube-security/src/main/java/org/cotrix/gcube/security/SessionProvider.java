package org.cotrix.gcube.security;

import org.cotrix.gcube.stubs.PortalSession;
import org.cotrix.gcube.stubs.SessionToken;

public interface SessionProvider {

	
	PortalSession sessionFor(SessionToken token);
	
}
