package com.schilings.neiko.common.event.handler;

import com.schilings.neiko.common.event.handler.EventHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class EventHandlerChain implements EventHandler {

	private final EventHandler handler;

	public EventHandlerChain(EventHandler handlers) {
		this.handler = handlers;
	}

	boolean preHandle(Object event) {
		return true;
	}

	void postHandle(Object event) {

	}

	@Override
	public void handle(Object event) {
		if (preHandle(event)) {
			handler.handle(event);
			postHandle(event);
		}
	}

}
