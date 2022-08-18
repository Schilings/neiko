package com.schilings.neiko.common.netty.core.impl.future;


import com.schilings.neiko.common.netty.core.AsyncResult;
import com.schilings.neiko.common.netty.core.Future;
import com.schilings.neiko.common.netty.core.Handler;
import com.schilings.neiko.common.netty.core.NoStackTraceThrowable;
import com.schilings.neiko.common.netty.core.impl.ContextInternal;

public final class FailedFuture<T> extends AbstractFuture<T> {

    private final Throwable cause;
    

    public FailedFuture(Throwable cause) {
        this(null, cause);
    }

    public FailedFuture(String failureMessage) {
        this(new NoStackTraceThrowable(failureMessage));
    }
    
    public FailedFuture(ContextInternal context, String failureMessage) {
        this(context, new NoStackTraceThrowable(failureMessage));
    }
    
    public FailedFuture(ContextInternal context, Throwable cause) {
        super(context);
        this.cause = cause != null ? cause : new NoStackTraceThrowable(null);
    }


    // FutureInternal

    @Override
    public void addListener(Listener<T> listener) {
        
    }
    
    
    //Future

    @Override
    public Future<T> onComplete(Handler<AsyncResult<T>> handler) {
        return this;
    }

    
    @Override
    public Future<T> onSuccess(Handler<T> handler) {
        return this;
    }

    @Override
    public Future<T> onFailure(Handler<Throwable> handler) {
        return this;
    }
    
    
    @Override
    public boolean isComplete() {
        return true;
    }
    
    
    //AsyncResult
    
    @Override
    public T result() {
        return null;
    }

    @Override
    public Throwable cause() {
        return cause;
    }

    @Override
    public boolean succeeded() {
        return false;
    }

    @Override
    public boolean failed() {
        return true;
    }

}
