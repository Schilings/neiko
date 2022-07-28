package com.schilings.neiko.common.redis.core.lock;


import com.schilings.neiko.common.redis.core.lock.function.ThrowingExecutor;

/**
 * @author huyuanzhi 锁住的方法
 * @param <T> 返回类型
 */
public interface Action<T> {

	/**
	 * 执行方法
	 * @param lockKey 待锁定的 key
	 * @param supplier 执行方法
	 * @return 状态处理器
	 */
	StateHandler<T> action(String lockKey, ThrowingExecutor<T> supplier);

}
