package com.schilings.neiko.common.netty.core;

import com.schilings.neiko.common.netty.core.impl.NeikoThread;

/**
 * 
 * <p>Handler程序执行的执行上下文</p>
 * 
 * @author Schilings
*/
public interface Context {


    /**
     * 当前线程是否为工作线程
     * @return
     */
    static boolean isOnWorkerThread() {
        Thread thread = Thread.currentThread();
        return thread instanceof NeikoThread && ((NeikoThread) thread).isWorker();
    }


    /**
     * 当前线程是否为事件线程
     * @return
     */
    static boolean isOnEventLoopThread() {
        Thread thread = Thread.currentThread();
        return thread instanceof NeikoThread && !((NeikoThread) thread).isWorker();
    }

    /**
     * 当前线程是否为Neiko线程
     * @return
     */
    static boolean isOnNeikoThread() {
        Thread thread = Thread.currentThread();
        return thread instanceof NeikoThread;
    }
    
    // context

    /**
     * 是否为事件上下文
     * @return
     */
    boolean isEventLoopContext();

    /**
     * 是否为工作上下文
     * @return
     */
    boolean isWorkerContext();
    
    //excute

    /**
     * 安全地执行一些阻塞代码。
     * 使用工作池中的线程执行处理程序blockingCodeHandler中的阻塞代码。
     * 当代码完成时，处理程序resultHandler将被调用，结果在原始上下文中（例如在调用者的原始事件循环中）。
     */
    <T> void executeBlocking(Handler<Promise<T>> blockingCodeHandler, Handler<AsyncResult<T>> resultHandler);
    
    <T> Future<T> executeBlocking(Handler<Promise<T>> blockingCodeHandler);
    
}
