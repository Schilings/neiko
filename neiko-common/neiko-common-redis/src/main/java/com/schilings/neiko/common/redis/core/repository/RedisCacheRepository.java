package com.schilings.neiko.common.redis.core.repository;


import com.schilings.neiko.common.cache.components.AbstractCacheRepository;
import com.schilings.neiko.common.cache.components.CacheExpressionEvaluator;
import com.schilings.neiko.common.redis.config.RedisCachePropertiesHolder;
import com.schilings.neiko.common.redis.core.lock.DistributedLock;
import com.schilings.neiko.common.redis.core.serializer.CacheSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * <p>Redis集成Neiko-common-cache</p>
 * </pre>
 * @author Schilings
*/
@Slf4j
public class RedisCacheRepository extends AbstractCacheRepository {

    private static final String REDIS = "redis";
    
    private final CacheSerializer cacheSerializer;

    private final StringRedisTemplate redisTemplate;

    private final ValueOperations<String, String> valueOperations;

    /**
     * 存放类型用于解析，这个不可取，这样子redis设置有效时间怎么办
     */
    private final Map<String, Type> returnTypes = new ConcurrentHashMap<>();

    
    public RedisCacheRepository(CacheSerializer cacheSerializer, StringRedisTemplate redisTemplate) {
        Assert.notNull(redisTemplate, "StringRedisTemplate can not be null!");
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
        this.cacheSerializer = cacheSerializer;
    }
    
    @Override
    public String getName() {
        return REDIS;
    }

    @Override
    public Object get(String key) throws IOException {
        //获取缓存的数据类型
        Type type = returnTypes.get(key);
        //如果类型存在，说明放入过缓存中
        if (type != null) {
            //如果放入的为空值标识符
            String value = valueOperations.get(key);
            //如果拿出的为null，说明可能缓存已经过期,直接返回空
            if (value == null) {
                return null;
            }
            if (value.equals(RedisCachePropertiesHolder.nullValue())) {
                return CacheExpressionEvaluator.NO_RESULT;
            }
            return cacheSerializer.deserialize(value, type);
        }
        return null;
    }
    
    @Override
    public Object syncGet(String key) throws IOException {
        //这里不做同步，使用原本neiko-common-cache就有的第一重读锁
        return get(key);
    }
    
    @Override
    public void put(String key, Object value) throws IOException {
        //如果值是空值标识，则在缓存中放入空值标识符
        if (value == CacheExpressionEvaluator.NO_RESULT) {
            valueOperations.set(key, RedisCachePropertiesHolder.nullValue());
            returnTypes.put(key, ResolvableType.forInstance(RedisCachePropertiesHolder.nullValue()).getType());
            return;
        }
        //如果不是空值标识，直接放入
        valueOperations.set(key, cacheSerializer.serialize(value));
        returnTypes.put(key, ResolvableType.forInstance(value).getType());
    }

    @Override
    public void put(String key, Object value, long ttl, TimeUnit unit) throws IOException {
        //如果值是空值标识，则在缓存中放入空值标识符
        if (value == CacheExpressionEvaluator.NO_RESULT) {
            setWithTtl(key, RedisCachePropertiesHolder.nullValue(), ttl, unit);
            returnTypes.put(key, ResolvableType.forInstance(RedisCachePropertiesHolder.nullValue()).getType());
            return;
        }
        //将结果序列化后存入缓存
        setWithTtl(key, cacheSerializer.serialize(value), ttl, unit);
        //存入返回的数据类型
        returnTypes.put(key, ResolvableType.forInstance(value).getType());
    }

    @Override
    public void syncPut(String key, Object value) throws IOException {
        //redis分布式锁key
        String lockKey = key + RedisCachePropertiesHolder.lockKeySuffix();
        //进行写，上锁同步
        DistributedLock.<String>instance().action(lockKey, () -> {
            put(key, value);
            return null;
        }).lock();
    }

    @Override
    public void syncPut(String key, Object value, long ttl, TimeUnit unit) throws IOException {
        //redis分布式锁key
        String lockKey = key + RedisCachePropertiesHolder.lockKeySuffix();
        DistributedLock.<String>instance().action(lockKey, () -> {
            put(key, value, ttl, unit);
            return null;
        }).lock();
    }

    
    public void setWithTtl(String key, String cacheValue, long ttl, TimeUnit unit) {
        //ttl<0的情况已经被过滤掉了
        if (ttl == 0) {//使用全局配置的ttl
            valueOperations.set(key, cacheValue, RedisCachePropertiesHolder.expireTime(), unit);
        }
        //这里肯定是ttl > 0的情况
        else {//使用注释配置的ttl
            valueOperations.set(key, cacheValue, ttl, unit);
        }
    }

    @Override
    public void evict(String key) {
        redisTemplate.delete(key);
        returnTypes.remove(key);
    }
}
