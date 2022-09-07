package com.schilings.neiko.wechat.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class WechatMpConst {

	@Getter
	@AllArgsConstructor
	public enum Status {

		/**
		 * 可用
		 */
		ENABLED(1),
		/**
		 * 不可用
		 */
		DISABLED(0);

		private final Integer value;

	}

	@Getter
	@AllArgsConstructor
	public enum Reply {

		/**
		 * 1、关注时回复；2、消息回复；3、关键词回复
		 */
		SUBSCRIBE(1), MESSAGE(2), KETWORD(3);

		private final Integer value;

	}

	@Getter
	@AllArgsConstructor
	public enum Message {

		// 消息类型（text：文本；image：图片；voice：语音；video：视频；shortvideo：小视频；location：地理位置）
		TEXT("text"), IMAGE("image"), VOICE("voice"), VIDEO("video"), SHORTVIDEO("shortvedio"), LOCATION("location");

		private final String value;

	}

	@Getter
	@AllArgsConstructor
	public enum Match {

		// （1、全匹配，2、半匹配）
		FULL(1), HALF(2);

		private final Integer value;

	}

}
