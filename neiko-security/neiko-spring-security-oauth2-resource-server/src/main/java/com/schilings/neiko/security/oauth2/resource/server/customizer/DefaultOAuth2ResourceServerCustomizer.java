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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

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
                .authorizeHttpRequests()
					//如果是spring webmvc环境，默认是MvcRequestMatcher，手动改成AntPathRequestMatcher
					.requestMatchers(createAntPathRequestMatchers()).permitAll()
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

	private RequestMatcher[] createAntPathRequestMatchers() {
		List<RequestMatcher> matchers = new ArrayList<>();
		for (String pattern : resourceServerProperties.getIgnoreUrls()) {
			matchers.add(new AntPathRequestMatcher(pattern, null));
		}
		return ArrayUtil.toArray(matchers, RequestMatcher.class);
	}

	private ApplicationContext getApplicationContext(HttpSecurity http) {
		if (this.context != null) {
			return this.context;
		}
		this.context = http.getSharedObject(ApplicationContext.class);
		return this.context;
	}

	private BearerTokenResolver getBearerTokenResolver(HttpSecurity http) {
		if (this.tokenResolver != null) {
			return this.tokenResolver;
		}
		this.tokenResolver = getApplicationContext(http).getBean(BearerTokenResolver.class);
		return this.tokenResolver;
	}

	private AuthenticationEntryPoint getAuthenticationEntryPoint(HttpSecurity http) {
		if (this.authenticationEntryPoint != null) {
			return this.authenticationEntryPoint;
		}
		// 原本默认为BearerTokenAuthenticationEntryPoint
		this.authenticationEntryPoint = getApplicationContext(http).getBean(AuthenticationEntryPoint.class);
		return this.authenticationEntryPoint;
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
