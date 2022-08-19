package com.schilings.neiko.common.netty.core.impl.future;

import com.schilings.neiko.common.netty.core.Promise;
import com.schilings.neiko.common.netty.core.impl.ContextInternal;
import io.netty.util.concurrent.FutureListener;

public interface PromiseInternal<T> extends Promise<T>, FutureListener<T>, FutureInternal<T> {

	/**
	 * 与此 Promise 关联的上下文，如果没有则为null
	 * @return
	 */
	ContextInternal context();

}
