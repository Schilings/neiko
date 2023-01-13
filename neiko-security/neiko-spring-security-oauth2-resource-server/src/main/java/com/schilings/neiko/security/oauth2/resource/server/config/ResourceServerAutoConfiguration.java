package com.schilings.neiko.security.oauth2.resource.server.config;

import com.schilings.neiko.security.oauth2.resource.server.customizer.DefaultOAuth2ResourceServerCustomizer;
import com.schilings.neiko.security.oauth2.resource.server.customizer.jwt.JwtOAuth2ResourceServerCustomizer;
import com.schilings.neiko.security.oauth2.resource.server.customizer.opaque.OpaqueOAuth2ResourceServerCustomizer;
import com.schilings.neiko.security.oauth2.resource.server.properties.ResourceServerProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@AutoConfiguration(before = OAuth2ResourceServerAutoConfiguration.class)
@EnableConfigurationProperties({ ResourceServerProperties.class })
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Import({ ResourceServerAutoConfiguration.DefaultCustomizerAutoConfiguration.class,
		ResourceServerConfiguration.OpaqueTokenConfiguration.class,
		ResourceServerConfiguration.JwtConfiguration.class })
public class ResourceServerAutoConfiguration {

	/**
	 * <p>
	 * </p>
	 *
	 * @author Schilings
	 */
	@Configuration(proxyBeanMethods = false)
	static class DefaultCustomizerAutoConfiguration {

		// @Bean
		// @ConditionalOnProperty(name = "123", havingValue = "123")
		// public JwtOAuth2ResourceServerCustomizer jwtOAuth2ResourceServerCustomizer() {
		// return new JwtOAuth2ResourceServerCustomizer();
		// }

		@Bean
		@ConditionalOnMissingBean({ JwtOAuth2ResourceServerCustomizer.class,
				OpaqueOAuth2ResourceServerCustomizer.class })
		public OpaqueOAuth2ResourceServerCustomizer opaqueOAuth2ResourceServerCustomizer() {
			return new OpaqueOAuth2ResourceServerCustomizer();
		}

		@Bean
		@ConditionalOnMissingBean
		public DefaultOAuth2ResourceServerCustomizer defaultOAuth2ResourceServerCustomizer(
				ResourceServerProperties resourceServerProperties) {
			return new DefaultOAuth2ResourceServerCustomizer(resourceServerProperties);
		}

	}

}
