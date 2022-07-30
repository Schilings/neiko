package com.schilings.neiko.common.event;

import com.schilings.neiko.common.event.handlermapping.AbstractEventHandlerMethodMapping;
import com.schilings.neiko.common.event.strategy.EventHandlerSupportStrategy;
import com.schilings.neiko.common.event.annotation.NeikoEventHandler;
import com.schilings.neiko.common.event.annotation.NeikoEventListener;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;

public class NeikoEventHandlerMethodMapping extends AbstractEventHandlerMethodMapping<NeikoEventMappingInfo> {

	public NeikoEventHandlerMethodMapping() {
		setSupportStrategy((EventHandlerSupportStrategy<NeikoEventMappingInfo>) (o, mapping) -> {
			return mapping.getEventClz().isAssignableFrom(o.getClass());
		});
	}

	@Override
	protected boolean isHandler(Class<?> beanType) {
		return AnnotatedElementUtils.hasAnnotation(beanType, NeikoEventListener.class);
	}

	@Override
	protected boolean isHandlerMethod(Method method) {
		return AnnotatedElementUtils.hasAnnotation(method, NeikoEventHandler.class);
	}

	@Override
	protected NeikoEventMappingInfo getMappingForMethod(Method method, Class<?> userType) {
		NeikoEventMappingInfo info = new NeikoEventMappingInfo(userType, method);
		info.setNeikoEventHandler(method.getAnnotation(NeikoEventHandler.class));
		return info;
	}

}
