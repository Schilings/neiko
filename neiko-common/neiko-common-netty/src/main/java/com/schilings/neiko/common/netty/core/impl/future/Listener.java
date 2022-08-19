package com.schilings.neiko.common.netty.core.impl.future;

/**
 *
 * <p>
 * 结果监听
 * </p>
 *
 * @author Schilings
 */
public interface Listener<T> {

	/**
	 * 成功回调
	 * @param value
	 */
	void onSuccess(T value);

	/**
	 * 失败回调
	 * @param failure
	 */
	void onFailure(Throwable failure);

}
