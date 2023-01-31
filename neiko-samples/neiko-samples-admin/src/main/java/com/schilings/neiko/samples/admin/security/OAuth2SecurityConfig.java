package com.schilings.neiko.samples.admin.security;

import com.fasterxml.jackson.databind.Module;
import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerExtensionConfigurer;

import com.schilings.neiko.security.oauth2.authorization.server.jackson.AuthorizationServerJackson2Module;
import com.schilings.neiko.security.oauth2.client.federated.identity.FederatedIdentityConfigurer;
import com.schilings.neiko.security.oauth2.resource.server.OAuth2ResourceServerConfigurerCustomizer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
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

	@Bean
	public OAuth2AuthorizationServerConfigurerCustomizer authorizationServerConfigurerCustomizer() {
		return (configuer,http) -> {
			//解决测试跨域， 开启Spring Security 对 CORS 的支持,逻辑简单，点进去看看
			// 跨域问题：https://blog.csdn.net/Hongyu_Liu/article/details/118930061,
			// 解决方法：https://blog.csdn.net/xu_hui123/article/details/128231972
			http.securityMatchers()
					.requestMatchers(new AntPathRequestMatcher("/oauth2/**", HttpMethod.OPTIONS.name()));
			http.cors().configurationSource(corsConfigurationSource());

		};
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(true);
		configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
		configuration.setAllowedMethods(Collections.singletonList("*"));
		configuration.setAllowedHeaders(Collections.singletonList("*"));
		configuration.setMaxAge(Duration.ofHours(1));
		source.registerCorsConfiguration("/**", configuration);
		return source;
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
