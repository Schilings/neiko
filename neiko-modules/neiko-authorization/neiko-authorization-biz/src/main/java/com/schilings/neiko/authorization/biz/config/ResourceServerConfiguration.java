package com.schilings.neiko.authorization.biz.config;

import com.schilings.neiko.security.oauth2.resource.server.config.EnableResourceServer;
import com.schilings.neiko.security.oauth2.resource.server.customizer.opaque.SpringAuthorizationServerSharedStoredOpaqueTokenIntrospector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

@Configuration(proxyBeanMethods = false)
@EnableResourceServer
public class ResourceServerConfiguration {

	@Bean
	public SpringAuthorizationServerSharedStoredOpaqueTokenIntrospector opaqueTokenIntrospector(
			OAuth2AuthorizationService authorizationService, RegisteredClientRepository registeredClientRepository) {
		return new SpringAuthorizationServerSharedStoredOpaqueTokenIntrospector(authorizationService,
				registeredClientRepository);
	}

}
