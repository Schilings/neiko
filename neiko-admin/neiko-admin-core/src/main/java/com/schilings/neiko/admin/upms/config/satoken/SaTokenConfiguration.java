package com.schilings.neiko.admin.upms.config.satoken;

import cn.dev33.satoken.interceptor.SaAnnotationInterceptor;
import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.stp.StpInterface;
import com.schilings.neiko.auth.handler.AuthHandlerExceptionResolver;
import com.schilings.neiko.auth.properties.AuthProperties;
import com.schilings.neiko.extend.sa.token.holder.RBACAuthorityHolder;
import com.schilings.neiko.system.service.SysMenuService;
import com.schilings.neiko.system.service.SysRoleMenuService;
import com.schilings.neiko.system.service.SysUserRoleService;
import com.schilings.neiko.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
	 * RBAC权限缓存
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public RBACAuthorityHolder rbacAuthorityHolder() {
		return new RBACAuthorityHolder();
	}

	/**
	 * Sa-Token异常处理
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(AuthHandlerExceptionResolver.class)
	public AuthHandlerExceptionResolver authHandlerExceptionResolver() {
		return new AuthHandlerExceptionResolver();
	}

	@Configuration
	@RequiredArgsConstructor
	public class SaTokenConfigure implements WebMvcConfigurer {
		
		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			// 注册Sa-Token的注解拦截器，打开注解式鉴权功能，并排除不需要注解鉴权的接口地址 (与登录拦截器无关)
			registry.addInterceptor(new SaAnnotationInterceptor())
					.addPathPatterns("/**")
					.excludePathPatterns("/oauth2/**")
					.excludePathPatterns(authProperties.getIgnoreUrls());
			/// 注册 Sa-Token 的路由拦截器，自定义认证规则 
//			registry.addInterceptor(new SaRouteInterceptor())
//					.addPathPatterns("/**")
//					.excludePathPatterns("/oauth2/**")
//					.excludePathPatterns(authProperties.getIgnoreUrls());
			
		}

	}

}
