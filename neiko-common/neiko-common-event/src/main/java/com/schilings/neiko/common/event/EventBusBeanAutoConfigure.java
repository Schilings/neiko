package com.schilings.neiko.common.event;


import com.schilings.neiko.common.event.publisher.EventBus;
import com.schilings.neiko.common.event.publisher.EventBusAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class EventBusBeanAutoConfigure implements ApplicationListener<ContextRefreshedEvent> {

	private static AtomicBoolean autoConfigured = new AtomicBoolean(false);

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// 只加载一次
		if (autoConfigured.compareAndSet(false, true)) {
			ApplicationContext context = event.getApplicationContext();
			EventBus eventBus = context.getBean(EventBus.class);
			if (eventBus != null) {
				String[] namesForType = context.getBeanNamesForType(EventBusAware.class);
				if (namesForType.length > 0) {
					for (String s : namesForType) {
						EventBusAware bean = context.getBean(s, EventBusAware.class);
						bean.setEventBus(eventBus);
					}
					if (log.isTraceEnabled()) {
						log.trace("EventPusherAware configured");
					}
				}
				if (log.isTraceEnabled()) {
					log.trace("EventPusherAware not found");
				}
			}
		}
	}

}
