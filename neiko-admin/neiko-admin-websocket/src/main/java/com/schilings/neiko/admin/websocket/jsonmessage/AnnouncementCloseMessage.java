package com.schilings.neiko.admin.websocket.jsonmessage;

import com.schilings.neiko.common.websocket.message.JsonWebSocketMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnouncementCloseMessage extends JsonWebSocketMessage {

	public AnnouncementCloseMessage() {
		super("announcement-close");
	}

	/**
	 * ID
	 */
	private Long id;

}
