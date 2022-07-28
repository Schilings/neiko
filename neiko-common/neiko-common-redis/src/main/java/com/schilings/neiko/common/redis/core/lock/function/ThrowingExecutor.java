package com.schilings.neiko.common.redis.core.lock.function;

/**
 * 允许抛出异常的执行器
 *
 * @author huyuanzhi
 */
public interface ThrowingExecutor<T> {

	/**
	 * 可抛异常的supplier
	 * @return T
	 * @throws Throwable 异常
	 */
	T execute() throws Throwable;

}
