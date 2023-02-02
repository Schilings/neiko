package com.schilings.neiko.security.oauth2.authorization.server.autoconfigure;

import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerExtensionConfigurer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.*;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.authorization.DefaultOAuth2AuthorizationEndpointCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.formlogin.FormLoginRememberMeConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.introspection.DefaultOAuth2TokenIntrospectionCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.oidc.DefaultOAuth2OidcConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.revocation.DefaultOAuth2TokenRevocationCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.DefaultOAuth2TokenEndpointConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.handler.NullEventAuthenticationFailureHandler;
import com.schilings.neiko.security.oauth2.authorization.server.handler.NullEventAuthenticationSuccessHandler;
import com.schilings.neiko.security.oauth2.authorization.server.properties.OAuth2AuthorizationServerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * <p>
 * 注入默认的Customizer
 * </p>
 *
 * @author Schilings
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
class DefaultCustomizerAutoConfiguration {

	private final OAuth2AuthorizationServerProperties authorizationServerProperties;

	/**
	 * 表单登陆支持
	 * @return FormLoginConfigurerCustomizer
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = OAuth2AuthorizationServerProperties.PREFIX, name = "form-login-enabled",
			havingValue = "true")
	public FormLoginRememberMeConfigurerCustomizer formLoginRememberMeConfigurerCustomizer(
			UserDetailsService userDetailsService) {
		FormLoginRememberMeConfigurerCustomizer customizer = new FormLoginRememberMeConfigurerCustomizer(
				userDetailsService);
		customizer.enabled(authorizationServerProperties.isLoginPageEnabled())
				.loginPage(authorizationServerProperties.getLoginPage());
		return customizer;
	}

	/**
	 * Custom Configurer Injection
	 * @param configurers
	 * @return
	 */
	@Bean
	public OAuth2AuthorizationServerExtensionConfigurerInjectionCustomizer extensionConfigurerInjectionCustomizer(
			@Autowired(required = false) List<OAuth2AuthorizationServerExtensionConfigurer> configurers) {
		return new OAuth2AuthorizationServerExtensionConfigurerInjectionCustomizer(configurers);
	}

	/**
	 * Default Configuration
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(DefaultOAuth2AuthorizationServerCustomizer.class)
	public DefaultOAuth2AuthorizationServerCustomizer defaultOAuth2AuthorizationServerCustomizer() {
		return new DefaultOAuth2AuthorizationServerCustomizer();
	}

	/**
	 * Authorization Endpoint
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean({ OAuth2AuthorizationEndpointConfigurerCustomizer.class,
			DefaultOAuth2AuthorizationEndpointCustomizer.class })
	public DefaultOAuth2AuthorizationEndpointCustomizer defaultAuthorizationEndpointCustomizer() {
		DefaultOAuth2AuthorizationEndpointCustomizer authorizationCustomizer = new DefaultOAuth2AuthorizationEndpointCustomizer();
		authorizationCustomizer.consentPage(authorizationServerProperties.getConsentPage());
		authorizationCustomizer.stateless(authorizationServerProperties.isStateless());
		return authorizationCustomizer;
	}

	/**
	 * Token Endpoint
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean({ OAuth2TokenEndpointConfigurerCustomizer.class,
			DefaultOAuth2TokenEndpointConfigurerCustomizer.class })
	public DefaultOAuth2TokenEndpointConfigurerCustomizer extensionGrantTypeCustomizer() {
		DefaultOAuth2TokenEndpointConfigurerCustomizer extensionGrantTypeCustomizer = new DefaultOAuth2TokenEndpointConfigurerCustomizer();
		extensionGrantTypeCustomizer.accessTokenResponseHandler(NullEventAuthenticationSuccessHandler::new);
		extensionGrantTypeCustomizer.errorResponseHandler(NullEventAuthenticationFailureHandler::new);
		return extensionGrantTypeCustomizer;
	}

	/**
	 * Token Revocation Endpoint
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean({ OAuth2TokenRevocationEndpointConfigurerCustomizer.class,
			DefaultOAuth2TokenRevocationCustomizer.class })
	public DefaultOAuth2TokenRevocationCustomizer defaultTokenRevocationCustomizer() {
		DefaultOAuth2TokenRevocationCustomizer tokenRevocationCustomizer = new DefaultOAuth2TokenRevocationCustomizer();
		return tokenRevocationCustomizer;
	}

	/**
	 * Token Introspection Endpoint
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean({ OAuth2TokenIntrospectionEndpointConfigurerCustomizer.class,
			DefaultOAuth2TokenIntrospectionCustomizer.class })
	public DefaultOAuth2TokenIntrospectionCustomizer defaultOAuth2TokenIntrospectionCustomizer() {
		DefaultOAuth2TokenIntrospectionCustomizer tokenIntrospectionCustomizer = new DefaultOAuth2TokenIntrospectionCustomizer();
		return tokenIntrospectionCustomizer;
	}

	/**
	 * Enable OpenId Connect
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean({ DefaultOAuth2OidcConfigurerCustomizer.class })
	public DefaultOAuth2OidcConfigurerCustomizer oidcConfigurerCustomizer() {
		return new DefaultOAuth2OidcConfigurerCustomizer();
	}

}
