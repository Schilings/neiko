package com.schilings.neiko.security.oauth2.authorization.server;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@FunctionalInterface
public interface OAuth2AuthorizationServerInitializer {

	/**
	 * AuthorizationServer的SecurityFilterChain创建完成build之后进行的初始化
	 * @param httpSecurity
	 */
	void initialize(HttpSecurity httpSecurity);

}
