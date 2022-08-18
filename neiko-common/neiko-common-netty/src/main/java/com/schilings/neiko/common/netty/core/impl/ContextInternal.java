package com.schilings.neiko.common.netty.core.impl;


import com.schilings.neiko.common.netty.core.*;
import io.netty.channel.EventLoop;

public interface ContextInternal extends Context {

    /**
     * 返回当前线程的当前上下文
     * @return
     */
    static ContextInternal current() {
        Thread current = Thread.currentThread();
        if (current instanceof NeikoThread) {
            return ((NeikoThread) current).context();
        }
        return null;
    }

    /**
     * 返回此 Context 使用的 Netty EventLoop。
     * @return
     */
    EventLoop nettyEventLoop();

    <T> Future<T> succeededFuture();

    <T> Future<T> succeededFuture(T result);

    <T> Future<T> failedFuture(Throwable failure);

    <T> Future<T> failedFuture(String message);


    <T> void executeBlocking(Handler<Promise<T>> blockingCodeHandler, TaskQueue queue, Handler<AsyncResult<T>> resultHandler);

    <T> Future<T> executeBlocking(Handler<Promise<T>> blockingCodeHandler, TaskQueue queue);

    <T> void executeBlockingInternal(Handler<Promise<T>> action, Handler<AsyncResult<T>> resultHandler);
    
    <T> Future<T> executeBlockingInternal(Handler<Promise<T>> action);


    void execute(Handler<Void> task);

    void execute(Runnable task);

    <T> void execute(T argument, Handler<T> task);
}
