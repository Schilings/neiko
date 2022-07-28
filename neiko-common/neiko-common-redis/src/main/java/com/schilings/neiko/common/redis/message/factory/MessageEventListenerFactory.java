package com.schilings.neiko.common.redis.message.factory;


import com.schilings.neiko.common.redis.message.listener.MessageEventListener;
import com.schilings.neiko.common.redis.message.annoation.RedisListener;
import org.springframework.data.redis.connection.MessageListener;

import java.lang.reflect.Method;

/**
 * <pre>
 * <p>为使用{@link RedisListener}注释的方法创建{@link MessageListener}的策略接口。</p>
 * </pre>
 * @author Schilings
*/
public interface MessageEventListenerFactory {

    /**
     * 指定此工厂是否支持指定的Method 。
     * @param method
     * @return
     */
    boolean supportsMethod(Method method);

    /**
     * 为指定的方法创建一个{@link MessageListener}
     * @param beanName
     * @param type
     * @param method
     * @return
     */
    MessageEventListener createMessageEventListener(String beanName, Class<?> type, Method method);
}
