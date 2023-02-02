package com.schilings.neiko.security.oauth2.authorization.server.autoconfigure;

import com.schilings.neiko.security.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 *
 * 授权管理器配置类
 *
 */

@AutoConfiguration
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class AuthenticationManagerAutoConfiguration {

	private final List<AuthenticationProvider> authenticationProviders;

	/**
	 * 显式注入授权管理器，password 模式时必须要提供 防止AuthenticationProvider的Bean破坏原本的默认配置
	 */
	@Bean
	public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
			ObjectProvider<PasswordEncoder> passwordEncoderProvider,
			AuthenticationConfiguration authenticationConfiguration, ApplicationContext applicationContext)
			throws Exception {

		AuthenticationManagerBuilder authBuilder = applicationContext.getBean(AuthenticationManagerBuilder.class);

		// 添加多种授权模式
		for (AuthenticationProvider authenticationProvider : authenticationProviders) {
			authBuilder.authenticationProvider(authenticationProvider);
		}
		// 注册 DaoAuthenticationProvider
		if (userDetailsService != null) {
			authBuilder.userDetailsService(userDetailsService).passwordEncoder(
					passwordEncoderProvider.getIfAvailable(PasswordUtils::createDelegatingPasswordEncoder));
		}
		return authenticationConfiguration.getAuthenticationManager();
	}

}
