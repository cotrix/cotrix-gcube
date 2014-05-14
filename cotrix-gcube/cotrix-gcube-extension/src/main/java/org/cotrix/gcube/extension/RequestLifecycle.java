package org.cotrix.gcube.extension;

import static org.virtualrepository.CommonProperties.*;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.common.cdi.BeanSession;
import org.cotrix.common.cdi.Current;
import org.cotrix.common.cdi.ApplicationEvents.EndRequest;
import org.cotrix.common.cdi.ApplicationEvents.StartRequest;
import org.cotrix.domain.user.User;
import org.cotrix.gcube.stubs.SessionToken;
import org.gcube.common.scope.api.ScopeProvider;
import org.virtualrepository.Context;

@Singleton
public class RequestLifecycle {

	@Inject
	@Current
	private BeanSession session;
	
	void onStartRequest(@Observes StartRequest start){
		init(session.get(SessionToken.class), session.get(User.class));
	}
	
	void onEndRequest(@Observes EndRequest end){
		stop();
	}
	
	
	public void init(SessionToken token, User user) {
		
		Context.properties().add(USERNAME.property(user.name()));
		
		ScopeProvider.instance.set(token.scope());
		
	}
	
	public void stop() {
		
		ScopeProvider.instance.reset();
		
	}
	
	
}
