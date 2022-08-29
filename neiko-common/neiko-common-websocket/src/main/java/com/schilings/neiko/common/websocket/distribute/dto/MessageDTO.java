package com.schilings.neiko.common.websocket.distribute.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class MessageDTO {

	/**
	 * 是否广播
	 */
	private Boolean needBroadcast;

	/**
	 * 对于拥有相同 sessionKey 的客户端，仅对其中的一个进行发送
	 */
	private Boolean onlyOneClientInSameKey;

	/**
	 * 需要发送的 sessionKeys 集合，当广播时，不需要
	 */
	private List<Object> sessionKeys;

	/**
	 * 需要发送的消息文本
	 */
	private String messageText;

}
