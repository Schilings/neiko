package com.schilings.neiko.common.event.publisher;

import com.schilings.neiko.common.event.handlermapping.EventHandleMapping;
import com.schilings.neiko.common.event.handler.EventHandler;
import lombok.extern.slf4j.Slf4j;


/**
 * <pre>{@code
 *      
 * }
 * <p>简单实现的事件发布器</p>
 * </pre>
 * @author Schilings
*/
@Slf4j
public class SimpleEventPublisher extends AbstractEventPublisher {
    
    public SimpleEventPublisher(EventHandleMapping handleMapping) {
        super(handleMapping);
    }

    @Override
    public void publish(Object o) {
        for (EventHandler handler : getHandlers(o)) {
            try {
                handler.handle(o);
            } catch (Throwable throwable) {
                log.error("EventHandler executing throw ex:{}", throwable);
            }
        }
        
    }
}
