package com.schilings.neiko.common.event.publisher;

import com.schilings.neiko.common.event.handler.EventHandler;
import com.schilings.neiko.common.event.handlermapping.EventHandleMapping;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
public class AsyncEventPublisher extends AbstractEventPublisher {

	private static final Integer DEFAULT_THREAD_SIZE = 10;

	private DefaultEventLoopGroup executor;

	private final Integer size;

	public AsyncEventPublisher(EventHandleMapping handleMapping) {
		this(handleMapping, DEFAULT_THREAD_SIZE);
	}

	public AsyncEventPublisher(EventHandleMapping handleMapping, Integer size) {
		super(handleMapping);
		this.size = size;
	}

	@Override
	public void publish(Object o) {
		for (EventHandler handler : getHandlers(o)) {
			try {
				executor.submit(() -> {
					handler.handle(o);
				});
			}
			catch (Throwable throwable) {
				log.error("EventHandler executing throw ex:{}", throwable);
			}
		}
	}

	@PostConstruct
	public void construct() {
		executor = new DefaultEventLoopGroup(new DefaultThreadFactory(getClass()));
	}

	@PreDestroy
	public void destroy() {
		executor.shutdownGracefully();
	}

}
