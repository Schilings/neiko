package com.schilings.neiko.common.websocket.distribute;

import com.schilings.neiko.common.websocket.distribute.dto.MessageDTO;

/**
 * 消息分发器
 */
public interface MessageDistributor {

	/**
	 * 消息分发
	 * @param messageDTO 发送的消息
	 */
	void distribute(MessageDTO messageDTO);

}
