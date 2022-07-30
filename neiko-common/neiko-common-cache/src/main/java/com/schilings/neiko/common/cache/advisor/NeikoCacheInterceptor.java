package com.schilings.neiko.common.cache.advisor;

import com.schilings.neiko.common.cache.operation.CacheOperationInvoker;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * <pre>
 * <p>自定义Aop的Advisor拦截切入</p>
 * </pre>
 *
 * @author Schilings
 */
@Slf4j
public class NeikoCacheInterceptor extends NeikoCacheAspectSupport implements MethodInterceptor, Serializable {

	@Override
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		log.trace("=======The string cache aop is executed! method : {}", method.getName());
		CacheOperationInvoker aopAllianceInvoker = () -> {
			try {
				return invocation.proceed();
			}
			catch (Throwable ex) {
				throw new CacheOperationInvoker.ThrowableWrapper(ex);
			}
		};

		Object target = invocation.getThis();
		Assert.state(target != null, "Target must not be null");
		try {
			return excute(aopAllianceInvoker, target, method, invocation.getArguments());
		}
		catch (CacheOperationInvoker.ThrowableWrapper th) {
			throw th.getOriginal();
		}
	}

}
