package com.schilings.neiko.autoconfigure.shiro.realm;

import cn.hutool.core.util.ObjectUtil;
import com.schilings.neiko.autoconfigure.shiro.ShiroJWTProperties;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

/**
 * <pre>
 * <p></p>
 * </pre>
 *
 * @author <a href="https://github.com/kil1ua">Ken-Chy129</a>
 * @date 2022/8/13 20:45
 */
@RequiredArgsConstructor
public class RealmInterceptor implements BeanPostProcessor {
    
    private final ShiroJWTProperties shiroJWTProperties;
    
    private final RedisProperties redisProperties;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof JWTRealm) {
            JWTRealm jwtRealm = (JWTRealm) bean;
            ShiroJWTProperties.Redis redis = shiroJWTProperties.getRedis();
            if (ObjectUtil.isNull(redis)) {
                redis = new ShiroJWTProperties.Redis();
            }
            if (redis.isEnabled()) {
                jwtRealm.setCacheManager(redisCacheManager());
                jwtRealm.setCachingEnabled(true);
                if (redis.isAuthenticationCachingEnabled()) {
                    redis.setAuthenticationCachingEnabled(true);
                    jwtRealm.setAuthenticationCacheName(redis.getAuthenticationCacheName());
                }
                if (redis.isAuthorizationCachingEnabled()) {
                    redis.setAuthorizationCachingEnabled(true);
                    redis.setAuthorizationCacheName(redis.getAuthorizationCacheName());
                }
            }
            System.out.println(jwtRealm);
            return jwtRealm;
        }
        return bean;
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
}
