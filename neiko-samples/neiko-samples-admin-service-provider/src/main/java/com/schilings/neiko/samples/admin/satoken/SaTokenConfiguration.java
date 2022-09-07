package com.schilings.neiko.samples.admin.satoken;

import cn.dev33.satoken.interceptor.SaAnnotationInterceptor;



import lombok.RequiredArgsConstructor;

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
	
	
	@Configuration
	@RequiredArgsConstructor
	public class SaTokenConfigure implements WebMvcConfigurer {

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			// 注册Sa-Token的注解拦截器，打开注解式鉴权功能，并排除不需要注解鉴权的接口地址 (与登录拦截器无关)
			registry.addInterceptor(new SaAnnotationInterceptor()).addPathPatterns("/**")
					.excludePathPatterns("/oauth2/**");
			/// 注册 Sa-Token 的路由拦截器，自定义认证规则
			// registry.addInterceptor(new SaRouteInterceptor())
			// .addPathPatterns("/**")
			// .excludePathPatterns("/oauth2/**")
			// .excludePathPatterns(authProperties.getIgnoreUrls());
		}

	}

}
