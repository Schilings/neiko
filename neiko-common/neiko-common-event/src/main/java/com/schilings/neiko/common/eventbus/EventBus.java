package com.schilings.neiko.common.eventbus;


import com.schilings.neiko.common.netty.core.AsyncResult;
import com.schilings.neiko.common.netty.core.Handler;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.Future;
import org.springframework.lang.Nullable;

import java.util.concurrent.Callable;


/**
 * 
 * <p>Event Bus 总线</p>
 * 
 * @author Schilings
*/
public interface EventBus {
    
    

    <T> Future<T> request(Object message, Class<T> tClass);

    EventBus publish(String address, @Nullable Object message);
    
    
    
}
