package com.schilings.neiko.samples.common.core;

import com.schilings.neiko.common.event.publisher.EventBus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		EventBus eventBus = context.getBean(EventBus.class);
		// bean.publishAsync(new DTO().setMessage("123213213"));
		String blocking = (String) eventBus.requestBlocking(new DTO().setMessage("asdasda"));
		System.out.println(blocking);

	}

}
