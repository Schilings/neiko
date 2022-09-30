package com.schilings.neiko.extend.sa.token;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Template;
import cn.dev33.satoken.spring.oauth2.SaOAuth2BeanInject;
import cn.dev33.satoken.spring.oauth2.SaOAuth2BeanRegister;
import com.schilings.neiko.common.redis.RedisHelper;
import com.schilings.neiko.extend.sa.token.bean.ExtendSaOAuth2Template;
import com.schilings.neiko.extend.sa.token.core.RedisSaTokenDao;
import com.schilings.neiko.extend.sa.token.holder.RBACAuthorityHolder;
import com.schilings.neiko.extend.sa.token.oauth2.event.AuthorityEventListener;
import com.schilings.neiko.extend.sa.token.properties.ExtendSaTokenProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * <p>
 * </p>
 *
 * @author Schilings
 */
@Slf4j
@AutoConfiguration
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnSaTokenEnabled
@EnableConfigurationProperties(ExtendSaTokenProperties.class)
public class ExtendSaTokenOAuth2AutoConfiguration {

	private final ExtendSaTokenProperties saTokenProperties;

	@Bean
	@Primary
	public SaTokenConfig saTokenConfig() {
		SaTokenConfig saTokenConfig = new SaTokenConfig();
		// ========Sa-Token
		saTokenConfig.setIsLog(saTokenProperties.getSaTokenConfig().getIsLog());
		saTokenConfig.setIsPrint(saTokenProperties.getSaTokenConfig().getIsPrint());
		saTokenConfig.setTokenPrefix(saTokenProperties.getSaTokenConfig().getTokenPrefix());

		// Resource
		saTokenConfig.setTokenName(saTokenProperties.getSaTokenConfig().getTokenName());
		saTokenConfig.setIsReadCookie(saTokenProperties.getSaTokenConfig().getIsReadCookie());
		saTokenConfig.setIsReadBody(saTokenProperties.getSaTokenConfig().getIsReadBody());
		saTokenConfig.setIsReadHead(saTokenProperties.getSaTokenConfig().getIsReadHead());
		saTokenConfig.setBasic(saTokenProperties.getSaTokenConfig().getBasic());
		saTokenConfig.setCurrDomain(saTokenProperties.getSaTokenConfig().getCurrDomain());

		// Authorization
		saTokenConfig.setTokenStyle(saTokenProperties.getSaTokenConfig().getTokenStyle());
		saTokenConfig.setTimeout(saTokenProperties.getSaTokenConfig().getTimeout());
		saTokenConfig.setActivityTimeout(saTokenProperties.getSaTokenConfig().getActivityTimeout());
		saTokenConfig.setIsConcurrent(saTokenProperties.getSaTokenConfig().getIsConcurrent());
		saTokenConfig.setIsShare(saTokenProperties.getSaTokenConfig().getIsShare());
		saTokenConfig.setAutoRenew(saTokenProperties.getSaTokenConfig().getAutoRenew());
		saTokenConfig.setMaxLoginCount(saTokenProperties.getSaTokenConfig().getMaxLoginCount());
		saTokenConfig.setDataRefreshPeriod(saTokenProperties.getSaTokenConfig().getDataRefreshPeriod());
		saTokenConfig.setTokenSessionCheckLogin(saTokenProperties.getSaTokenConfig().getTokenSessionCheckLogin());

		return saTokenConfig;
	}

	/**
	 * Sa-Token-OAuth2 模块
	 *
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public SaOAuth2Template extendSaOAuth2Template() {
		return new ExtendSaOAuth2Template();
	}

	/**
	 * 处理权限变更等事件的事件监听器
	 *
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public AuthorityEventListener authorityEventListener() {
		return new AuthorityEventListener();
	}

	/**
	 * RBAC权限缓存
	 *
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public RBACAuthorityHolder rbacAuthorityHolder() {
		return new RBACAuthorityHolder();
	}

	/**
	 * Sa-Token自定义Redis实现Dao
	 *
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(RedisHelper.class)
	public SaTokenDao redisSaTokenDao() {
		return new RedisSaTokenDao();
	}

}
