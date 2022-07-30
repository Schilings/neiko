package com.schilings.neiko.autoconfigure.web.api;

import com.schilings.neiko.autoconfigure.web.api.properties.ApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

/**
 * <pre>{@code
 *
 * }
 * <p>Api自动配置</p>
 * </pre>
 *
 * @author Schilings
 */

@Deprecated
@AutoConfiguration(before = WebMvcAutoConfiguration.class)
@RequiredArgsConstructor
@EnableConfigurationProperties(ApiProperties.class)
// @Import(NeikoWebMvcConfiguration.class)
public class ApiAutoConfiguration {

	@RequiredArgsConstructor
	@Configuration(proxyBeanMethods = false)
	static class CustomWebMvcConfigurer implements WebMvcConfigurer {

	}

}
