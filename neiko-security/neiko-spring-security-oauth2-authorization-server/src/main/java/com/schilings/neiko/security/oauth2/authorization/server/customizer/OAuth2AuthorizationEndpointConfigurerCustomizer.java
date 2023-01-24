package com.schilings.neiko.security.oauth2.authorization.server.customizer;

import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerConfigurerCustomizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationEndpointConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.List;

public abstract class OAuth2AuthorizationEndpointConfigurerCustomizer
		implements OAuth2AuthorizationServerConfigurerCustomizer {

	protected final Log logger = LogFactory.getLog(getClass());

	@Override
	public void customize(OAuth2AuthorizationServerConfigurer configurer, HttpSecurity httpSecurity) {
		configurer.authorizationEndpoint(authorizationEndpoint -> customize(authorizationEndpoint, httpSecurity));
	}

	public abstract void customize(OAuth2AuthorizationEndpointConfigurer configurer, HttpSecurity httpSecurity);

}
