package com.schilings.neiko.common.event.publisher;

/**
 * <pre>{@code
 *      
 * }
 * <p>事件发布器抽象接口</p>
 * </pre>
 * @author Schilings
*/
public interface EventPublisher {

    void publish(Object o);

    void publish(Object[] objects);
}
