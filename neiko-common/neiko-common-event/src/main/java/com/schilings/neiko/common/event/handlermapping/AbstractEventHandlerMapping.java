package com.schilings.neiko.common.event.handlermapping;

import com.schilings.neiko.common.event.handler.EventHandler;
import com.schilings.neiko.common.event.handler.EventHandlerCachHolder;
import com.schilings.neiko.common.event.handler.EventHandlerChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ApplicationObjectSupport;

import java.util.*;

/**
 * <pre>{@code
 *
 * }
 * <p>抽象的 事件-方法类型处理器处理器 映射类</p>
 * </pre>
 *
 * @author Schilings
 */

@Slf4j
public abstract class AbstractEventHandlerMapping extends ApplicationObjectSupport implements EventHandleMapping {
	

	protected boolean isHandler(Class<?> beanType) {
		return true;
	}
	

	@Override
	public List<EventHandler> getHandler(Object event) {
		List<EventHandler> handlers = EventHandlerCachHolder.get(event);
		List<EventHandler> handler = null;
		if (handlers == null) {
			handler = getHandlerInternal(event);
			if (handler == null) {
				EventHandlerCachHolder.put(event, Collections.emptyList());
			} else {
				EventHandlerCachHolder.put(event, handler);
			} 
		}
	
		// 使用默认处理器
		if (handler == null) {
			handler = getDefaultHandler();
		}
		//
		if (handler == null) {
			return null;
		}

		//List<EventHandler> chains = getEventHandlerChainChain(handler, event);
		if (logger.isTraceEnabled()) {
			logger.trace("Mapped to " + handler);
		}
		return handler;
	}

	protected List<EventHandler> getEventHandlerChainChain(List<EventHandler> handlers, Object event) {
		ArrayList<EventHandler> chains = new ArrayList<>(handlers.size());
		for (EventHandler handler : handlers) {
			EventHandlerChain chain = (handler instanceof EventHandlerChain ? (EventHandlerChain) handler
					: new EventHandlerChain(handler));
			chains.add(chain);
		}
		return chains;
	}

	protected List<EventHandler> getDefaultHandler() {
		return null;
	}

	protected abstract List<EventHandler> getHandlerInternal(Object event);

	@Override
	protected void initApplicationContext() throws BeansException {
		super.initApplicationContext();
		initInterceptors();
	}

	protected void initInterceptors() {
		
	}

}
