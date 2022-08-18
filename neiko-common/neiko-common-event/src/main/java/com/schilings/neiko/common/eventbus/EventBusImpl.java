package com.schilings.neiko.common.eventbus;


import com.schilings.neiko.common.event.handler.EvenHandlerMethodAdater;
import com.schilings.neiko.common.event.handler.EventHandler;
import com.schilings.neiko.common.event.handlermapping.EventHandleMapping;
import com.schilings.neiko.common.event.publisher.AbstractEventPublisher;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class EventBusImpl extends AbstractEventPublisher implements EventBus{


    private DefaultEventLoopGroup eventLoopGroup;

    public EventBusImpl(EventHandleMapping handleMapping) {
        super(handleMapping);
        eventLoopGroup = new DefaultEventLoopGroup();
    }

    

    @Override
    public <T> Future<T> request(Object message, Class<T> tClass) {
        return null;
    }

    @Override
    public EventBus publish(String address, Object message) {
        return null;
    }

    @Override
    public void publish(Object o) {
        
    }
}
