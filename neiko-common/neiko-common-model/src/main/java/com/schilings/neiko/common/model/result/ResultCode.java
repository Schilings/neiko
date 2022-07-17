package com.schilings.neiko.common.model.result;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/3/20 14:45
 */
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
