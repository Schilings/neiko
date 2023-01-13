package com.schilings.neiko.security.oauth2.resource.server.customizer;

import cn.hutool.core.util.ArrayUtil;
import com.schilings.neiko.security.oauth2.resource.server.OAuth2ResourceServerConfigurerCustomizer;
import com.schilings.neiko.security.oauth2.resource.server.properties.ResourceServerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.access.DelegatingAccessDeniedHandler;
import org.springframework.security.web.csrf.CsrfException;

import java.util.*;

@Order
@RequiredArgsConstructor
public class DefaultOAuth2ResourceServerCustomizer implements OAuth2ResourceServerConfigurerCustomizer {

	private final ResourceServerProperties resourceServerProperties;

	private ApplicationContext context;

	private BearerTokenResolver tokenResolver;

	private AuthenticationEntryPoint authenticationEntryPoint;

	private AccessDeniedHandler accessDeniedHandler;

	@Override
	public void customize(HttpSecurity http) throws Exception {
		// @formatter:off
        http
                // 拦截 url 配置
                .authorizeRequests()
                    .antMatchers(ArrayUtil.toArray(resourceServerProperties.getIgnoreUrls(), String.class)).permitAll()
                    .anyRequest().authenticated()

                // 关闭 csrf 跨站攻击防护
                .and().csrf().disable();
        // @formatter:on

		http.oauth2ResourceServer(resourceServerConfigurer -> {
			// TokenResolver
			// this.requestMatcher.setBearerTokenResolver(bearerTokenResolver);
			// OAuth2ResourceServerConfigurer的requestMatcher只用于配置AuthenticationEntryPoint和BearerTokenAuthenticationFilter
			// 不作为该SecurityFilterChain的匹配器之一
			resourceServerConfigurer.bearerTokenResolver(getBearerTokenResolver(http));
			// AuthenticationEntryPoint
			// RequestMatcher: registerDefaultEntryPoint()
			resourceServerConfigurer.authenticationEntryPoint(getAuthenticationEntryPoint(http));
			// AccessDeniedHandler
			// RequestMatcher: registerDefaultAccessDeniedHandler()
			// 虽然是根据RequestMatcher配置的AccessDeniedHandler,但如果只有一个，那么作为默认Handler
			// 可看ExceptionHandlingConfigurer.createDefaultDeniedHandler()
			resourceServerConfigurer.accessDeniedHandler(getAccessDeniedHandler(http));
		});

	}

	private ApplicationContext getApplicationContext(HttpSecurity http) {
		if (this.context != null) {
			return this.context;
		}
		return http.getSharedObject(ApplicationContext.class);
	}

	private BearerTokenResolver getBearerTokenResolver(HttpSecurity http) {
		if (this.tokenResolver != null) {
			return this.tokenResolver;
		}
		return getApplicationContext(http).getBean(BearerTokenResolver.class);
	}

	private AuthenticationEntryPoint getAuthenticationEntryPoint(HttpSecurity http) {
		if (this.authenticationEntryPoint != null) {
			return this.authenticationEntryPoint;
		}
		// 原本默认为BearerTokenAuthenticationEntryPoint
		return getApplicationContext(http).getBean(AuthenticationEntryPoint.class);
	}

	private AccessDeniedHandler getAccessDeniedHandler(HttpSecurity http) {
		if (this.accessDeniedHandler != null) {
			return this.accessDeniedHandler;
		}
		this.accessDeniedHandler = new DelegatingAccessDeniedHandler(new LinkedHashMap<>(createAccessDeniedHandlers()),
				// default Handler 原本默认为BearerTokenAccessDeniedHandler
				getApplicationContext(http).getBean(AccessDeniedHandler.class));
		return this.accessDeniedHandler;
	}

	private static Map<Class<? extends AccessDeniedException>, AccessDeniedHandler> createAccessDeniedHandlers() {
		Map<Class<? extends AccessDeniedException>, AccessDeniedHandler> handlers = new HashMap<>();
		handlers.put(CsrfException.class, new AccessDeniedHandlerImpl());
		return handlers;
	}

}
