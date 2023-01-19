package com.schilings.neiko.extend.sa.token.oauth2.introspector;

import cn.dev33.satoken.strategy.SaStrategy;
import com.schilings.neiko.common.security.constant.SecurityConstants;
import com.schilings.neiko.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.method.HandlerMethod;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class OpaqueTokenIntrospectorWrapper {

	private final OpaqueTokenIntrospector opaqueTokenIntrospector;

	/**
	 * 注解鉴权的Token自省
	 * @param request
	 * @param response
	 * @param handler
	 */
	public void checkMethodAnnotation(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String header = request.getHeader(SecurityConstants.Param.access_token);
		if (StringUtils.isNotBlank(header)) {
			opaqueTokenIntrospector.introspect(header);
		}
		SaStrategy.me.checkMethodAnnotation.accept(((HandlerMethod) handler).getMethod());
	}

}
