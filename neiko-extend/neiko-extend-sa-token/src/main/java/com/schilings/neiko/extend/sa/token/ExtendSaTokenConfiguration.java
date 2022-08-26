package com.schilings.neiko.extend.sa.token;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.oauth2.config.SaOAuth2Config;
import com.schilings.neiko.extend.sa.token.holder.RBACAuthorityHolder;
import com.schilings.neiko.extend.sa.token.core.RedisSaTokenDao;
import com.schilings.neiko.extend.sa.token.oauth2.Oauth2RequestBody;
import com.schilings.neiko.extend.sa.token.oauth2.component.LoginService;
import com.schilings.neiko.extend.sa.token.oauth2.filter.ExtendOauth2RequestFilter;
import com.schilings.neiko.extend.sa.token.properties.ExtendSaTokenProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *
 * <p>
 * </p>
 *
 * @author Schilings
 */
@Slf4j
@RequiredArgsConstructor
@Import(ExtendAnnotationConfiguration.class)
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ExtendSaTokenProperties.class)
public class ExtendSaTokenConfiguration {

	private final ExtendSaTokenProperties extendSaTokenProperties;

	private final LoginService loginService;

	/**
	 * 以代码的方式配置Sa-Token-Config
	 * @param config
	 */
	@Autowired
	public void setSaTokenConfig(SaTokenConfig config) {
		// 限制只能从Header上读取token
		config.setIsReadCookie(false);
		config.setIsReadBody(false);
		config.setIsReadHead(true);
		// 限制token生成策略
		config.setTokenStyle("random-64");

	}

	/**
	 * Sa-OAuth2 定制化配置
	 * @param cfg
	 */
	@Autowired
	public void setSaOAuth2Config(SaOAuth2Config cfg) {
		// 提供的oauth2认证流程，暂只适用password和refresh_token
		cfg.setIsCode(false);
		cfg.setIsPassword(true);
		cfg.setIsNewRefresh(true);
		cfg.setIsImplicit(false);
		cfg.setIsClient(false);
		// LoginService
		cfg.setDoLoginHandle(loginService::passwordLogin);// 登录处理函数
	}

	/**
	 * Sa-Token自定义Redis实现Dao
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public SaTokenDao redisSaTokenDao() {
		return new RedisSaTokenDao();
	}

	/**
	 * 对Oauth2请求的过滤器
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(ExtendOauth2RequestFilter.class)
	@ConditionalOnProperty(name = "neiko.sa-token.enforce-json-filter", havingValue = "true")
	public FilterRegistrationBean<ExtendOauth2RequestFilter> extendFilterRegistrationBean() {
		FilterRegistrationBean<ExtendOauth2RequestFilter> registrationBean = new FilterRegistrationBean<>(
				new ExtendOauth2RequestFilter());
		registrationBean.addUrlPatterns("/oauth2/*");
		return registrationBean;
	}

}
