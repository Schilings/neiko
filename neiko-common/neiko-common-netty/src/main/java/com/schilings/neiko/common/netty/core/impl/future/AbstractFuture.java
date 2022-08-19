package com.schilings.neiko.common.netty.core.impl.future;

import com.schilings.neiko.common.netty.core.impl.ContextInternal;

/**
 * Future抽象实现
 *
 * @param <T>
 */
public abstract class AbstractFuture<T> implements FutureInternal<T> {

	protected final ContextInternal context;

	AbstractFuture() {
		this(null);
	}

	AbstractFuture(ContextInternal context) {
		this.context = context;
	}

	@Override
	public final ContextInternal context() {
		return context;
	}

}
