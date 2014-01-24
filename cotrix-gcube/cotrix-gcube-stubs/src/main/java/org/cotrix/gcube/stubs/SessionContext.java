/**
 * 
 */
package org.cotrix.gcube.stubs;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 * 
 */
public abstract class SessionContext {

	private static JAXBContext context;

	static {
		
		try {
			
			context = JAXBContext.newInstance(PortalSession.class);
			
		} catch (JAXBException e) {
		
			throw new RuntimeException("JAXB component initialization failed", e);
		}
		
	}
	
	public static String serialize(PortalSession session) {
		try {
			
			StringWriter writer = new StringWriter();
			
			context.createMarshaller().marshal(session, writer);
		
			return writer.toString();
		
		} catch (JAXBException e) {
			throw new RuntimeException("Marshalling failed", e);
		}
	}

	public static PortalSession deserialize(String session) {
	
		try {
			ByteArrayInputStream stream = new ByteArrayInputStream(session.getBytes());
		
			return (PortalSession) context.createUnmarshaller().unmarshal(stream);
		
		} catch (JAXBException e) {
			throw new RuntimeException("Unmarshalling failed", e);
		}
	}

}
