package com.schilings.neiko.admin.upms.config.satoken;

import cn.dev33.satoken.stp.StpInterface;
import com.schilings.neiko.extend.sa.token.handler.ExtendSaTokenHandlerExceptionResolver;
import com.schilings.neiko.auth.properties.AuthProperties;
import com.schilings.neiko.system.service.SysRoleMenuService;
import com.schilings.neiko.system.service.SysUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * <p>
 * Sa-Token配置
 * </p>
 *
 * @author Schilings
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class SaTokenConfiguration {

	private final AuthProperties authProperties;

	/**
	 * Sa-Token角色权限业务配置
	 * @param sysUserRoleService
	 * @param sysRoleMenuService
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public StpInterface stpInterfaceImpl(SysUserRoleService sysUserRoleService, SysRoleMenuService sysRoleMenuService) {
		return new StpInterfaceImpl(sysUserRoleService, sysRoleMenuService);
	}

	/**
	 * Sa-Token异常处理
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(ExtendSaTokenHandlerExceptionResolver.class)
	public ExtendSaTokenHandlerExceptionResolver authHandlerExceptionResolver() {
		return new ExtendSaTokenHandlerExceptionResolver();
	}

}
