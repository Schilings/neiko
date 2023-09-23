package com.schilings.neiko.security.oauth2.authorization.server.configurer;

import cn.hutool.core.lang.Assert;
import com.schilings.neiko.security.captcha.CaptchaValidator;
import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerExtensionConfigurer;
import com.schilings.neiko.security.oauth2.authorization.server.filter.LoginCaptchaFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.web.OAuth2ClientAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 登录验证码校验
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 300)
public class OAuth2LoginCaptchaConfigurer
		extends OAuth2AuthorizationServerExtensionConfigurer<OAuth2LoginCaptchaConfigurer, HttpSecurity> {

	private final CaptchaValidator captchaValidator;

	public OAuth2LoginCaptchaConfigurer(CaptchaValidator captchaValidator) {
		Assert.notNull(captchaValidator, "captchaValidator can not be null");
		this.captchaValidator = captchaValidator;
	}

	@Override
	public void configure(HttpSecurity httpSecurity) {
		// 获取授权服务器配置
		AuthorizationServerSettings authorizationServerSettings = httpSecurity
				.getSharedObject(AuthorizationServerSettings.class);

		// 只处理登录接口
		AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher(authorizationServerSettings.getTokenEndpoint(),
				HttpMethod.POST.name());

		// 验证码，必须在 OAuth2ClientAuthenticationFilter 过滤器之后，方便获取当前客户端
		httpSecurity.addFilterAfter(new LoginCaptchaFilter(requestMatcher, captchaValidator),
				OAuth2ClientAuthenticationFilter.class);
	}

}
