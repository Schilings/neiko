package com.schilings.neiko.common.event.handler;

@FunctionalInterface
public interface EventHandler {

	void handle(Object event);

}
