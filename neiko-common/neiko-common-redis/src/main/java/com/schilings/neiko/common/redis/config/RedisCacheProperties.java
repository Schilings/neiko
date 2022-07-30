package com.schilings.neiko.common.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/3/20 16:56
 */
@Data
@ConfigurationProperties(prefix = "neiko.redis")
public class RedisCacheProperties {

	/**
	 * 通用的key前缀
	 */
	private String keyPrefix = "neiko";

	/**
	 * redis锁 后缀
	 */
	private String lockKeySuffix = "locked";

	/**
	 * 默认分隔符
	 */
	private String delimiter = ":";

	/**
	 * 空值标识
	 */
	private String nullValue = "N_V";

	/**
	 * 默认超时时间(s)
	 */
	private long expireTime = 86400L;

	/**
	 * 锁的超时时间(ms)
	 */
	private long lockedTimeOut = 1000L;

	// 消息模块

	/**
	 * 可重试获取锁最大次数
	 */
	private long retryCount = 20L;

	/**
	 * 重试间隔(ms)
	 */
	private long retryIntervalTime = 100L;

	/**
	 * 消息处理线程池最大数线程数,小于等于0就是无限制
	 */
	private int threadCount = 10;

}
