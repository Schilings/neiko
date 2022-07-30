package com.schilings.neiko.common.redis.core.lock;

import com.schilings.neiko.common.redis.config.RedisCachePropertiesHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/3/27 21:15 缓存锁的操作类
 */
public class RedisCacheLock {

	private static final Logger log = LoggerFactory.getLogger(RedisCacheLock.class);

	private static StringRedisTemplate redisTemplate;

	public void setStringRedisTemplate(StringRedisTemplate redisTemplate) {
		RedisCacheLock.redisTemplate = redisTemplate;
	}

	/**
	 * 上锁
	 * @param requestId 请求id
	 * @return Boolean 是否成功获得锁
	 */
	public static Boolean lock(String lockKey, String requestId) {
		log.trace("lock: {key:{}, clientId:{}}", lockKey, requestId);
		// spring 在这个 api 上就添加了 nullable 注解,如果你不判 null，idea 就会爆黄
		Boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey, requestId,
				RedisCachePropertiesHolder.lockedTimeOut(), TimeUnit.SECONDS);
		return success;
	}

	/**
	 *
	 * 释放锁lua脚本 KEYS【1】：key值是为要加的锁定义的字符串常量 ARGV【1】：value值是 request id, 用来防止解除了不该解除的锁. 可用
	 * UUID
	 */
	private static final String RELEASE_LOCK_LUA_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

	/**
	 * 释放锁成功返回值
	 */
	private static final Long RELEASE_LOCK_SUCCESS_RESULT = 1L;

	/**
	 * 释放锁
	 * @param key 锁ID
	 * @param requestId 请求ID
	 * @return 是否成功
	 */
	public static boolean releaseLock(String key, String requestId) {
		log.trace("release lock: {key:{}, clientId:{}}", key, requestId);
		// 指定ReturnType为Long.class
		DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(RELEASE_LOCK_LUA_SCRIPT, Long.class);
		Long result = redisTemplate.execute(redisScript, Collections.singletonList(key), requestId);
		return Objects.equals(result, RELEASE_LOCK_SUCCESS_RESULT);
	}

	/**
	 * <p>
	 * (尝试获取锁)重试限制lua脚本
	 * </p>
	 * <p>
	 * KEYS[1] :key
	 * </p>
	 * <p>
	 * ARGV[1]: 重试时间
	 * </p>
	 * <p>
	 * ARGV[2]: 重试时间内可重试次数
	 * </p>
	 */
	private static final String RETRY_LIMIT_LUA_SCRIPT = "local retryNum = redis.call('incr', KEYS[1]);"
			+ "if retryNum == 1 then return redis.call('expire',KEYS[1],ARGV[1]) end;"
			+ "if retryNUM > tonumber(ARGV[2]) then return 0 end;" + "return 1;";

	/**
	 * (尝试获取锁)重试未到限制返回值
	 */
	private static final Long RETRY_SUCCESS_RESULT = 1L;

	/**
	 * 重试计数
	 * @param key
	 * @param time
	 * @param count
	 * @return true：重试未达到限制 false:重试次数达到设置限制次数值
	 */
	public static boolean retryLimit(String key, String time, String count) {
		log.trace("retry acquire lock: {key:{}, time:{}, count:{}}", key, time, count);
		// 指定ReturnType为Long.class
		DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(RETRY_LIMIT_LUA_SCRIPT, Long.class);
		Long result = redisTemplate.execute(redisScript, Collections.singletonList(key), time, count);
		return Objects.equals(result, RETRY_SUCCESS_RESULT);
	}

	// //进行操作，上锁同步
	// DistributedLock.<String>instance().action(lockKey, () -> {
	// doInvoke(args);
	// return null;
	// }).onLockFail(()->{
	// //如果获取失败，重试
	// try {
	// //先写死0.1秒后重试,先不考虑限制总重试次数
	// Thread.sleep(100);
	// //尝试重试
	// if (Boolean.TRUE.equals(RedisCacheLock.retryLimit(lockKey + "暂时写死后缀--limitNum",
	// "10", "5"))) {
	// invokeWithLock(args);
	// }
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// return null;
	// }).lock();
	//
	//

}
