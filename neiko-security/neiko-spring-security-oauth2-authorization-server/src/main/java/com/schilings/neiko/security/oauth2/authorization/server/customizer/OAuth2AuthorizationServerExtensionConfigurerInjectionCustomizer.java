package com.schilings.neiko.security.oauth2.authorization.server.customizer;

import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerExtensionConfigurer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Order(Ordered.LOWEST_PRECEDENCE) // 优先级最低，最慢加载
public class OAuth2AuthorizationServerExtensionConfigurerInjectionCustomizer
		implements OAuth2AuthorizationServerConfigurerCustomizer {

	private final List<OAuth2AuthorizationServerExtensionConfigurer> extensionConfigurers;

	public OAuth2AuthorizationServerExtensionConfigurerInjectionCustomizer(
			List<OAuth2AuthorizationServerExtensionConfigurer> extensionConfigurers) {
		this.extensionConfigurers = extensionConfigurers;
	}

	@Override
	public void customize(OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer,
			HttpSecurity httpSecurity) throws Exception {
		if (!CollectionUtils.isEmpty(this.extensionConfigurers)) {
			AnnotationAwareOrderComparator.sort(this.extensionConfigurers);
			for (OAuth2AuthorizationServerExtensionConfigurer configurer : this.extensionConfigurers) {
				httpSecurity.apply(configurer);
			}
		}
	}

}
