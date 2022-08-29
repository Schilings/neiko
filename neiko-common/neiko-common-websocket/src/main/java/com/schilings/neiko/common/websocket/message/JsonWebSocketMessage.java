package com.schilings.neiko.common.websocket.message;

/**
 *
 * <p>
 * 自定义的Webscoket消息 Json类型的消息
 * </p>
 *
 * <ul>
 * <li>要求消息内容必须是一个 Json 对象
 * <li>Json 对象中必须有一个属性 type
 * <ul/>
 *
 * @author Schilings
 */
public abstract class JsonWebSocketMessage {

	public static final String TYPE_FIELD = "type";

	/**
	 * 以消息类型字段来区别不同业务消息
	 */
	private final String type;

	protected JsonWebSocketMessage(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

}
