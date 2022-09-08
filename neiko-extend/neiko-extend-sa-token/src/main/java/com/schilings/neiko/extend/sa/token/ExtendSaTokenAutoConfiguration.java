package com.schilings.neiko.extend.sa.token;

import cn.dev33.satoken.dao.SaTokenDao;
import com.schilings.neiko.common.redis.RedisHelper;
import com.schilings.neiko.extend.sa.token.core.RedisSaTokenDao;
import com.schilings.neiko.extend.sa.token.holder.RBACAuthorityHolder;
import com.schilings.neiko.extend.sa.token.oauth2.event.AuthorityEventListener;
import com.schilings.neiko.extend.sa.token.properties.ExtendSaTokenProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * <p>
 * </p>
 *
 * @author Schilings
 */
@Slf4j
@AutoConfiguration
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ExtendSaTokenProperties.class)
public class ExtendSaTokenAutoConfiguration {


	/**
	 * 处理权限变更等事件的事件监听器
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public AuthorityEventListener authorityEventListener() {
		return new AuthorityEventListener();
	}

	/**
	 * RBAC权限缓存
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public RBACAuthorityHolder rbacAuthorityHolder() {
		return new RBACAuthorityHolder();
	}

	
	/**
	 * Sa-Token自定义Redis实现Dao
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(RedisHelper.class)
	public SaTokenDao redisSaTokenDao() {
		return new RedisSaTokenDao();
	}


}
