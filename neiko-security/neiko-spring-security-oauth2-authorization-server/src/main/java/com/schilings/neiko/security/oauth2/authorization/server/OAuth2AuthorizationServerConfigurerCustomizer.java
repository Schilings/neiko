package com.schilings.neiko.security.oauth2.authorization.server;

import cn.hutool.core.util.ArrayUtil;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * <p>
 * 授权服务器配置的自定义器
 * </p>
 *
 * @author Schilings
 */

public interface OAuth2AuthorizationServerConfigurerCustomizer {

	void customize(OAuth2AuthorizationServerConfigurer oAuth2AuthorizationServerConfigurer, HttpSecurity httpSecurity)
			throws Exception;

	default List<RequestMatcher> createAntMatcherList(String... patterns) {
		List<RequestMatcher> matchers = new ArrayList<>(patterns.length);
		for (String pattern : patterns) {
			matchers.add(new AntPathRequestMatcher(pattern));
		}
		return matchers;
	}

	default RequestMatcher[] createAntMatcherArray(String... patterns) {
		return ArrayUtil.toArray(createAntMatcherList(patterns), RequestMatcher.class);
	}

}
