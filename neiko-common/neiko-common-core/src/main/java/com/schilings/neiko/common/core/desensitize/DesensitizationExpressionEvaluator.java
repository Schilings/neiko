package com.schilings.neiko.common.core.desensitize;

import com.schilings.neiko.common.core.evaluator.AbstractExpressionEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ResolvableType;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 * <p>脱敏注解生效spel条件评估</p>
 * </pre>
 *
 * @author Schilings
 */
@Slf4j
public class DesensitizationExpressionEvaluator extends AbstractExpressionEvaluator {

	/**
	 * 保存结果对象的变量的名称。
	 */
	public static final String VALUE_VARIABLE = "value";

	private final Map<ExpressionKey, Expression> conditionCache = new ConcurrentHashMap<>(64);

	/**
	 * 确定由指定表达式定义的条件的计算结果是否为true 。
	 */
	public boolean condition(String conditionExpression, Object value, AnnotatedElementKey methodKey,
			BeanFactory beanFactory) {
		StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
		if (beanFactory != null) {
			evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
		}
		if (value != null) {
			evaluationContext.setVariable(VALUE_VARIABLE, value);
		}
		return (Boolean.TRUE.equals(getExpression(this.conditionCache, methodKey, conditionExpression)
				.getValue(evaluationContext, Boolean.class)));
	}

}
