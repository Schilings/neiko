package com.schilings.neiko.system.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

public final class SysUserConst {

	private SysUserConst() {
	}

	@Getter
	@AllArgsConstructor
	public enum Status {

		/**
		 * 正常
		 */
		NORMAL(1),
		/**
		 * 锁定的
		 */
		LOCKED(0);

		private final Integer value;

	}

	@Getter
	@AllArgsConstructor
	public enum Sex {

		/**
		 * 默认未知
		 */
		UNKONW(0),
		/**
		 * 男
		 */
		MALE(1),
		/**
		 * 女
		 */
		FEMALE(2);

		private final Integer value;

	}

	/**
	 * 用户类型，1系统用户，2平台用户
	 */
	@Getter
	@AllArgsConstructor
	public enum Type {

		/**
		 * 系统用户
		 */
		SYSTEM(1),
		/**
		 * 平台用户
		 */
		CUSTOMER(2);

		private final Integer value;

	}

}
