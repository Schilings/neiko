package com.schilings.neiko.notify.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 通知接收方式
 */
@Getter
@RequiredArgsConstructor
public enum NotifyChannelEnum {

	// 站内
	STATION(1),
	// 短信
	SMS(2),
	// 邮件
	MAIL(3);

	private final int value;

}
