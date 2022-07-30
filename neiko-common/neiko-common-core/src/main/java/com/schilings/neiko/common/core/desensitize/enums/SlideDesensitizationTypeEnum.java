package com.schilings.neiko.common.core.desensitize.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SlideDesensitizationTypeEnum {

	/**
	 * 自定义类型
	 */
	CUSTOM(0, 0, "*"),

	/**
	 * 全部字符替换为 *
	 */
	ALL_ASTERISK(0, 0, "*"),

	/**
	 * 【银行卡号】, 前6位和后4位不脱敏，中间脱敏 eg. 330150******1234
	 */
	BANK_CARD_NO(6, 4, "*"),

	/**
	 * 【身份证号】年月日脱敏，前6后4不脱敏 eg. 655356*******1234
	 */
	ID_CARD_NO(6, 4, "*"),

	/**
	 * 【手机号】，某些国家手机号位数短，所以不做前三后四，使用前三后二
	 */
	PHONE_NUMBER(3, 2, "*");

	/**
	 * 左边的明文数
	 */
	private final int leftPlainTextLen;

	/**
	 * 右边的明文数
	 */
	private final int rightPlainTextLen;

	/**
	 * 剩余部分字符逐个替换的字符串
	 */
	private final String maskString;

}
