package com.schilings.neiko.common.security.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * <p>
 * 设备类型 针对一套 用户体系
 * </p>
 *
 * @author Schilings
 */
@AllArgsConstructor
@Getter
public enum DeviceType {

	/**
	 * pc端
	 */
	PC("pc"),

	/**
	 * app端
	 */
	APP("app"),

	/**
	 * 小程序端
	 */
	APPLETS("applets");

	private final String device;

}
