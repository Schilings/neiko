package com.schilings.neiko.security.oauth2.resource.server.config;

import com.schilings.neiko.security.oauth2.resource.server.customizer.DefaultOAuth2ResourceServerCustomizer;
import com.schilings.neiko.security.oauth2.resource.server.customizer.jwt.JwtOAuth2ResourceServerCustomizer;
import com.schilings.neiko.security.oauth2.resource.server.customizer.opaque.OpaqueTokenOAuth2ResourceServerCustomizer;
import com.schilings.neiko.security.oauth2.resource.server.exception.DefaultAccessDeniedHandler;
import com.schilings.neiko.security.oauth2.resource.server.exception.DefaultAuthenticationEntryPoint;
import com.schilings.neiko.security.oauth2.resource.server.properties.ResourceServerProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.util.HashMap;
import java.util.List;

@AutoConfiguration(before = OAuth2ResourceServerAutoConfiguration.class)
@EnableConfigurationProperties({ ResourceServerProperties.class })
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnBean(ResourceServerConfigurationAdapter.class)
@Import({ ResourceServerAutoConfiguration.DefaultCustomizerAutoConfiguration.class,
		ResourceServerConfiguration.OpaqueTokenConfiguration.class,
		ResourceServerConfiguration.JwtConfiguration.class })
public class ResourceServerAutoConfiguration {

	/**
	 * BearTokenResolve 允许使用 url 传参，方便 ws 连接 ps: 使用 url 传参不安全，待改进
	 * @return DefaultBearerTokenResolver
	 */
	@Bean
	@ConditionalOnMissingBean
	public BearerTokenResolver defaultBearerTokenResolver() {
		DefaultBearerTokenResolver defaultBearerTokenResolver = new DefaultBearerTokenResolver();
		defaultBearerTokenResolver.setAllowUriQueryParameter(true);
		return defaultBearerTokenResolver;
	}

	/**
	 * 自定义认证异常处理
	 * @return AuthenticationEntryPoint
	 */
	@Bean
	@ConditionalOnMissingBean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new DefaultAuthenticationEntryPoint();
	}

	/**
	 * 自定义授权异常处理
	 * @return AuthenticationEntryPoint
	 */
	@Bean
	@ConditionalOnMissingBean
	public AccessDeniedHandler accessDeniedHandler() {
		return new DefaultAccessDeniedHandler();
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @author Schilings
	 */
	@Configuration(proxyBeanMethods = false)
	static class DefaultCustomizerAutoConfiguration {

		/**
		 * 如果有自定义JwtEncodder ，那么使用JWT
		 * @return
		 */
		@Bean
		@Conditional(JwtDecoderCondition.class)
		@ConditionalOnMissingBean(JwtOAuth2ResourceServerCustomizer.class)
		public JwtOAuth2ResourceServerCustomizer jwtOAuth2ResourceServerCustomizer() {
			return new JwtOAuth2ResourceServerCustomizer();
		}

		/**
		 * 如果没有自定义使用Jwt或者OpaqueToken，那么使用OpaqueToken
		 * @return
		 */
		@Bean
		@ConditionalOnMissingBean({ JwtOAuth2ResourceServerCustomizer.class,
				OpaqueTokenOAuth2ResourceServerCustomizer.class })
		public OpaqueTokenOAuth2ResourceServerCustomizer opaqueTokenOAuth2ResourceServerCustomizer() {
			return new OpaqueTokenOAuth2ResourceServerCustomizer();
		}

		/**
		 * 默认的资源服务端定义
		 * @param resourceServerProperties
		 * @return
		 */
		@Bean
		@ConditionalOnMissingBean(DefaultOAuth2ResourceServerCustomizer.class)
		public DefaultOAuth2ResourceServerCustomizer defaultOAuth2ResourceServerCustomizer(
				ResourceServerProperties resourceServerProperties) {
			return new DefaultOAuth2ResourceServerCustomizer(resourceServerProperties);
		}

	}

}
