package com.schilings.neiko.security.oauth2.resource.server.customizer.jwt;

import com.schilings.neiko.security.oauth2.resource.server.OAuth2ResourceServerConfigurerCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

public class JwtOAuth2ResourceServerCustomizer implements OAuth2ResourceServerConfigurerCustomizer {

	private ApplicationContext context;

	private JwtDecoder jwtDecoder;

	private Converter<Jwt, ? extends AbstractAuthenticationToken> authenticationConverter;

	@Override
	public void customize(HttpSecurity http) throws Exception {
		http.oauth2ResourceServer(resourceServerConfigurer -> {
			// Jwt
			resourceServerConfigurer.jwt(jwt -> {
				jwt.decoder(getJwtDecoder(http));
				jwt.jwtAuthenticationConverter(getAuthenticationConverter(http));
			});
		});
	}

	private ApplicationContext getApplicationContext(HttpSecurity http) {
		if (this.context != null) {
			return this.context;
		}
		this.context = http.getSharedObject(ApplicationContext.class);
		return this.context;
	}

	private JwtDecoder getJwtDecoder(HttpSecurity http) {
		if (this.jwtDecoder != null) {
			return this.jwtDecoder;
		}
		this.jwtDecoder = getApplicationContext(http).getBean(JwtDecoder.class);

		return this.jwtDecoder;
	}

	private Converter<Jwt, ? extends AbstractAuthenticationToken> getAuthenticationConverter(HttpSecurity http) {
		if (this.authenticationConverter == null) {
			if (getApplicationContext(http).getBeanNamesForType(JwtAuthenticationConverter.class).length > 0) {
				this.authenticationConverter = getApplicationContext(http).getBean(JwtAuthenticationConverter.class);
			}
			else {
				this.authenticationConverter = new DefaultJwtAuthenticationConverter();
			}
		}
		return this.authenticationConverter;
	}

}
