package com.schilings.neiko.admin.websocket.jsonmessage;

import com.schilings.neiko.common.websocket.message.JsonWebSocketMessage;
import lombok.Getter;
import lombok.Setter;

/**
 * 字典修改消息
 */
@Getter
@Setter
public class DictChangeMessage extends JsonWebSocketMessage {

	public DictChangeMessage() {
		super("dict-change");
	}

	/**
	 * 改动的字典标识
	 */
	private String dictCode;

}
