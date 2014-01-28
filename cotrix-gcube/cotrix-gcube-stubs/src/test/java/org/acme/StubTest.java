package org.acme;

import static java.util.Arrays.*;
import static org.cotrix.gcube.stubs.SessionContext.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.cotrix.gcube.stubs.PortalSession;
import org.cotrix.gcube.stubs.PortalUser;
import org.junit.Test;

public class StubTest {
	
	
	@Test
	public void roundtrip() {
		
		PortalUser user = new PortalUser("n", "fn", "em", asList("r1","r2"));
		
		PortalSession session = new PortalSession(user, "s");
		
		assertEquals(session,deserialize(serialize(session)));
	}

	@Test
	public void noroles() {
		
		PortalUser user = new PortalUser("n", "fn", "em", new ArrayList<String>());
		
		PortalSession session = new PortalSession(user, "s");
		
		assertEquals(session,deserialize(serialize(session)));
	}

}
