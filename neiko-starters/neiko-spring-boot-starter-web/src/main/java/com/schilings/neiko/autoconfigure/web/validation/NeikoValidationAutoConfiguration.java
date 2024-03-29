package com.schilings.neiko.autoconfigure.web.validation;

import com.schilings.neiko.autoconfigure.web.validation.exception.handler.GlobalValidationExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.annotation.Bean;

import jakarta.validation.executable.ExecutableValidator;

@AutoConfiguration(before = ValidationAutoConfiguration.class)
@ConditionalOnClass(ExecutableValidator.class)
@ConditionalOnResource(resources = "classpath:META-INF/services/jakarta.validation.spi.ValidationProvider")
public class NeikoValidationAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public GlobalValidationExceptionHandler globalValidationExceptionHandler() {
		return new GlobalValidationExceptionHandler();
	}

}
