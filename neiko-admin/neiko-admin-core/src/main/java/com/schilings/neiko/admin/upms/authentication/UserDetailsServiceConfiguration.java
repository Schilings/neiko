package com.schilings.neiko.admin.upms.authentication;

import com.schilings.neiko.authorization.biz.federated.OAuth2UserService;
import com.schilings.neiko.system.service.SysUserService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 用户详情处理类
 *
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(SysUserService.class)
@ConditionalOnMissingBean(UserDetailsService.class)
public class UserDetailsServiceConfiguration {

	/**
	 * UserDetail处理类
	 * @return SysUserDetailsServiceImpl
	 */
	@Bean
	@ConditionalOnMissingBean
	public UserDetailsService userDetailsService(SysUserService sysUserService,
			ObjectProvider<UserInfoCoordinator> userInfoCoordinators) {
		return new SysUserDetailsServiceImpl(sysUserService, userInfoCoordinators);
	}

	/**
	 * OAuth2User处理类
	 * @return SysUserDetailsServiceImpl
	 */
	@Bean
	@ConditionalOnMissingBean
	public OAuth2UserService oAuth2UserService(SysUserService sysUserService,
			ObjectProvider<UserInfoCoordinator> userInfoCoordinators) {
		return new SysOAuth2UserServiceImpl(sysUserService, userInfoCoordinators);
	}

	/**
	 * 用户信息协调者
	 * @return UserInfoCoordinator
	 */
	@Bean
	@ConditionalOnMissingBean
	public UserInfoCoordinator userInfoCoordinator() {
		return new DefaultUserInfoCoordinatorImpl();
	}

}
