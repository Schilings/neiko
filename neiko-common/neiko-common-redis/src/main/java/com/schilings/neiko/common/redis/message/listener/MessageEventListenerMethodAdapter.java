package com.schilings.neiko.common.redis.message.listener;


import com.schilings.neiko.common.redis.config.RedisCachePropertiesHolder;
import com.schilings.neiko.common.redis.core.lock.RedisCacheLock;
import com.schilings.neiko.common.redis.core.serializer.CacheSerializer;
import com.schilings.neiko.common.redis.message.annoation.RedisListener;
import com.schilings.neiko.common.redis.message.evaluator.MessageEventExpressionEvaluator;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <pre>
 * <p>{@link MessageListenerAdapter}</p>
 * </pre>
 * @author Schilings
*/
@Slf4j
public class MessageEventListenerMethodAdapter implements MessageEventListener {

    @Getter
    private final String beanName;
    @Getter
    private final Method method;
    @Getter
    private final Method targetMethod;

    private final AnnotatedElementKey methodKey;

    /**
     * 监听器绑定的频道
     */
    //private final List<Topic> declaredTopics;
    private final Topic declaredTopic;

    /**
     * 匹配条件
     */
    @Getter
    private final String condition;

    /**
     * 监听器Id
     */
    private volatile String listenerId;

    /**
     * 监听器绑定的频道
     */
    private final List<ResolvableType> declaredParameterTypes;

    //需要init注入
    private ApplicationContext applicationContext;

    private MessageEventExpressionEvaluator evaluator;
    
    private RedisSerializer redisSerializer;
    
    


    public MessageEventListenerMethodAdapter(String beanName, Class<?> targetClass, Method method) {
        this.beanName = beanName;
        this.method = BridgeMethodResolver.findBridgedMethod(method);
        this.targetMethod = (!Proxy.isProxyClass(targetClass) ?
                AopUtils.getMostSpecificMethod(method, targetClass) : this.method);
        this.methodKey = new AnnotatedElementKey(this.targetMethod, targetClass);
        this.declaredParameterTypes = resolveParameterTypes(method);
        validMethod(method,this.declaredParameterTypes);
        
        RedisListener ann = AnnotatedElementUtils.findMergedAnnotation(this.targetMethod, RedisListener.class);
        this.declaredTopic = resolveDeclaredTopic(ann);
        this.condition = (ann != null ? ann.condition() : null);
        String id = (ann != null ? ann.id() : "");
        this.listenerId = (!id.isEmpty() ? id : null);
    }

    /**
     * 字符串转化为Topic
     * @param ann
     * @return
     */
    private Topic resolveDeclaredTopic(RedisListener ann) {
        if (ann != null) {
            String topic = ann.topic();
            Assert.state(StringUtils.hasText(topic),"The topic of the message listener can not be empty.");
            return new PatternTopic(topic);
        }
        return null;
    }
    
    

    /**
     * 解析方法参数
     * @param method
     * @return
     */
    private static List<ResolvableType> resolveParameterTypes(Method method) {
        int count = method.getParameterCount();
        if (count > 2) {
            throw new IllegalStateException(
                    "Maximum one parameter is allowed for event listener method: " + method);
        }
        if (count == 0) {
            throw new IllegalStateException(
                    "Event parameter is mandatory for event listener method: " + method);
        }
        List<ResolvableType> types = null;
        if (count == 1) {
            types = Collections.singletonList(ResolvableType.forMethodParameter(method, 0));
        } else if (count == 2) {
            types = Arrays.asList(ResolvableType.forMethodParameter(method, 0), ResolvableType.forMethodParameter(method, 1));
        }
        return types;
    }

    /**
     * 校验方法：返回类型Void 第一个参数类型Message、Object、String、(byte[])第二个参数类型String，(byte[])
     * @param method
     */
    protected static void validMethod(Method method, List<ResolvableType> types) {
        Class<?> returnType = method.getReturnType();
        if (!Void.TYPE.isAssignableFrom(returnType)) {
            throw new IllegalStateException(
                    "Return type is must be void for event listener method: " + method);
        }
        //第一个参数类型
        ResolvableType firstType = types.get(0);
        if (firstType.isArray()) {
            throw new IllegalStateException(
                    "The first parameter type can not be Array for event listener method: " + method);
        }
        // 第二个参数类型String
        if (types.size() == 2) {
            ResolvableType secondType = types.get(1);
            
            if (!secondType.isAssignableFrom(String.class) || secondType.isArray()) {
                throw new IllegalStateException(
                        "The second parameter type is must be String for event listener method: " + method);
            }
        }
    }

    /**
     * 初始化这个实例。
     * @param applicationContext
     * @param evaluator
     * @param serializer
     * @param redisSerializer
     */
    public void init(ApplicationContext applicationContext, MessageEventExpressionEvaluator evaluator, RedisSerializer redisSerializer) {
        this.applicationContext = applicationContext;
        this.evaluator = evaluator;
        this.redisSerializer = redisSerializer;
    }


