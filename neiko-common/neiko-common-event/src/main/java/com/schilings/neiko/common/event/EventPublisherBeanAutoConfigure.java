package com.schilings.neiko.common.event;


import com.schilings.neiko.common.event.publisher.EventPublisher;
import com.schilings.neiko.common.event.publisher.EventPublisherAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class EventPublisherBeanAutoConfigure implements ApplicationListener<ContextRefreshedEvent>{

    private static AtomicBoolean autoConfigured = new AtomicBoolean(false);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //只加载一次
        if (autoConfigured.compareAndSet(false, true)) {
            ApplicationContext context = event.getApplicationContext();
            EventPublisher publisher = context.getBean(EventPublisher.class);
            if (publisher != null) {
                String[] namesForType = context.getBeanNamesForType(EventPublisherAware.class);
                if (namesForType.length > 0) {
                    for (String s : namesForType) {
                        EventPublisherAware bean = context.getBean(s, EventPublisherAware.class);
                        bean.setEventPublisher(publisher);
                    }
                    if (log.isTraceEnabled()) {
                        log.trace("EventPusherAware configured");
                    }
                }
                if (log.isTraceEnabled()) {
                    log.trace("EventPusherAware not found");
                }
            }
        }
    }
}
