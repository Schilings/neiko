package com.schilings.neiko.common.core.evaluator;

import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.Map;

/**
 * <pre>
 * <p>用于评估和缓存在 reflect.AnnotatedElement 上定义的reflect.AnnotatedElement表达式的共享实用程序类。</p>
 * </pre>
 *
 * @author Schilings
 */
public abstract class AbstractExpressionEvaluator {

	private final SpelExpressionParser parser;

	private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

	protected AbstractExpressionEvaluator(SpelExpressionParser parser) {
		Assert.notNull(parser, "SpelExpressionParser must not be null");
		this.parser = parser;
	}

	protected AbstractExpressionEvaluator() {
		this(new SpelExpressionParser());
	}

	protected SpelExpressionParser getParser() {
		return this.parser;
	}

	protected ParameterNameDiscoverer getParameterNameDiscoverer() {
		return this.parameterNameDiscoverer;
	}

	/**
	 * 返回指定Expression值的表达式
	 * @param elCache
	 * @param elementKey
	 * @param expression
	 * @return
	 */
	protected Expression getExpression(Map<ExpressionKey, Expression> elCache, AnnotatedElementKey elementKey,
			String expression) {
		ExpressionKey expressionKey = createKey(elementKey, expression);
		Expression expr = elCache.get(expressionKey);
		if (expr == null) {
			expr = parseExpression(expression);
			elCache.put(expressionKey, expr);
		}
		return expr;
	}

	/**
	 * 创建El表示的键
	 * @param elementKey
	 * @param expression
	 * @return
	 */
	private ExpressionKey createKey(AnnotatedElementKey elementKey, String expression) {
		return new ExpressionKey(elementKey, expression);
	}

	/**
	 * 解析指定的expression
	 * @param expression 要解析的表达式
	 * @return
	 */
	protected Expression parseExpression(String expression) {
		return getParser().parseExpression(expression);
	}

	/**
	 * <pre>
	 * <p>EL表达式的键</p>
	 * </pre>
	 *
	 * @author Schilings
	 */
	protected static class ExpressionKey implements Comparable<ExpressionKey> {

		private final AnnotatedElementKey element;

		private final String expression;

		protected ExpressionKey(AnnotatedElementKey element, String expression) {
			Assert.notNull(element, "AnnotatedElementKey must not be null");
			Assert.notNull(expression, "Expression must not be null");
			this.element = element;
			this.expression = expression;
		}

		@Override
		public boolean equals(@Nullable Object other) {
			if (this == other) {
				return true;
			}
			if (!(other instanceof ExpressionKey)) {
				return false;
			}
			ExpressionKey otherKey = (ExpressionKey) other;
			return (this.element.equals(otherKey.element)
					&& ObjectUtils.nullSafeEquals(this.expression, otherKey.expression));
		}

		@Override
		public int hashCode() {
			return this.element.hashCode() * 29 + this.expression.hashCode();
		}

		@Override
		public String toString() {
			return this.element + " with expression \"" + this.expression + "\"";
		}

		@Override
		public int compareTo(ExpressionKey other) {
			int result = this.element.toString().compareTo(other.element.toString());
			if (result == 0) {
				result = this.expression.compareTo(other.expression);
			}
			return result;
		}

	}

}
