package com.schilings.neiko.autoconfigure.shiro;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.schilings.neiko.autoconfigure.shiro.filter.JWTAuthenticationFilter;
import com.schilings.neiko.autoconfigure.shiro.reaml.JWTRealm;
import com.schilings.neiko.autoconfigure.shiro.token.JWTRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ken-Chy129
 * @date 2022/8/3 10:01
 */
@Slf4j
@RequiredArgsConstructor
@ConditionalOnWebApplication
@EnableConfigurationProperties(ShiroJWTProperties.class)
public class DefaultJWTAutoConfiguration {
    
    private final ShiroJWTProperties shiroJWTProperties;
    
    private final RedisProperties redisProperties;
    
    @Bean
    @ConditionalOnMissingBean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager) throws NoSuchBeanDefinitionException {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        
        Map<String, Filter> filterMap = new HashMap<>();
        JWTRepository jwtRepository = SpringUtil.getBean(JWTRepository.class);
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(jwtRepository);
        filterMap.put("jwt", jwtAuthenticationFilter);
        shiroFilterFactoryBean.setFilters(filterMap);
        
        Map<String,String> map = new LinkedHashMap<>();
        ShiroJWTProperties.JWTFilter jwtFilter = shiroJWTProperties.getFilter();
        if (ObjectUtil.isNull(jwtFilter)) {
            jwtFilter = new ShiroJWTProperties.JWTFilter();
        }
        jwtFilter.getAuthcUrlList().forEach(url -> map.put(url, "authc"));
        jwtFilter.getAnonUrlList().forEach(url -> map.put(url, "anon"));
        jwtFilter.getJwtList().forEach(url -> map.put(url, "jwt"));
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        shiroFilterFactoryBean.setLoginUrl(jwtFilter.getLoginUrl());
        shiroFilterFactoryBean.setSuccessUrl(jwtFilter.getSuccessUrl());
        shiroFilterFactoryBean.setUnauthorizedUrl(jwtFilter.getUnauthorizedUrl());
        return shiroFilterFactoryBean;
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultWebSecurityManager getDefaultWebSecurityManager() throws NoSuchBeanDefinitionException {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        
        JWTRealm realm = SpringUtil.getBean(JWTRealm.class);
        defaultWebSecurityManager.setRealm(realm);
        
        // 如果开启redis缓存，则配置缓存管理器和使用redis自定义session管理
        ShiroJWTProperties.Redis redis = shiroJWTProperties.getRedis();
        if (ObjectUtil.isNull(redis) ) {
            redis = new ShiroJWTProperties.Redis();
        }
        if (redis.isEnabled()) {
            defaultWebSecurityManager.setCacheManager(redisCacheManager());
        }
        
        // 关闭session
        DefaultSubjectDAO defaultSubjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        defaultSubjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator);
        defaultWebSecurityManager.setSubjectDAO(defaultSubjectDAO);
        
        return defaultWebSecurityManager;
    }
    
    public RedisCacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        ShiroJWTProperties.Redis redis = shiroJWTProperties.getRedis();
        if (ObjectUtil.isNull(redis)) {
            redis = new ShiroJWTProperties.Redis();
        }
        redisCacheManager.setExpire(redis.getExpired());
        redisCacheManager.setKeyPrefix(redis.getKeyPrefix());
        return redisCacheManager;
    }
    
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisProperties.getHost() + ":" + redisProperties.getPort());
        redisManager.setDatabase(redisProperties.getDatabase());
        redisManager.setPassword(redisProperties.getPassword());
        if (ObjectUtil.isNotNull(redisProperties.getTimeout())) {
            redisManager.setTimeout((int)redisProperties.getTimeout().toMillis());
        }
        return redisManager;
    }
    
    @Deprecated
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        sessionManager.setSessionDAO(redisSessionDAO);
        return sessionManager;
    }
    
    /**
     * 授权所用配置<br>
     * setUsePrefix(false)用于解决一个奇怪的bug。在引入spring aop的情况下。
     * 在@Controller注解的类的方法中加入@RequiresRole等shiro注解，会导致该方法无法映射请求，导致返回404。
     * 加入这项配置能解决这个bug
     */
    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return defaultAdvisorAutoProxyCreator;
    }

}
