/**
 * 
 */
package org.cotrix.gcube.stubs;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class GCubeSessionMarshaller {
	
	protected JAXBContext context;
	protected Unmarshaller unmarshaller;
	protected Marshaller marshaller;
	
	public GCubeSessionMarshaller() {
		try {
		context = JAXBContext.newInstance(GCubeSession.class);
		unmarshaller = context.createUnmarshaller();
		marshaller = context.createMarshaller();
		} catch (JAXBException e) {
			throw new RuntimeException("JAXB component initialization failed", e);
		}
	}
	
	public String serialize(GCubeSession session) {
		try {
			StringWriter writer = new StringWriter();
			marshaller.marshal(session, writer);
			return writer.toString();
		} catch (JAXBException e) {
			throw new RuntimeException("Marshalling failed", e);
		}
	}
	
	public GCubeSession deserialize(String session) {
		try {
			ByteArrayInputStream stream = new ByteArrayInputStream(session.getBytes());
			return (GCubeSession)unmarshaller.unmarshal(stream);
		} catch (JAXBException e) {
			throw new RuntimeException("Unmarshalling failed", e);
		}
	}

}
