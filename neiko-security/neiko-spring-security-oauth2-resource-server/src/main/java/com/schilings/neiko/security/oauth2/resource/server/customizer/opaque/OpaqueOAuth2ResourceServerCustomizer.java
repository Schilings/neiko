package com.schilings.neiko.security.oauth2.resource.server.customizer.opaque;

import com.schilings.neiko.security.oauth2.resource.server.OAuth2ResourceServerConfigurerCustomizer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

@RequiredArgsConstructor
public class OpaqueOAuth2ResourceServerCustomizer implements OAuth2ResourceServerConfigurerCustomizer {

	private ApplicationContext context;

	private OpaqueTokenIntrospector introspector;

	@Override
	public void customize(HttpSecurity http) throws Exception {
		http.oauth2ResourceServer(resourceServerConfigurer -> {
			// OpaqueToken
			resourceServerConfigurer.opaqueToken(opaqueToken -> {
				opaqueToken.introspector(getIntrospector(http));
				// authenticationManager cannot be null
				// opaqueToken.authenticationManager(getAuthenticationManager(http));
			});
		});
	}

	public OpaqueOAuth2ResourceServerCustomizer introspector(OpaqueTokenIntrospector introspector) {
		this.introspector = introspector;
		return this;
	}

	private ApplicationContext getApplicationContext(HttpSecurity http) {
		if (this.context != null) {
			return this.context;
		}
		this.context = http.getSharedObject(ApplicationContext.class);
		return this.context;
	}

	private OpaqueTokenIntrospector getIntrospector(HttpSecurity http) {
		if (this.introspector != null) {
			return this.introspector;
		}
		this.introspector = getApplicationContext(http).getBean(OpaqueTokenIntrospector.class);
		return this.introspector;
	}

}
