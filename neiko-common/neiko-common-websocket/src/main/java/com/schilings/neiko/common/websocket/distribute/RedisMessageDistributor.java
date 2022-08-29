package com.schilings.neiko.common.websocket.distribute;

import com.schilings.neiko.common.util.json.JsonUtils;
import com.schilings.neiko.common.websocket.distribute.dto.MessageDTO;
import com.schilings.neiko.common.websocket.session.WebSocketSessionStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 基于 redis PUB/SUB 的消息分发器, 订阅 websocket 发送消息，接收到消息时进行推送
 */
@Slf4j
public class RedisMessageDistributor extends AbstractMessageDistributor implements MessageListener {

	public static final String CHANNEL = "websocket-send";

	private final StringRedisTemplate stringRedisTemplate;

	public RedisMessageDistributor(WebSocketSessionStore webSocketSessionStore,
			StringRedisTemplate stringRedisTemplate) {
		super(webSocketSessionStore);
		this.stringRedisTemplate = stringRedisTemplate;
	}

	/**
	 * 消息分发
	 * @param messageDO 发送的消息
	 */
	@Override
	public void distribute(MessageDTO messageDO) {
		String str = JsonUtils.toJson(messageDO);
		stringRedisTemplate.convertAndSend(CHANNEL, str);
	}

	@Override
	public void onMessage(Message message, byte[] bytes) {
		log.info("redis channel Listener message send {}", message);
		byte[] channelBytes = message.getChannel();
		RedisSerializer<String> stringSerializer = stringRedisTemplate.getStringSerializer();
		String channel = stringSerializer.deserialize(channelBytes);

		// 这里没有使用通配符，所以一定是true
		if (CHANNEL.equals(channel)) {
			byte[] bodyBytes = message.getBody();
			String body = stringSerializer.deserialize(bodyBytes);
			MessageDTO messageDO = JsonUtils.toObj(body, MessageDTO.class);
			doSend(messageDO);
		}
	}

}
