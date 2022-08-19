package com.schilings.neiko.common.netty.core;

/**
 *
 * <p>
 * 封装异步操作的结果。
 * </p>
 * <p>
 * 如果它失败了，那么可以通过 cause 获得失败的cause 。 如果成功，则实际结果可与result一起使用
 * </p>
 *
 * @author Schilings
 */
public interface AsyncResult<T> {

	/**
	 * 操作的结果。如果操作失败，这将为 null。
	 * @return
	 */
	T result();

	/**
	 * 描述失败的 Throwable。如果操作成功，这将为 null。
	 * @return
	 */
	Throwable cause();

	/**
	 * 如果成功则为真，否则为假
	 * @return
	 */
	boolean succeeded();

	/**
	 * 如果失败则为真，否则为假
	 * @return
	 */
	boolean failed();

}
