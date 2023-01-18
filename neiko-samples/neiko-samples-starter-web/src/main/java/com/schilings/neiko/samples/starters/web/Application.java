package com.schilings.neiko.samples.starters.web;


import com.schilings.neiko.common.core.desensitize.DesensitizationHandlerHolder;
import com.schilings.neiko.common.core.desensitize.handler.DesensitizationHandler;
import com.schilings.neiko.common.core.desensitize.handler.regex.DefaultRegexDesensitizationHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		DesensitizationHandler handler = DesensitizationHandlerHolder.getHandler(DefaultRegexDesensitizationHandler.class);
	}

}
