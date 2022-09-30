package com.schilings.neiko.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据权限范围类型
 */
@Getter
@AllArgsConstructor
public enum DataScopeTypeEnum {

	/**
	 * 查询全部数据
	 */
	ALL(0),

	/**
	 * 本人
	 */
	SELF(1),

	/**
	 * 本人及子级
	 */
	SELF_CHILD_LEVEL(2),

	/**
	 * 本级
	 */
	LEVEL(3),

	/**
	 * 本级及子级
	 */
	LEVEL_CHILD_LEVEL(4),

	/**
	 * 自定义
	 */
	CUSTOM(5);

	/**
	 * 类型
	 */
	private final Integer type;

}
