package com.schilings.neiko.common.netty.core;


/**
 * 
 * <p>异步结果Future</p>
 * <p>表示可能已经发生或尚未发生的操作的结果</p>
 * 
 * @author Schilings
*/
public interface Future<T> extends AsyncResult<T> {

    /**
     * 创建一个初始的Futrue，
     * @param handler
     * @param <T>
     * @return
     */
//    static <T> Future<T> future(Handler<Promise<T>> handler) {
//        Promise<T> promise = Promise.promise();
//        try {
//            handler.handle(promise);
//        } catch (Throwable e) {
//            promise.tryFail(e);
//        }
//        return promise.future();
//    }

    /**
     * 如果完成则为真，否则为假
     * @return
     */
    boolean isComplete();

    /**
     * Success或Failure都会触发Complete
     * Complete完成的回调
     * @param handler
     * @return
     */
    Future<T> onComplete(Handler<AsyncResult<T>> handler);

    /**
     * Success成功的回调
     * @param handler
     * @return
     */
    default Future<T> onSuccess(Handler<T> handler) {
        return onComplete(asyncResult -> {
            if (asyncResult.succeeded()) {
                handler.handle(asyncResult.result());
            }
        });
    }

    /**
     * Fail失败的回调
     * @param handler
     * @return
     */
    default Future<T> onFailure(Handler<Throwable> handler) {
        return onComplete(asyncResult -> {
            if (asyncResult.failed()) {
                handler.handle(asyncResult.cause());
            }
        });
    }
    
    //AsyncResult的接口，声明式继承
    @Override
    T result();
    
    @Override
    Throwable cause();
    
    @Override
    boolean succeeded();
    
    @Override
    boolean failed();
    
    
    
    
    
    
}
