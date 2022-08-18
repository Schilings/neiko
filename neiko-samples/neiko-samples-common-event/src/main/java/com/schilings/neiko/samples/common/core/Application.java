package com.schilings.neiko.samples.common.core;

import com.schilings.neiko.common.event.publisher.AsyncEventPublisher;
import com.schilings.neiko.common.event.publisher.EventPublisher;
import com.schilings.neiko.common.event.NeikoEventHandlerMethodMapping;
import com.schilings.neiko.common.event.publisher.SimpleEventPublisher;
import com.schilings.neiko.common.eventbus.EventBus;
import com.schilings.neiko.common.eventbus.EventBusImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		EventPublisher bean = context.getBean(EventPublisher.class);
		bean.publish(new DTO().setMessage("123213213"));
	}



}
