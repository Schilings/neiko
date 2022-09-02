package com.schilings.neiko.common.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BooleanEnum {

	/**
	 * 是
	 */
	TRUE(1),
	/**
	 * 否
	 */
	FALSE(0);

	private final int value;

}