    /**
     * 返回订阅的主题通道
     * @return
     */
    @Override
    public Topic topic() {
        return declaredTopic;
    }


    /**
     * 触发事件
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        processMessage(message,pattern);
    }

    /**
     * 处理指定的通道内的{@link Message} ，检查条件是否匹配并处理非空结果（如果有）
     * @param message
     * @param pattern
     */
    @SneakyThrows
    private void processMessage(Message message, byte[] pattern) {
        //解析参数
        String channel = new String(pattern);
        String body = new String(message.getBody());
        Object data = null;
        ResolvableType firstType = this.declaredParameterTypes.get(0);
        if (firstType.isAssignableFrom(Message.class)) {
            data = message;
        } else if (firstType.isAssignableFrom(String.class)) {
            data = body;
        } else {
            //data = serializer.deserialize(body, firstType.getType());
            data = redisSerializer.deserialize(message.getBody());
        }
        
        //执行
        if (conditionPass(data, channel)) {
            invokeWithLock(data, channel);
        }
    }

    /**
     * 校验spel条件
     * @param message
     * @param channel
     * @return
     */
    private boolean conditionPass(Object message, String channel) {
        if (message == null) {
            return false;
        }
        String condition = getCondition();
        if (StringUtils.hasText(condition)) {
            Assert.notNull(this.evaluator, "EventExpressionEvaluator must not be null");
            return this.evaluator.condition(condition, this, this.targetMethod, this.methodKey,
                    new Object[]{message, channel}, this.applicationContext);
        }
        return true;
    }

    /**
     * 加锁同步执行处理器
     * @param args
     */
    private void invokeWithLock(Object... args) {
        //锁key : topic + suffix
        String lockKey = topic().getTopic() + RedisCachePropertiesHolder.lockKeySuffix();
        String requestId = UUID.randomUUID().toString();
      
        //自旋获取锁
        long retryNum = 0;
        for (; ; ) {
            if (Boolean.TRUE.equals(RedisCacheLock.lock(lockKey, requestId))) {
                try {
                    doInvoke(args);
                }finally {
                    RedisCacheLock.releaseLock(lockKey, requestId);
                }
                break;
            }
            try {
                Thread.sleep(RedisCachePropertiesHolder.retryIntervalTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (retryNum++ > RedisCachePropertiesHolder.retryCount()) {
                break;
            }
        }
    }

    /**
     * 真正执行
     * @param args
     */
    private void doInvoke(Object... args) {
        Object bean = getTargetBean();
        // 通过equals(null)检查检测包保护的NullBean实例
        if (bean.equals(null)) {
            return;
        }
        ReflectionUtils.makeAccessible(this.method);
        try {
            this.method.invoke(bean, args);
            return;
        }
        catch (IllegalArgumentException ex) {
            throw new IllegalStateException(getInvocationErrorMessage(bean, ex.getMessage(), args), ex);
        }
        catch (IllegalAccessException ex) {
            throw new IllegalStateException(getInvocationErrorMessage(bean, ex.getMessage(), args), ex);
        }
        catch (InvocationTargetException ex) {
            // 抛出底层异常
            Throwable targetException = ex.getTargetException();
            if (targetException instanceof RuntimeException) {
                throw (RuntimeException) targetException;
            }
            else {
                String msg = getInvocationErrorMessage(bean, "Failed to invoke event listener method", args);
                throw new UndeclaredThrowableException(targetException, msg);
            }
        }
    }


    protected Object getTargetBean() {
        Assert.notNull(this.applicationContext, "ApplicationContext must no be null");
        return this.applicationContext.getBean(this.beanName);
    }

    private String getInvocationErrorMessage(Object bean, String message, Object[] resolvedArgs) {
        StringBuilder sb = new StringBuilder(getDetailedErrorMessage(bean, message));
        sb.append("Resolved arguments: \n");
        for (int i = 0; i < resolvedArgs.length; i++) {
            sb.append('[').append(i).append("] ");
            if (resolvedArgs[i] == null) {
                sb.append("[null] \n");
            }
            else {
                sb.append("[type=").append(resolvedArgs[i].getClass().getName()).append("] ");
                sb.append("[value=").append(resolvedArgs[i]).append("]\n");
            }
        }
        return sb.toString();
    }

    protected String getDetailedErrorMessage(Object bean, String message) {
        StringBuilder sb = new StringBuilder(message).append('\n');
        sb.append("HandlerMethod details: \n");
        sb.append("Bean [").append(bean.getClass().getName()).append("]\n");
        sb.append("Method [").append(this.method.toGenericString()).append("]\n");
        return sb.toString();
    }

    public String getListenerId() {
        String id = this.listenerId;
        if (id == null) {
            id = getDefaultListenerId();
            this.listenerId = id;
        }
        return id;
    }

    protected String getDefaultListenerId() {
        Method method = getTargetMethod();
        StringJoiner sj = new StringJoiner(",", "(", ")");
        for (Class<?> paramType : method.getParameterTypes()) {
            sj.add(paramType.getName());
        }
        return ClassUtils.getQualifiedMethodName(method) + sj.toString();
    }

}
