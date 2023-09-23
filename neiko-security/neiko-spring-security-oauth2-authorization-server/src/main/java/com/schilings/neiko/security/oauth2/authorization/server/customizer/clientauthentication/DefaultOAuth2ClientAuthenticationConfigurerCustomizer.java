package com.schilings.neiko.security.oauth2.authorization.server.customizer.clientauthentication;

import com.schilings.neiko.security.oauth2.authorization.server.customizer.OAuth2ClientAuthenticationConfigurerCustomizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;

public class DefaultOAuth2ClientAuthenticationConfigurerCustomizer
		extends OAuth2ClientAuthenticationConfigurerCustomizer {

	@Override
	public void customize(OAuth2AuthorizationServerConfigurer authorizationServerConfigurer, HttpSecurity httpSecurity)
			throws Exception {
		authorizationServerConfigurer.clientAuthentication(configurer -> {

		});
	}

}
