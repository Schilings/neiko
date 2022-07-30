package com.schilings.neiko.common.cache.advisor;

import com.schilings.neiko.common.cache.parser.NeikoCacheAnnotationParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * <pre>
 * <p>作为缓存方法拦截Aop实现的切入点Pointcut</p>
 * </pre>
 *
 * @author Schilings
 */

@Slf4j
public class NeikoCachePointcut extends StaticMethodMatcherPointcut implements Serializable {

	private NeikoCacheAnnotationParser parser = new NeikoCacheAnnotationParser();

	/**
	 * 方法过滤器，来匹配哪些方法需要进行Aop拦截
	 * @param method
	 * @param targetClass
	 * @return
	 */
	@Override
	public boolean matches(Method method, Class<?> targetClass) {
		return (parser != null && !CollectionUtils.isEmpty(parser.getCacheOperations(method, targetClass)));
	}

	/**
	 * 类过滤器，来匹配哪些类的方法需要进行Aop拦截
	 */
	private class CacheOperationClassFilter implements ClassFilter {

		@Override
		public boolean matches(Class<?> clazz) {
			return (parser == null || parser.isCandidateClass(clazz));
		}

	}

}
