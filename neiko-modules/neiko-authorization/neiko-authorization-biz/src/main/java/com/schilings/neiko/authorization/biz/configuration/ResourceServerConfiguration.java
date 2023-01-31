package com.schilings.neiko.authorization.biz.configuration;

import com.schilings.neiko.authorization.biz.component.CustomAccessDeniedHandler;
import com.schilings.neiko.authorization.biz.component.CustomAuthenticationEntryPoint;
import com.schilings.neiko.security.oauth2.resource.server.autoconfigure.EnableResourceServer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration(proxyBeanMethods = false)
@EnableResourceServer
public class ResourceServerConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

	@Bean
	@ConditionalOnMissingBean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new CustomAuthenticationEntryPoint();
	}

}
