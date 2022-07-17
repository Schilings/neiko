package com.schilings.neiko.common.event.publisher;


import com.schilings.neiko.common.event.handler.EventHandler;
import com.schilings.neiko.common.event.handlermapping.EventHandleMapping;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class AsyncEventPublisher extends AbstractEventPublisher {

    private ExecutorService executor;
    private Integer size = 20;
    
    public AsyncEventPublisher(EventHandleMapping handleMapping) {
        super(handleMapping);
    }
    
    @Override
    public void publish(Object o) {
        for (EventHandler handler : getHandleMapping().getHandler(o)) {
            try {
                executor.execute(() -> {
                    handler.handle(o);
                });
            } catch (Throwable throwable) {
                log.error("EventHandler executing throw ex:{}", throwable);
            }
        }
    }

    @PostConstruct
    public void contruct() {
        executor = Executors.newFixedThreadPool(size);
    }

    @PreDestroy
    public void destroy() {
        executor.shutdown();
    }
    
}
