package com.schilings.neiko.sample.authorization.server.event;


import org.springframework.context.ApplicationEvent;

public class DemoEvent extends ApplicationEvent {
    public DemoEvent(Object source) {
        super(source);
    }
}
