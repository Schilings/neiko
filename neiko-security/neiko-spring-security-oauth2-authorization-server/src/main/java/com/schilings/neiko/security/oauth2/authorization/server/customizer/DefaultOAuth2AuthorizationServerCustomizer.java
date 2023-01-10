package com.schilings.neiko.security.oauth2.authorization.server.customizer;

import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerExtensionConfigurer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Order//优先级最低，最慢加载
public class DefaultOAuth2AuthorizationServerCustomizer implements OAuth2AuthorizationServerConfigurerCustomizer {

	@Override
	public void customize(OAuth2AuthorizationServerConfigurer configurer, HttpSecurity http) throws Exception {

		// @formatter:on
		RequestMatcher endpointsMatcher = configurer.getEndpointsMatcher();
		http
				// 操作RequestMatcherConfigurer，留给其他的Configurer操作空间
				.requestMatchers().requestMatchers(endpointsMatcher)
				.and()
				.csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
				.apply(configurer);
		// @formatter:off
		http.apply(new LastTriggeredConfigurer());
    }

	static class LastTriggeredConfigurer extends OAuth2AuthorizationServerExtensionConfigurer<LastTriggeredConfigurer, HttpSecurity> {

		@Override
		public void configure(HttpSecurity httpSecurity) throws Exception {
			httpSecurity.authorizeRequests().anyRequest().authenticated();
		}
	}



}
