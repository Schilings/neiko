package com.schilings.neiko.autoconfigure.web.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常处理类型
 */
@Getter
@AllArgsConstructor
public enum ExceptionHandleTypeEnum {

	/**
	 * 异常处理通知类型 说明
	 */
	NONE("不通知"), MAIL("邮件通知"),;

	private final String text;

}
