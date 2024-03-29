package com.schilings.neiko.common.event.handler;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>{@code
 *
 * }
 * <p>对事件处理器的适配类</p>
 * </pre>
 *
 * @author Schilings
 */
@Slf4j
public class EvenHandlerMethodAdater implements EventHandler {

	private final HandlerMethod handlerMethod;

	// 方法返回结果
	private Object result;

	public Object getResult() {
		return result;
	}

	public EvenHandlerMethodAdater(HandlerMethod handlerMethod) {
		this.handlerMethod = handlerMethod.createWithResolvedBean();
	}

	@Override
	public void handle(Object event) {
		handleInternal(event, handlerMethod);

	}

	public HandlerMethod getHandlerMethod() {
		return handlerMethod;
	}

	protected void handleInternal(Object event, HandlerMethod handlerMethod) {
		invokeHandlerMethod(event, handlerMethod);
	}

	private void invokeHandlerMethod(Object event, HandlerMethod handlerMethod) {
		EventHandlerMethod method = new EventHandlerMethod(handlerMethod);
		try {
			method.invokeForEvent(event);
			result = method.getResult();
		}
		catch (Exception e) {
			log.error("EventHandlerMethod executing throw ex:{}", e.getMessage());
			log.error("{}", e);
		}

	}

}
