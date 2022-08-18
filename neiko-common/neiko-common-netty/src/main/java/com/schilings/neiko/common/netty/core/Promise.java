package com.schilings.neiko.common.netty.core;

/**
 * 
 * <p>表示可能已经发生或尚未发生的动作的可写方面。</p>
 * <p>future()方法返回与 Promise 关联的Future ，Future 可用于获取 Promise 完成的通知并检索其值。</p>
 * 
 * @author Schilings
*/
public interface Promise<T> extends Handler<AsyncResult<T>> {

    /**
     * 创建一个尚未完成的Promise
     * @param <T>
     * @return
     */
//    static <T> Promise<T> promise() {
//        return null;
//        
//    }

    /**
     * 与这个promise关联的Future ，它可以用来感知promise的完成
     * @return
     */
    Future<T> future();
    

    /**
     * 通过AsyncResult事件成功或失败,执行次Promise
     * @param asyncResult
     */
    @Override
    default void handle(AsyncResult<T> asyncResult) {
        if (asyncResult.succeeded()) {
            complete(asyncResult.result());
        } else {
            fail(asyncResult.cause());
        }
    }

    /**
     * 标记为完成
     * 设置结果。如果有任何处理程序，则将调用任何处理程序，并且Promise将被标记为已完成。
     * @param result
     */
    default void complete(T result) {
        if (!tryComplete(result)) {
            throw new IllegalStateException("Result is already complete");
        }
    }

    default void complete() {
        if (!tryComplete()) {
            throw new IllegalStateException("Result is already complete");
        }
    }

    /**
     * 标记为失败
     * @param cause
     */
    default void fail(Throwable cause) {
        if (!tryFail(cause)) {
            throw new IllegalStateException("Result is already complete");
        }
    }

    default void fail(String message) {
        if (!tryFail(message)) {
            throw new IllegalStateException("Result is already complete");
        }
    }
    
    /**
     * 尝试一次Complete
     * @param result
     * @return
     */
    boolean tryComplete(T result);

    default boolean tryComplete() {
        return tryComplete(null);
    }

    /**
     * 尝试一次Fail
     * @param cause
     * @return
     */
    boolean tryFail(Throwable cause);

    default boolean tryFail(String message) {
        return tryFail(new NoStackTraceThrowable(message));
    }
}
