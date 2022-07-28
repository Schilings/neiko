package com.schilings.neiko.common.redis.message.listener;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.Topic;

/**
 * <pre>
 * <p>PUB/SUB 的监听器</p>
 * </pre>
 * @author Schilings
*/
public interface MessageEventListener extends MessageListener {

    /**
     * 订阅者订阅的话题
     * @return topic
     */
    Topic topic();
}
