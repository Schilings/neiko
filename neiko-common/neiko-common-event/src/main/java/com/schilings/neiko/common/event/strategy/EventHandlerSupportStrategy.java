package com.schilings.neiko.common.event.strategy;

public interface EventHandlerSupportStrategy<T> {

	boolean support(Object o, T eventMappingInfo);

}
