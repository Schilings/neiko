package com.schilings.neiko.samples.admin.security;

import com.fasterxml.jackson.databind.Module;
import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerExtensionConfigurer;

import com.schilings.neiko.security.oauth2.authorization.server.jackson.AuthorizationServerJackson2Module;
import com.schilings.neiko.security.oauth2.client.federated.identity.FederatedIdentityConfigurer;
import com.schilings.neiko.security.oauth2.resource.server.OAuth2ResourceServerConfigurerCustomizer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;

import java.util.*;

@Configuration
@RequiredArgsConstructor
public class OAuth2SecurityConfig {
	

	@Bean
	public OAuth2ResourceServerConfigurerCustomizer resourceServerConfigurerCustomizer() {
		return http -> {
			// 适配h2 console页面
			http.headers().frameOptions().disable();
		};
	}


	/**
	 * 测试
	 */
	@Configuration(proxyBeanMethods = false)
	static class TestConfiguration{

		/**
		 * 给自动生成的页面OAuth2登录加上redirect_uri
		 * @param clientProperties
		 * @return
		 */
		@Bean
		public LoginPageExtensionConfigurer extensionConfigurer(OAuth2ClientProperties clientProperties) {
			return new LoginPageExtensionConfigurer(clientProperties);
		}

		@RequiredArgsConstructor
		static class LoginPageExtensionConfigurer
				extends OAuth2AuthorizationServerExtensionConfigurer<LoginPageExtensionConfigurer, HttpSecurity> {

			private final OAuth2ClientProperties clientProperties;

			@Override
			public void configure(HttpSecurity httpSecurity) throws Exception {
				DefaultLoginPageGeneratingFilter loginPageGeneratingFilter = httpSecurity
						.getSharedObject(DefaultLoginPageGeneratingFilter.class);
				if (loginPageGeneratingFilter != null) {
					Map<String, String> loginUrlToClientName = new HashMap<>();
					clientProperties.getRegistration().forEach((s, v) -> {
						String authorizationRequestUri = FederatedIdentityConfigurer.AUTHORIZATION_REQUEST_BASE_URI + "/"
								+ s;
						authorizationRequestUri += "?response_type=code&client_id=test&redirect_uri=http://localhost:9000/oauth2Login";
						loginUrlToClientName.put(authorizationRequestUri, v.getClientName());
					});
					loginPageGeneratingFilter.setOauth2AuthenticationUrlToClientName(loginUrlToClientName);
				}
			}

		}
	}

}
