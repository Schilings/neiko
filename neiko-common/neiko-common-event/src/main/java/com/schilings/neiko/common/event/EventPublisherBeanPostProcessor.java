package com.schilings.neiko.common.event;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class EventPublisherBeanPostProcessor implements BeanPostProcessor {
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof EventPublisherSupport) {
            ((EventPublisherSupport) bean).setEventPublisher(null);
        }
        return bean;
    }
}
