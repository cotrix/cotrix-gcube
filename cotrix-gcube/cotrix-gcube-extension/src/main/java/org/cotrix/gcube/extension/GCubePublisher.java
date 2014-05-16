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

	private Logger logger = LoggerFactory.getLogger(GCubePublisher.class);

	public void onImportEvent(@Observes Import importEvent) {
		logger.trace("onImportEvent event: {}", importEvent);
		try {
			PortalProxy portalProxy = importEvent.session.get(PortalProxy.class);
			portalProxy.publish(importEvent.name+" version "+ importEvent.version+" now available.");
		} catch(Exception e) {
			logger.error("Failed news propagation", e);
		}
	}

	public void onPublishEvent(@Observes Publish publishEvent) {
		logger.trace("onPublishEvent event: {}", publishEvent);
		try {
			PortalProxy portalProxy = publishEvent.session.get(PortalProxy.class);
			portalProxy.publish(publishEvent.name+" version "+ publishEvent.version+" has just been published to "+publishEvent.repository+".");
		} catch(Exception e) {
			logger.error("Failed news propagation", e);
		}
	}

	public void onVersionEvent(@Observes Version versionEvent) {
		logger.trace("onVersionEvent event: {}", versionEvent);
		try {
			PortalProxy portalProxy = versionEvent.session.get(PortalProxy.class);
			portalProxy.publish("version "+versionEvent.version+" of "+versionEvent.name+" now available.");
		} catch(Exception e) {
			logger.error("Failed news propagation", e);
		}
	}



}
