package com.schilings.neiko.admin.upms;

import com.schilings.neiko.admin.upms.log.LogConfiguration;
import com.schilings.neiko.authorization.common.properties.SecurityProperties;
import com.schilings.neiko.common.core.snowflake.SnowflakeIdGenerator;
import com.schilings.neiko.system.authentication.DefaultUserInfoCoordinatorImpl;
import com.schilings.neiko.system.authentication.SysUserDetailsServiceImpl;
import com.schilings.neiko.system.authentication.UserInfoCoordinator;
import com.schilings.neiko.system.properties.SystemProperties;
import com.schilings.neiko.system.service.SysUserService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

@MapperScan({ "com.schilings.neiko.**.mapper" })
@ComponentScan({ "com.schilings.neiko.admin.upms", "com.schilings.neiko.file", "com.schilings.neiko.system",
		"com.schilings.neiko.log", "com.schilings.neiko.notify", "com.schilings.neiko.authorization" })
@EnableAsync
@AutoConfiguration
@Import(LogConfiguration.class)
@EnableConfigurationProperties({ SystemProperties.class, SecurityProperties.class })
public class UpmsAutoConfiguration {

	/**
	 * 雪花ID生成器
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public SnowflakeIdGenerator snowflakeIdGenerator() {
		return new SnowflakeIdGenerator();
	}

	/**
	 * 用户详情处理类
	 *
	 */
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(SysUserService.class)
	@ConditionalOnMissingBean(UserDetailsService.class)
	static class UserDetailsServiceConfiguration {

		/**
		 * 用户详情处理类
		 * @return SysUserDetailsServiceImpl
		 */
		@Bean
		@ConditionalOnMissingBean
		public UserDetailsService userDetailsService(SysUserService sysUserService,
				ObjectProvider<List<UserInfoCoordinator>> userInfoCoordinator) {
			return new SysUserDetailsServiceImpl(sysUserService, userInfoCoordinator);
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

}
