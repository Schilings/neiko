package com.schilings.neiko.security.oauth2.resource.server;

import cn.hutool.core.util.ArrayUtil;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * <p>
 * 资源服务器配置自定义器
 * </p>
 *
 * @author Schilings
 */
public interface OAuth2ResourceServerConfigurerCustomizer {

	void customize(HttpSecurity httpSecurity) throws Exception;

	default RequestMatcher[] createAntMatchers(String... patterns) {
		List<RequestMatcher> matchers = new ArrayList<>(patterns.length);
		for (String pattern : patterns) {
			matchers.add(new AntPathRequestMatcher(pattern));
		}
		return ArrayUtil.toArray(matchers, RequestMatcher.class);
	}
	
}
