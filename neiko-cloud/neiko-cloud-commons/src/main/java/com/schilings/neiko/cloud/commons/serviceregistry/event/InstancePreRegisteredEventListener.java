package com.schilings.neiko.cloud.commons.serviceregistry.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.event.InstancePreRegisteredEvent;
import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.ApplicationListener;

/**
 *
 * <p>
 * {@link AbstractAutoServiceRegistration}
 * </p>
 *
 * @author Schilings
 */
@Slf4j
public class InstancePreRegisteredEventListener implements ApplicationListener<InstancePreRegisteredEvent> {

	@Override
	public void onApplicationEvent(InstancePreRegisteredEvent event) {
		Registration registration = event.getRegistration();
		AbstractAutoServiceRegistration autoServiceRegistration = (AbstractAutoServiceRegistration) event.getSource();
	}

}
