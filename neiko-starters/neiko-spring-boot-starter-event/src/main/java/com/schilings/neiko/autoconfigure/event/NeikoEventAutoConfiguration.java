package com.schilings.neiko.autoconfigure.event;

import com.schilings.neiko.common.event.NeikoEventHandlerMethodMapping;
import com.schilings.neiko.common.event.publisher.EventBus;
import com.schilings.neiko.common.event.publisher.EventBusImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class NeikoEventAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public NeikoEventHandlerMethodMapping neikoEventHandlerMethodMapping() {
		return new NeikoEventHandlerMethodMapping();
	}

	@Bean
	@ConditionalOnMissingBean(EventBus.class)
	@ConditionalOnProperty(name = "neiko.event.enabled", havingValue = "true", matchIfMissing = true)
	public EventBus eventBus(NeikoEventHandlerMethodMapping neikoEventHandlerMethodMapping) {
		return new EventBusImpl(neikoEventHandlerMethodMapping);
	}
	

}
