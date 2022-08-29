package com.schilings.neiko.autoconfigure.websocket.properties;

/**
 * websocket 消息分发器类型常量类
 *
 * @author hccake
 */
public final class MessageDistributorTypeConstants {

	private MessageDistributorTypeConstants() {
	}

	/**
	 * 本地
	 */
	public static final String LOCAL = "local";

	/**
	 * 基于 Redis PUB/SUB
	 */
	public static final String REDIS = "redis";

	/**
	 * 自定义
	 */
	public static final String CUSTOM = "custom";

}
