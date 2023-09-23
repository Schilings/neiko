package com.schilings.neiko.security.oauth2.authorization.server.customizer;

import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerConfigurerCustomizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2TokenRevocationEndpointConfigurer;

public abstract class OAuth2TokenRevocationEndpointConfigurerCustomizer
		implements OAuth2AuthorizationServerConfigurerCustomizer {

	@Override
	public void customize(OAuth2AuthorizationServerConfigurer configurer, HttpSecurity httpSecurity) {
		configurer.tokenRevocationEndpoint(tokenRevocation -> customize(tokenRevocation, httpSecurity));
	}

	public abstract void customize(OAuth2TokenRevocationEndpointConfigurer configurer, HttpSecurity httpSecurity);

}
