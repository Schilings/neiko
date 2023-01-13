package com.schilings.neiko.security.oauth2.resource.server;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 *
 * <p>
 * 资源服务器配置自定义器
 * </p>
 *
 * @author Schilings
 */
public interface OAuth2ResourceServerConfigurerCustomizer {

	void customize(HttpSecurity httpSecurity) throws Exception;

}
