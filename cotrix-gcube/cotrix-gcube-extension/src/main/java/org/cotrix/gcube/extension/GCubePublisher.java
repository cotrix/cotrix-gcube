/**
 * 
 */
package org.cotrix.gcube.extension;

import javax.enterprise.event.Observes;

import org.cotrix.action.events.CodelistActionEvents.Import;
import org.cotrix.action.events.CodelistActionEvents.Publish;
import org.cotrix.action.events.CodelistActionEvents.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class GCubePublisher {
	
	protected Logger logger = LoggerFactory.getLogger(GCubePublisher.class);
	
	public void onImportEvent(@Observes Import importEvent) {
		logger.trace("onImportEvent event: {}", importEvent);
		PortalProxy portalProxy = importEvent.session.get(PortalProxy.class);
		portalProxy.publish(importEvent.name+" version "+ importEvent.version+" now available.");
	}
	
	public void onPublishEvent(@Observes Publish publishEvent) {
		logger.trace("onPublishEvent event: {}", publishEvent);
		PortalProxy portalProxy = publishEvent.session.get(PortalProxy.class);
		portalProxy.publish(publishEvent.name+" version "+ publishEvent.version+" has just been published to "+publishEvent.repository+".");
	}
	
	public void onVersionEvent(@Observes Version versionEvent) {
		logger.trace("onVersionEvent event: {}", versionEvent);
		PortalProxy portalProxy = versionEvent.session.get(PortalProxy.class);
		portalProxy.publish("version "+versionEvent.version+" of "+versionEvent.name+" now available.");
	}

	
	
}
