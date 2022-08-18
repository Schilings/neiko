package com.schilings.neiko.cloud.commons.serviceregistry.event;

import org.springframework.cloud.client.discovery.event.InstanceRegisteredEvent;
import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.context.ApplicationListener;

public class InstanceRegisteredEventListener implements ApplicationListener<InstanceRegisteredEvent> {

	@Override
	public void onApplicationEvent(InstanceRegisteredEvent event) {
		@Deprecated
		Object config = event.getConfig();
		AbstractAutoServiceRegistration autoServiceRegistration = (AbstractAutoServiceRegistration) event.getSource();
	}

}
