package com.schilings.neiko.auth.handler;

import com.schilings.neiko.auth.properties.AuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@RequiredArgsConstructor
public class SaTokenInterceptorsRegistration implements InterceptorsRegistration {

	private final AuthProperties authProperties;

	@Override
	public void register(InterceptorRegistry registry) {
		// // 注册Sa-Token的注解拦截器，打开注解式鉴权功能，并排除不需要注解鉴权的接口地址 (与登录拦截器无关)
		// registry.addInterceptor(new SaAnnotationInterceptor())
		// .addPathPatterns("/**")
		// .excludePathPatterns(authProperties.getIgnoreUrls());
		// /// 注册 Sa-Token 的路由拦截器，自定义认证规则
		// registry.addInterceptor(new SaRouteInterceptor())
		// .addPathPatterns("/**")
		// .excludePathPatterns(authProperties.getIgnoreUrls());
	}

}
