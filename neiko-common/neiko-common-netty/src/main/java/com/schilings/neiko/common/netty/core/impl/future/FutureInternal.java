package com.schilings.neiko.common.netty.core.impl.future;

import com.schilings.neiko.common.netty.core.Future;
import com.schilings.neiko.common.netty.core.impl.ContextInternal;

/**
 *
 * <p>
 * </p>
 *
 * @author Schilings
 */
public interface FutureInternal<T> extends Future<T> {

	ContextInternal context();

	void addListener(Listener<T> listener);

}
