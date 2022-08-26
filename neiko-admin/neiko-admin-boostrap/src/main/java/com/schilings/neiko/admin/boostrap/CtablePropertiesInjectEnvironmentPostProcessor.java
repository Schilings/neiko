package com.schilings.neiko.admin.boostrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class CtablePropertiesInjectEnvironmentPostProcessor implements EnvironmentPostProcessor {

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		Map<String, Object> map = new HashMap<>();
		MapPropertySource ctableSource = new MapPropertySource("ctable", map);
		environment.getPropertySources().addFirst(ctableSource);

	}

}
