package com.schilings.neiko.common.model.result;


public interface ResultCode {

	/**
	 * 获取业务码
	 * @return 业务码
	 */
	Integer getCode();

	/**
	 * 获取信息
	 * @return 返回结构体中的信息
	 */
	String getMessage();

}
