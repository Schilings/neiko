package com.schilings.neiko.common.model.result;

import com.schilings.neiko.common.model.constants.GeneralConstants;
import com.schilings.neiko.common.model.result.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseResultCode implements ResultCode {

	SUCCESS(GeneralConstants.SUCCESS, GeneralConstants.SUCCESS_MESSAGE),

	FAIL(GeneralConstants.FAIL, GeneralConstants.FAIL_MESSAGE),

	/**
	 * 数据库保存/更新异常
	 */
	UPDATE_DATABASE_ERROR(90001, "Update Database Error"),

	/**
	 * 通用的逻辑校验异常
	 */
	LOGIC_CHECK_ERROR(90004, "Logic Check Error"),

	/**
	 * 恶意请求
	 */
	MALICIOUS_REQUEST(90005, "Malicious Request"),

	/**
	 * 文件上传异常
	 */
	FILE_UPLOAD_ERROR(90006, "File Upload Error"),

	/**
	 * 重复执行
	 */
	REPEATED_EXECUTE(90007, "Repeated execute"),

	/**
	 * 未知异常
	 */
	UNKNOWN_ERROR(99999, "Unknown Error");

	/**
	 * 错误码
	 */
	private final Integer code;

	/**
	 * 错误提示
	 */
	private final String message;

}
