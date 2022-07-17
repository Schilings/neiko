package com.schilings.neiko.common.event.publisher;


import com.schilings.neiko.common.event.handlermapping.EventHandleMapping;
import com.schilings.neiko.common.event.publisher.EventPublisher;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public abstract class AbstractEventPublisher implements EventPublisher {
    
    private final EventHandleMapping handleMapping;
    
    protected AbstractEventPublisher(EventHandleMapping handleMapping) {
        this.handleMapping = handleMapping;
    }
}
