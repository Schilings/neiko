package com.schilings.neiko.common.core.desensitize.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegexDesensitizationTypeEnum {

	/**
	 * 自定义类型
	 */
	CUSTOM("^[\\s\\S]*$", "******"),

	/**
	 * 【邮箱】脱敏，保留邮箱第一个字符和'@'之后的原文显示，中间的显示为4个* eg. 12@qq.com -> 1****@qq.com
	 */
	EMAIL("(^.)[^@]*(@.*$)", "$1****$2"),

	/**
	 * 【对称密文的密码】脱敏，前3后2，中间替换为 4个 *
	 */
	ENCRYPTED_PASSWORD("(.{3}).*(.{2}$)", "$1****$2");

	/**
	 * 匹配的正则表达式
	 */
	private final String regex;

	/**
	 * 替换规则
	 */
	private final String replacement;

}
