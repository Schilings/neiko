package com.schilings.neiko.extend.sa.token.oauth2.introspector;


import cn.dev33.satoken.strategy.SaStrategy;
import com.schilings.neiko.common.security.constant.SecurityConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

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
        opaqueTokenIntrospector.introspect(request.getHeader(SecurityConstants.Param.access_token));
        SaStrategy.me.checkMethodAnnotation.accept(((HandlerMethod) handler).getMethod());
    }
}
