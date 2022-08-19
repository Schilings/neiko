package com.schilings.neiko.common.event.publisher;

/**
 * 事件发布器支持面
 */
public interface EventBusAware {

	void setEventBus(EventBus eventPublisher);

}
