package com.schilings.neiko.common.event.publisher;

import com.schilings.neiko.common.event.EventRequestException;
import com.schilings.neiko.common.event.NeikoEventThreadFactory;
import com.schilings.neiko.common.event.handler.EvenHandlerMethodAdater;
import com.schilings.neiko.common.event.handler.EventHandler;
import com.schilings.neiko.common.event.handlermapping.EventHandleMapping;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

/**
 * <pre>{@code
 *
 * }
 * <p>提供异步支持的事件发布器</p>
 * </pre>
 *
 * @author Schilings
 */
@Slf4j
@Setter
@Getter
public class EventBusImpl extends AbstractEventBus {

	private DefaultEventLoopGroup executor;

	public EventBusImpl(EventHandleMapping handleMapping) {
		super(handleMapping);
	}

	@PostConstruct
	public void construct() {
		executor = new DefaultEventLoopGroup(new NeikoEventThreadFactory(getClass()));
	}

	@PreDestroy
	public void destroy() {
		executor.shutdownGracefully();
	}

	@Override
	public void publishAsync(Object o) {
		for (EventHandler handler : getHandlers(o)) {
			executor.submit(() -> {
				handler.handle(o);
			});
		}
	}

	@Override
	public void publishBlocking(Object o) {
		for (EventHandler handler : getHandlers(o)) {
			handler.handle(o);
		}
	}

	@Override
	public Object requestBlocking(Object o) {
		List<EventHandler> handlers = getHandlers(o);
		if (handlers != null && handlers.size() >= 2) {
			throw new EventRequestException("The handler for event request can not be more than 2.");
		}
		EventHandler eventHandler = handlers.get(0);
		EvenHandlerMethodAdater adater = null;
		if (eventHandler instanceof EvenHandlerMethodAdater) {
			adater = (EvenHandlerMethodAdater) eventHandler;
			adater.handle(o);
			return adater.getResult();
		}
		throw new EventRequestException("The handler for event request can not return result.");
	}

}
