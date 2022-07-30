package com.schilings.neiko.common.event.handlermapping;

import com.schilings.neiko.common.event.handler.EventHandler;

import java.util.List;

public interface EventHandleMapping {

	List<EventHandler> getHandler(Object event);

}
