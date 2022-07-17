package com.schilings.neiko.samples.common.core;


import com.schilings.neiko.common.event.publisher.AsyncEventPublisher;
import com.schilings.neiko.common.event.publisher.EventPublisher;
import com.schilings.neiko.common.event.neiko.NeikoEventHandlerMethodMapping;
import com.schilings.neiko.common.event.publisher.SimpleEventPublisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;


@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        EventPublisher bean = context.getBean(EventPublisher.class);
        bean.publish(new DTO().setMessage("123213213"));
        

    }

    @Bean
    public NeikoEventHandlerMethodMapping neikoEventHandlerMethodMapping() {
        return new NeikoEventHandlerMethodMapping();
    }

    @Bean
    @Primary
    public EventPublisher neikoEventPublishe1r(NeikoEventHandlerMethodMapping neikoEventHandlerMethodMapping) {
        return new SimpleEventPublisher(neikoEventHandlerMethodMapping);
    }
    @Bean
    public EventPublisher neikoEventPublisher2(NeikoEventHandlerMethodMapping neikoEventHandlerMethodMapping) {
        return new AsyncEventPublisher(neikoEventHandlerMethodMapping);
    }
}
