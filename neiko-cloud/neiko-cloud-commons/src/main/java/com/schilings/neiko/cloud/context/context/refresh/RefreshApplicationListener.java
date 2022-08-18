package com.schilings.neiko.cloud.context.context.refresh;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

//RefreshEventListener是SpringCloud提供的类，不与其他配置中心耦合
@Component
public class RefreshApplicationListener implements SmartApplicationListener {

    private static Log log = LogFactory.getLog(RefreshApplicationListener.class);

    //SpringCloud提供的
    private ContextRefresher refresh;

    private AtomicBoolean ready = new AtomicBoolean(false);
    
    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return ApplicationReadyEvent.class.isAssignableFrom(eventType)
                || RefreshEvent.class.isAssignableFrom(eventType);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent) {
            handle((ApplicationReadyEvent) event);
        }
        else if (event instanceof RefreshEvent) {
            handle((RefreshEvent) event);
        }
    }
    public void handle(ApplicationReadyEvent event) {
        this.ready.compareAndSet(false, true);
    }

    public void handle(RefreshEvent event) {
        if (this.ready.get()) {
            System.out.println("[RefreshApplicationListener] Event received " + event.getEventDesc());
            //刷新 暂时不研究
            //Set<String> keys = this.refresh.refresh();
        }
    }
}
