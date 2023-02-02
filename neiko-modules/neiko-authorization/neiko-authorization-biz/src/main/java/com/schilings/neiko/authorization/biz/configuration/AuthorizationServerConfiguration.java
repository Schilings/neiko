package com.schilings.neiko.authorization.biz.configuration;

import com.fasterxml.jackson.databind.Module;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.schilings.neiko.authorization.biz.component.CustomOAuth2AuthorizationConsentService;
import com.schilings.neiko.authorization.biz.component.CustomOAuth2AuthorizationService;
import com.schilings.neiko.authorization.biz.component.CustomRegisteredClientRepository;
import com.schilings.neiko.authorization.biz.federated.*;
import com.schilings.neiko.authorization.biz.handler.OAuth2AccessTokenAuthenticationSuccessHandler;
import com.schilings.neiko.authorization.biz.handler.OAuth2TokenRevocationAuthenticationSuccessHandler;
import com.schilings.neiko.authorization.biz.jose.Jwks;
import com.schilings.neiko.authorization.biz.service.AuthorizationConsentService;
import com.schilings.neiko.authorization.biz.service.AuthorizationService;
import com.schilings.neiko.authorization.biz.service.OAuth2RegisteredClientService;
import com.schilings.neiko.authorization.common.jackson2.NeikoAuthorizationJackson2Module;
import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.autoconfigure.EnableAuthorizationServer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.revocation.DefaultOAuth2TokenRevocationCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.DefaultOAuth2TokenEndpointConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.OAuth2FederatedIdentityAuthenticationConverter;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.OAuth2FederatedIdentityAuthenticationProvider;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.client.FederatedIdentityConfigurerConfiguration;
import com.schilings.neiko.security.oauth2.authorization.server.jackson.AuthorizationServerJackson2Module;
import com.schilings.neiko.security.oauth2.authorization.server.util.OAuth2ConfigurerUtils;
import com.schilings.neiko.security.oauth2.client.federated.identity.FederatedIdentityConfigurer;
import com.schilings.neiko.security.oauth2.client.federated.identity.OAuth2UserMerger;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;

