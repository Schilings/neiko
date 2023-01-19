package com.schilings.neiko.extend.sa.token.bean;

import com.schilings.neiko.extend.sa.token.oauth2.introspector.OpaqueTokenIntrospectorWrapper;
import com.schilings.neiko.extend.sa.token.properties.OAuth2ResourceServerProperties;
import lombok.RequiredArgsConstructor;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * <p>
 * 注解式鉴权 - 拦截器
 * </p>
 *
 * @author Schilings
 */
@RequiredArgsConstructor
public class ExtendAnnotationInterceptor implements HandlerInterceptor {

	private final OAuth2ResourceServerProperties resourceServerProperties;

	private final OpaqueTokenIntrospectorWrapper introspectorWrapper;

	/**
	 * 每次请求之前触发的方法
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 获取处理method
		if (handler instanceof HandlerMethod == false) {
			return true;
		}
		// 跳过注解鉴权
		if (resourceServerProperties.isEnforceCancelAuthenticate()) {
			return true;
		}
		// TODO Token自省 进行验证
		introspectorWrapper.checkMethodAnnotation(request, response, handler);
		// 通过验证
		return true;

	}

}
