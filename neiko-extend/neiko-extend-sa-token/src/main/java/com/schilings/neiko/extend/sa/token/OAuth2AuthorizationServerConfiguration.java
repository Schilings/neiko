package com.schilings.neiko.extend.sa.token;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.oauth2.config.SaOAuth2Config;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Template;
import com.schilings.neiko.common.security.constant.SecurityConstants;
import com.schilings.neiko.extend.sa.token.bean.ExtendSaOAuth2Template;
import com.schilings.neiko.extend.sa.token.bean.ExtendSaTokenListener;
import com.schilings.neiko.extend.sa.token.holder.ExtendComponentHolder;
import com.schilings.neiko.extend.sa.token.oauth2.component.*;
import com.schilings.neiko.extend.sa.token.oauth2.filter.ExtendOAuth2RequestFilter;
import com.schilings.neiko.extend.sa.token.properties.OAuth2AuthorizationServerProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.List;

/**
 *
 * <p>
 * </p>
 *
 * @author Schilings
 */
@Slf4j
@RequiredArgsConstructor
@ConditionalOnSaTokenEnabled
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(OAuth2AuthorizationServerProperties.class)
public class OAuth2AuthorizationServerConfiguration {

	private final OAuth2AuthorizationServerProperties authorizationServerProperties;

	private final LoginService loginService;

	private final UserDetailsService<?> userDetailsService;

	private final ClientDetailsService<?> clientDetailsService;

	public final ObjectProvider<List<TokenEnhancer>> tokenEnhancersProvider;

	public final ObjectProvider<List<OAuth2Granter>> oAuth2GrantersProvider;

	private final SaOAuth2Config saOAuth2Config;

	@PostConstruct
	public void initSaTokenConfig() {
		// =======OAuth2
		// 提供的oauth2认证流程，暂只适用password和refresh_token
		saOAuth2Config.setIsCode(authorizationServerProperties.getSaOAuth2Config().isCode);
		saOAuth2Config.setIsPassword(authorizationServerProperties.getSaOAuth2Config().isPassword);
		saOAuth2Config.setIsNewRefresh(authorizationServerProperties.getSaOAuth2Config().isNewRefresh);
		saOAuth2Config.setIsImplicit(authorizationServerProperties.getSaOAuth2Config().isImplicit);
		saOAuth2Config.setIsClient(authorizationServerProperties.getSaOAuth2Config().isClient);
		saOAuth2Config.setCodeTimeout(authorizationServerProperties.getSaOAuth2Config().codeTimeout);
		saOAuth2Config.setAccessTokenTimeout(authorizationServerProperties.getSaOAuth2Config().accessTokenTimeout);
		saOAuth2Config.setRefreshTokenTimeout(authorizationServerProperties.getSaOAuth2Config().refreshTokenTimeout);
		saOAuth2Config.setRefreshTokenTimeout(authorizationServerProperties.getSaOAuth2Config().clientTokenTimeout);
		saOAuth2Config
				.setPastClientTokenTimeout(authorizationServerProperties.getSaOAuth2Config().pastClientTokenTimeout);
		// LoginService
		saOAuth2Config.setDoLoginHandle(loginService::login);// 登录处理函数
	}

	@PostConstruct
	public void initExtendComponentHolder() {
		ExtendComponentHolder.setTokenEnhancersProvider(tokenEnhancersProvider);
		ExtendComponentHolder.setOAuth2GrantersProvider(oAuth2GrantersProvider);
		ExtendComponentHolder.setLoginService(loginService);
		ExtendComponentHolder.setUserDetailsService(userDetailsService);
		ExtendComponentHolder.setClientDetailsService(clientDetailsService);

	}

	/**
	 * 对Oauth2请求的过滤器
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(ExtendOAuth2RequestFilter.class)
	@ConditionalOnProperty(name = "neiko.sa-token.oauth2.authorization-server.enforce-json-filter",
			havingValue = "true")
	public FilterRegistrationBean<ExtendOAuth2RequestFilter> extendFilterRegistrationBean() {
		FilterRegistrationBean<ExtendOAuth2RequestFilter> registrationBean = new FilterRegistrationBean<>(
				new ExtendOAuth2RequestFilter());
		registrationBean.addUrlPatterns("/oauth2/*");
		return registrationBean;
	}

	/**
	 * Sa-Token 侦听器
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public SaTokenListener extendTokenListener() {
		return new ExtendSaTokenListener();
	}

}