import java.util.ArrayList;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfiguration {

	/**
	 * Token Endpoint拓展授权方式
	 * @return
	 */
	@Bean
	public DefaultOAuth2TokenEndpointConfigurerCustomizer extensionGrantTypeCustomizer() {
		DefaultOAuth2TokenEndpointConfigurerCustomizer extensionGrantTypeCustomizer = new DefaultOAuth2TokenEndpointConfigurerCustomizer();
		extensionGrantTypeCustomizer.converterExpander(((converters, http) -> {
			converters.add(new OAuth2FederatedIdentityAuthenticationConverter());
		}));
		extensionGrantTypeCustomizer.providerExpander((providers, http) -> {
			providers.add(new OAuth2FederatedIdentityAuthenticationProvider(
					OAuth2ConfigurerUtils.getAuthorizationService(http),
					OAuth2ConfigurerUtils.getTokenGenerator(http)));
		});
		extensionGrantTypeCustomizer.accessTokenResponseHandler(OAuth2AccessTokenAuthenticationSuccessHandler::new);
		return extensionGrantTypeCustomizer;
	}

	/**
	 * Token Revocation Endpoint配置
	 * @return
	 */
	@Bean
	public OAuth2AuthorizationServerConfigurerCustomizer tokenRevocationCustomizer() {
		DefaultOAuth2TokenRevocationCustomizer tokenRevocationCustomizer = new DefaultOAuth2TokenRevocationCustomizer();
		tokenRevocationCustomizer.revocationResponseHandler(OAuth2TokenRevocationAuthenticationSuccessHandler::new);
		return tokenRevocationCustomizer;
	}

	/**
	 * 自定义Authorization Server添加联合登录的支持
	 *
	 * @return
	 */
	@Configuration
	static class AuthorizationServerFederatedIdentityConfig {

		@Bean
		@ConditionalOnMissingBean
		public OAuth2UserMerger oAuth2UserMerger(ObjectProvider<WechatUserService> wechatUserServices,
				ObjectProvider<WorkWechatUserService> workWechatUserServices,
				ObjectProvider<OidcUserService> oidcUserServices,
				ObjectProvider<OAuth2UserService> oAuth2UserServices) {
			return new DefaultOAuth2UserMerger(wechatUserServices, workWechatUserServices, oidcUserServices,
					oAuth2UserServices);
		}

		@Bean
		public OAuth2AuthorizationServerConfigurerCustomizer federatedIdentityAuthorizationServerConfigurerCustomizer(
				OAuth2UserMerger oAuth2UserMerger) {
			return (configurer, http) -> {
				ObjectPostProcessor postProcessor = http.getSharedObject(ObjectPostProcessor.class);
				FederatedIdentityConfigurer federatedIdentityConfigurer = new FederatedIdentityConfigurer(http);
				// 对授权服务端添加联合登录的支持
				FederatedIdentityConfigurerConfiguration.applyDefaultFederatedIdentity(federatedIdentityConfigurer,
						http);
				// format:off
				http.apply(federatedIdentityConfigurer).wechatOAuth2Login().workWechatOAuth2Login()
						.oauth2UserMerger(oAuth2UserMerger);
				// format:on
			};
		}

	}

	/**
	 * Authorization Server 持久化配置
	 */
	@Configuration
	static class AuthorizationServerPersistenceConfiguration {

		/**
		 * RegisteredClientRepository
		 * @param registeredClientService
		 * @return
		 */
		@Bean
		@ConditionalOnMissingBean
		public RegisteredClientRepository customRegisteredClientRepository(
				OAuth2RegisteredClientService registeredClientService) {
			return new CustomRegisteredClientRepository(registeredClientService);
		}

		/**
		 * OAuth2AuthorizationConsentService
		 * @return
		 */
		@Bean
		@ConditionalOnMissingBean
		public OAuth2AuthorizationConsentService authorizationConsentService(AuthorizationConsentService consentService,
				RegisteredClientRepository registeredClientRepository) {
			return new CustomOAuth2AuthorizationConsentService(consentService, registeredClientRepository);
		}

		/**
		 * OAuth2AuthorizationService
		 * @return
		 */
		@Bean
		@ConditionalOnMissingBean
		public OAuth2AuthorizationService authorizationService(AuthorizationService authorizationService,
				RegisteredClientRepository registeredClientRepository,
				// Jackson2ObjectMapperBuilder是Scope("prototype")
				Jackson2ObjectMapperBuilder objectMapperBuilder) {
			ClassLoader classLoader = CustomOAuth2AuthorizationService.class.getClassLoader();
			ArrayList<Module> modules = new ArrayList<>();
			// Spring Authorization
			// Server,这个里面有限制SecurityJackson2Modules.enableDefaultTyping(context.getOwner());
			modules.add(new OAuth2AuthorizationServerJackson2Module());
			// Neiko Authorization Server
			modules.add(new AuthorizationServerJackson2Module());
			// Neiko Authorization Module
			modules.add(new NeikoAuthorizationJackson2Module());
			// Spring Security
			modules.addAll(SecurityJackson2Modules.getModules(classLoader));
			objectMapperBuilder.modules(modules::addAll);
			objectMapperBuilder.modules(modules);
			return new CustomOAuth2AuthorizationService(authorizationService, registeredClientRepository,
					objectMapperBuilder.build());
		}

	}

	/**
	 * Authorization Server Jwt
	 */
	@Configuration
	static class AuthorizationServerJwtConfiguration {

		@Bean
		public JWKSource<SecurityContext> jwkSource() {
			RSAKey rsaKey = Jwks.generateRsa();
			JWKSet jwkSet = new JWKSet(rsaKey);
			return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
		}

	}

}
