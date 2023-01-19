package com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.client;

import com.schilings.neiko.security.oauth2.authorization.server.util.OAuth2ConfigurerUtils;
import com.schilings.neiko.security.oauth2.client.federated.identity.FederatedIdentityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class FederatedIdentityConfigurerConfiguration {

	public static void applyDefaultFederatedIdentity(FederatedIdentityConfigurer identityConfigurer,
			HttpSecurity http) {
		identityConfigurer.authorizationRequestCustomizer(new OAuth2FederatedIdentityAuthorizationRequestCustomizer());
		identityConfigurer
				.authorizationRequestRepository(new OAuth2FederatedIdentityOAuth2AuthorizationRequestRepository());
		identityConfigurer.successHandler(new OAuth2FederatedIdentityAuthenticationSuccessHandler(
				OAuth2ConfigurerUtils.getAuthorizationService(http),
				OAuth2ConfigurerUtils.getRegisteredClientRepository(http)));
		http.securityMatchers().requestMatchers(
				new AntPathRequestMatcher(identityConfigurer.getFilterProccessUri()),
				new AntPathRequestMatcher(identityConfigurer.getAuthorizationRequestUri()));
	}

}
