package com.schilings.neiko.common.event.publisher;

/**
 * 事件发布器默认支持
 */
public abstract class EventBusSupport implements EventBusAware {

	private EventBus eventBus;

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

}
