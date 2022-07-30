package com.schilings.neiko.common.cache.components;

import com.schilings.neiko.common.util.spring.SpelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * <pre>
 * <p>抽象集成Spel的缓存键生成器</p>
 * </pre>
 *
 * @author Schilings
 */
@Slf4j
public class SimpleKeyGenerator implements KeyGenerator {

	StandardEvaluationContext spelContext;

	private final Object target;

	private final Method method;

	private final Object[] args;

	protected SimpleKeyGenerator(Object target, Method method, Object[] args) {
		this.target = target;
		this.method = method;
		this.args = args;
		this.spelContext = SpelUtils.getSpelContext(target, method, args);
	}

	@Override
	public String generate(Object target, Method method, Object... params) {
		return target.getClass().getName() + "." + method.getName();
	}

}
