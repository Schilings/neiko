package com.schilings.neiko.authorization.biz.configuration;

import com.schilings.neiko.authorization.biz.component.CustomPermissionEvaluator;
import com.schilings.neiko.security.util.PasswordUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class DefaultSecurityConfiguration {

	/**
	 * 密码解析器，只在授权服务器中进行配置
	 * @return BCryptPasswordEncoder
	 */
	@Bean
	@ConditionalOnMissingBean
	protected PasswordEncoder passwordEncoder() {
		return PasswordUtils.createDelegatingPasswordEncoder();
	}

	/**
	 * 自定义的权限字符串鉴定组件
	 * @return
	 */
	@Bean("per")
	@ConditionalOnMissingBean
	public CustomPermissionEvaluator permissionEvaluator() {
		return new CustomPermissionEvaluator();
	}

}
