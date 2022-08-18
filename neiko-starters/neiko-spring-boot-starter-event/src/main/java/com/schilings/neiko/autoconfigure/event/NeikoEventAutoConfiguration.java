package com.schilings.neiko.autoconfigure.event;


import com.schilings.neiko.common.event.NeikoEventHandlerMethodMapping;
import com.schilings.neiko.common.event.publisher.AsyncEventPublisher;
import com.schilings.neiko.common.event.publisher.EventPublisher;
import com.schilings.neiko.common.event.publisher.SimpleEventPublisher;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@AutoConfiguration
public class NeikoEventAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public NeikoEventHandlerMethodMapping neikoEventHandlerMethodMapping() {
        return new NeikoEventHandlerMethodMapping();
    }
    
    @Bean
    @ConditionalOnProperty(name = "neiko.event.async.enabled",havingValue = "true",matchIfMissing = true)
    public EventPublisher AsyncEventPublisher(NeikoEventHandlerMethodMapping neikoEventHandlerMethodMapping) {
        return new AsyncEventPublisher(neikoEventHandlerMethodMapping);
    }

    @Bean
    @ConditionalOnMissingBean(EventPublisher.class)
    public EventPublisher simpleEventPublisher(NeikoEventHandlerMethodMapping neikoEventHandlerMethodMapping) {
        return new SimpleEventPublisher(neikoEventHandlerMethodMapping);
    }
}
