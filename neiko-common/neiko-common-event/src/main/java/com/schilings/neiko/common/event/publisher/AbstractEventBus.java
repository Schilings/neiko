package com.schilings.neiko.common.event.publisher;

import com.schilings.neiko.common.event.handler.EventHandler;
import com.schilings.neiko.common.event.handlermapping.EventHandleMapping;
import io.netty.channel.DefaultEventLoopGroup;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <pre>{@code
 *
 * }
 * <p>抽象的事件发布器，同时实现了对需要事件发布器支持的类的自动配置</p>
 * </pre>
 *
 * @author Schilings
 */
@Slf4j
@Getter
@Setter
public abstract class AbstractEventBus implements EventBus, ApplicationListener<ContextRefreshedEvent> {

	private static AtomicBoolean autoConfigured = new AtomicBoolean(false);

	private final EventHandleMapping handleMapping;


	protected AbstractEventBus(EventHandleMapping handleMapping) {
		this.handleMapping = handleMapping;
	}

	@Override
	public void publishBlocking(Object[] objects) {
		for (Object object : objects) {
			publishBlocking(object);
		}
	}

	@Override
	public void publishAsync(Object[] objects) {
		for (Object object : objects) {
			publishAsync(object);
		}
	}



	public List<EventHandler> getHandlers(Object o) {
		return getHandleMapping().getHandler(o);
	}

	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// 只加载一次
		if (autoConfigured.compareAndSet(false, true)) {
			ApplicationContext context = event.getApplicationContext();
			String[] namesForType = context.getBeanNamesForType(EventBusAware.class);
			if (namesForType.length > 0) {
				for (String s : namesForType) {
					EventBusAware bean = context.getBean(s, EventBusAware.class);
					bean.setEventBus(this);
				}
			}
			if (log.isTraceEnabled()) {
				log.trace("EventPusherAwares have been configured");
			}
		}
	}

}
