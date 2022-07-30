package com.schilings.neiko.common.cache.components;

import com.schilings.neiko.common.core.evaluator.AbstractExpressionEvaluator;
import org.springframework.beans.factory.BeanFactory;

import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.BeanFactoryResolver;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 * <p>缓存操作的El表达式解析</p>
 * </pre>
 *
 * @author Schilings
 */
public class CacheExpressionEvaluator extends AbstractExpressionEvaluator implements CacheEvaluator {

	/**
	 * 表示没有结果变量。
	 */
	public static final Object NO_RESULT = new Object();

	/**
	 * 保存结果对象的变量的名称。
	 */
	public static final String RESULT_VARIABLE = "result";

	private final Map<ExpressionKey, Expression> keyCache = new ConcurrentHashMap<>(64);

	private final Map<ExpressionKey, Expression> conditionCache = new ConcurrentHashMap<>(64);

	private final Map<ExpressionKey, Expression> unlessCache = new ConcurrentHashMap<>(64);

	/**
	 * 创建一个EvaluationContext 。
	 * @param method 方法
	 * @param args 方法参数
	 * @param target 目标对象
	 * @param result 返回值（可以是null ）或NO_RESULT如果此时没有返回
	 * @param beanFactory
	 * @return
	 */
	public EvaluationContext createEvaluationContext(Method method, Object[] args, Object target, Object result,
			BeanFactory beanFactory) {

		// spel 上下文
		StandardEvaluationContext evaluationContext = new MethodBasedEvaluationContext(target, method, args,
				getParameterNameDiscoverer());
		// 方法参数名数组
		String[] parameterNames = getParameterNameDiscoverer().getParameterNames(method);
		// 把方法参数放入 spel 上下文中
		if (parameterNames != null && parameterNames.length > 0) {
			for (int i = 0; i < parameterNames.length; i++) {
				evaluationContext.setVariable(parameterNames[i], args[i]);
			}
		}
		// 将方法运行结果放入spel上下文
		if (result != NO_RESULT || result != null) {
			evaluationContext.setVariable(RESULT_VARIABLE, result);
		}
		if (beanFactory != null) {
			evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
		}
		return evaluationContext;
	}

	/**
	 * 生成的key返回String类型
	 * @param keyExpression
	 * @param methodKey
	 * @param evalContext
	 * @return
	 */
	public String key(String keyExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
		return getExpression(this.keyCache, methodKey, keyExpression).getValue(evalContext, String.class);
	}

	/**
	 * Condition返回boolean
	 * @param conditionExpression
	 * @param methodKey
	 * @param evalContext
	 * @return
	 */
	public boolean condition(String conditionExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
		return (Boolean.TRUE.equals(getExpression(this.conditionCache, methodKey, conditionExpression)
				.getValue(evalContext, Boolean.class)));
	}

	/**
	 * Unless返回boolean
	 * @param unlessExpression
	 * @param methodKey
	 * @param evalContext
	 * @return
	 */
	public boolean unless(String unlessExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
		return (Boolean.TRUE.equals(
				getExpression(this.unlessCache, methodKey, unlessExpression).getValue(evalContext, Boolean.class)));
	}

	void clear() {
		this.keyCache.clear();
		this.conditionCache.clear();
		this.unlessCache.clear();
	}

}
