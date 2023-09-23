package com.schilings.neiko.security.oauth2.authorization.server.customizer;

import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerConfigurerCustomizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2TokenEndpointConfigurer;

public abstract class OAuth2TokenEndpointConfigurerCustomizer implements OAuth2AuthorizationServerConfigurerCustomizer {

	@Override
	public void customize(OAuth2AuthorizationServerConfigurer configurer, HttpSecurity http) {
		configurer.tokenEndpoint(tokenEnpoint -> customize(tokenEnpoint, http));
	}

	public abstract void customize(OAuth2TokenEndpointConfigurer configurer, HttpSecurity http);

}
