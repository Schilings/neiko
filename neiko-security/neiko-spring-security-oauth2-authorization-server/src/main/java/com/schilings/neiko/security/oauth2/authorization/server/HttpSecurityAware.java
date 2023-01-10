package com.schilings.neiko.security.oauth2.authorization.server;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 *
 * <p>
 * AuthorizationServerConfigurer 作用的HttpSecurity Aware
 * </p>
 *
 * @author Schilings
 */
public interface HttpSecurityAware {

	/**
	 * 注意：该方法被调用时，AuthorizationServer的SecurityFilterChain已经创建完成
	 * @param httpSecurity
	 */
	void setHttpSecurity(HttpSecurity httpSecurity);

}
