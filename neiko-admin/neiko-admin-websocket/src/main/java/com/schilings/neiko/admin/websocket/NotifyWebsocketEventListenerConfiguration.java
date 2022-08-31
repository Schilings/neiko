package com.schilings.neiko.admin.websocket;

import com.schilings.neiko.admin.websocket.eventwebsokct.NotifyWebsocketEventListener;
import com.schilings.neiko.common.websocket.distribute.MessageDistributor;
import com.schilings.neiko.notify.model.domain.NotifyInfo;
import com.schilings.neiko.notify.service.UserAnnouncementService;
import com.schilings.neiko.notify.websocket.NotifyInfoDelegateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@ConditionalOnClass({ NotifyWebsocketEventListener.class, UserAnnouncementService.class })
@Configuration(proxyBeanMethods = false)
public class NotifyWebsocketEventListenerConfiguration {

	private final MessageDistributor messageDistributor;

	@Bean
	public NotifyWebsocketEventListener notifyWebsocketEventListener(
			NotifyInfoDelegateHandler<? super NotifyInfo> notifyInfoDelegateHandler) {
		return new NotifyWebsocketEventListener(messageDistributor, notifyInfoDelegateHandler);
	}

}
