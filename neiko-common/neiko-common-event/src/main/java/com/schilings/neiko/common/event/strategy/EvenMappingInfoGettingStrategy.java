package com.schilings.neiko.common.event.strategy;

import com.schilings.neiko.common.event.handler.EventHandler;
import com.schilings.neiko.common.event.handlermapping.AbstractEventHandlerMethodMapping;
import com.schilings.neiko.common.event.handlermapping.EventMappingInfo;

import java.util.Collection;

public interface EvenMappingInfoGettingStrategy<T extends EventMappingInfo> {

	Collection<T> get(AbstractEventHandlerMethodMapping.EventMappingRegistry registry, Object event);

}
