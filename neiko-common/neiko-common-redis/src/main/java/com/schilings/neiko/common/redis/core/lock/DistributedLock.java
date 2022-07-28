package com.schilings.neiko.common.redis.core.lock;

import cn.hutool.core.lang.Assert;
import com.schilings.neiko.common.redis.core.lock.function.ExceptionHandler;
import com.schilings.neiko.common.redis.core.lock.function.ThrowingExecutor;

import java.util.UUID;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * @author huyuanzhi
 * @version 1.0
 * @date 2021/11/16 分布式锁操作类
 */
public final class DistributedLock<T> implements Action<T>, StateHandler<T> {

	T result;

	String key;

	ThrowingExecutor<T> executeAction;

	UnaryOperator<T> successAction;

	Supplier<T> lockFailAction;

	ExceptionHandler exceptionHandler = DistributedLock::throwException;

	public static <T> Action<T> instance() {
		return new DistributedLock<>();
	}

	@Override
	public StateHandler<T> action(String lockKey, ThrowingExecutor<T> action) {
		Assert.isTrue(this.executeAction == null, "execute action has been already set");
		Assert.notNull(action, "execute action cant be null");
		Assert.notBlank(lockKey, "lock key cant be blank");
		this.executeAction = action;
		this.key = lockKey;
		return this;
	}

	@Override
	public StateHandler<T> onSuccess(UnaryOperator<T> action) {
		Assert.isTrue(this.successAction == null, "success action has been already set");
		Assert.notNull(action, "success action cant be null");
		this.successAction = action;
		return this;
	}

	@Override
	public StateHandler<T> onLockFail(Supplier<T> action) {
		Assert.isTrue(this.lockFailAction == null, "lock fail action has been already set");
		Assert.notNull(action, "lock fail action cant be null");
		this.lockFailAction = action;
		return this;
	}

	@Override
	public StateHandler<T> onException(ExceptionHandler exceptionHandler) {
		Assert.notNull(exceptionHandler, "exception handler cant be null");
		this.exceptionHandler = exceptionHandler;
		return this;
	}

	@Override
	public T lock() {
		//随机生成该次请求ID
		String requestId = UUID.randomUUID().toString();
		//尝试获取锁,因为 pipline 或者 事务模式下执行会返回 null会为null.防止空指针.返回的是包装类，所以Boolean.TRUE.equals验证
		if (Boolean.TRUE.equals(RedisCacheLock.lock(this.key, requestId))) {
			T value = null;
			boolean exResolved = false;
			try {
				//执行业务
				value = executeAction.execute();
				this.result = value;
			}
			catch (Throwable e) {
				//抛出异常回调
				this.exceptionHandler.handle(e);
				exResolved = true;
			}
			finally {//释放锁
				RedisCacheLock.releaseLock(this.key, requestId);
			}
			//执行成功回调
			if (!exResolved && this.successAction != null) {
				this.result = this.successAction.apply(value);
			}
		}
		//获取锁失败，失败回调
		else if (lockFailAction != null) {
			this.result = lockFailAction.get();
		}
		return this.result;
	}

	@SuppressWarnings("unchecked")
	private static <E extends Throwable> void throwException(Throwable t) throws E {
		throw (E) t;
	}

}
