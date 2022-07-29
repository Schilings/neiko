package com.schilings.neiko.common.redis.message.evaluator;


import com.schilings.neiko.common.core.evaluator.AbstractExpressionEvaluator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 * <p>用于处理应用程序事件的 SpEL 表达式解析的实用程序类</p>
 * </pre>
 * @author Schilings
*/
public class MessageEventExpressionEvaluator extends AbstractExpressionEvaluator {

    private final Map<ExpressionKey, Expression> conditionCache = new ConcurrentHashMap<>(64);

    /**
     * 确定由指定表达式定义的条件的计算结果是否为true 。
     */
    public boolean condition(String conditionExpression, Object rootObject,Method targetMethod,
                             AnnotatedElementKey methodKey, Object[] args, BeanFactory beanFactory) {
        
        MethodBasedEvaluationContext evaluationContext = 
                new MethodBasedEvaluationContext(rootObject, targetMethod, args, getParameterNameDiscoverer());
        if (beanFactory != null) {
            evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
            
        }
        return (Boolean.TRUE.equals(getExpression(this.conditionCache, methodKey, conditionExpression).getValue(
                evaluationContext, Boolean.class)));
    }
    
}
