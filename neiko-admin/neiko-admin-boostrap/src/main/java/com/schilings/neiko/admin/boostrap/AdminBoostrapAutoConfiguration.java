package com.schilings.neiko.admin.boostrap;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(AdminBoostrapProperties.class)
public class AdminBoostrapAutoConfiguration {

	@Bean
	@ConditionalOnProperty(name = "neiko.admin.boostrap.enabled", havingValue = "true", matchIfMissing = false)
	public BoostrapApplicationRunner boostrapApplicationRunner() {
		return new BoostrapApplicationRunner();
	}

}
