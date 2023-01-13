package com.schilings.neiko.security.oauth2.authorization.server;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;

/**
 *
 * <p>
 * 授权服务器配置的自定义器
 * </p>
 *
 * @author Schilings
 */

public interface OAuth2AuthorizationServerConfigurerCustomizer {

	void customize(OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer, HttpSecurity httpSecurity)
			throws Exception;
}
