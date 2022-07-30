package com.schilings.neiko.common.redis.message.factory;

import com.schilings.neiko.common.redis.message.listener.MessageEventListener;
import com.schilings.neiko.common.redis.message.listener.MessageEventListenerMethodAdapter;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

/**
 * <pre>
 * <p>默认的监听器工厂</p>
 * </pre>
 *
 * @author Schilings
 */
public class DefaultMessageEventListenerFactory implements MessageEventListenerFactory, Ordered {

	/**
	 * 优先级最低
	 */
	private int order = LOWEST_PRECEDENCE;

	@Override
	public boolean supportsMethod(Method method) {
		return true;
	}

	@Override
	public MessageEventListener createMessageEventListener(String beanName, Class<?> type, Method method) {
		return new MessageEventListenerMethodAdapter(beanName, type, method);
	}

	@Override
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
