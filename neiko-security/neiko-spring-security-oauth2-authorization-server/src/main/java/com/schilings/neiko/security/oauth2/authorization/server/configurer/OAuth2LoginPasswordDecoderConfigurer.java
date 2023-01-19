package com.schilings.neiko.security.oauth2.authorization.server.configurer;

import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerExtensionConfigurer;
import com.schilings.neiko.security.oauth2.authorization.server.filter.LoginPasswordDecoderFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.web.OAuth2ClientAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

/**
 *
 * <p>
 * 登陆时的密码解密配置
 * </p>
 *
 * @author Schilings
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 200)
public class OAuth2LoginPasswordDecoderConfigurer
		extends OAuth2AuthorizationServerExtensionConfigurer<OAuth2LoginPasswordDecoderConfigurer, HttpSecurity> {

	private final String passwordSecretKey;

	private RequestMatcher requestMatcher;

	public OAuth2LoginPasswordDecoderConfigurer(String passwordSecretKey) {
		Assert.hasText(passwordSecretKey, "passwordSecretKey can not be null");
		this.passwordSecretKey = passwordSecretKey;
	}

	@Override
	public void configure(HttpSecurity httpSecurity) throws Exception {
		// 获取授权服务器配置
		AuthorizationServerSettings authorizationServerSettings = httpSecurity
				.getSharedObject(AuthorizationServerSettings.class);
		// 只处理token endpoint
		this.requestMatcher = new AntPathRequestMatcher(authorizationServerSettings.getTokenEndpoint(),
				HttpMethod.POST.name());
		// 密码解密，必须在 OAuth2ClientAuthenticationFilter 过滤器之后，方便获取当前客户端
		httpSecurity.addFilterAfter(postProcess(new LoginPasswordDecoderFilter(requestMatcher, passwordSecretKey)),
				OAuth2ClientAuthenticationFilter.class);
	}

	protected void registerAuthenticationEntryPoint(HttpSecurity httpSecurity,
			AuthenticationEntryPoint authenticationEntryPoint) {
		ExceptionHandlingConfigurer<HttpSecurity> exceptionHandling = httpSecurity
				.getConfigurer(ExceptionHandlingConfigurer.class);
		if (exceptionHandling != null) {
			exceptionHandling.defaultAuthenticationEntryPointFor(postProcess(authenticationEntryPoint),
					new OrRequestMatcher(this.requestMatcher));
		}
	}

}
