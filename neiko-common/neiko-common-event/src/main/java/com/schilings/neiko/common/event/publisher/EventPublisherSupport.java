package com.schilings.neiko.common.event.publisher;


/**
 * 事件发布器默认支持
 */
public abstract class EventPublisherSupport implements EventPublisherAware {

    private EventPublisher publisher;

    public void setEventPublisher(EventPublisher eventPublisher){
        publisher = eventPublisher;
    }

    public EventPublisher getPublisher() {
        return publisher;
    }
    
}
