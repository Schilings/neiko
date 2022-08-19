package com.schilings.neiko.common.event.publisher;

/**
 * <pre>{@code
 *
 * }
 * <p>事件发布器抽象接口</p>
 * </pre>
 *
 * @author Schilings
 */
public interface EventBus {

	void publishAsync(Object o);

	void publishBlocking(Object o);
	
	void publishAsync(Object[] objects);
	
	void publishBlocking(Object[] objects);

	Object requestBlocking(Object o);

}
