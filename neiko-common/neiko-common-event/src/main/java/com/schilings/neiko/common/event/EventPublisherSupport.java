package com.schilings.neiko.common.event;


import com.schilings.neiko.common.event.publisher.EventPublisher;

public abstract class EventPublisherSupport {

    private EventPublisher publisher;

    void setEventPublisher(EventPublisher eventPublisher){
        publisher = eventPublisher;
    }

    public EventPublisher getPublisher() {
        return publisher;
    }
}
