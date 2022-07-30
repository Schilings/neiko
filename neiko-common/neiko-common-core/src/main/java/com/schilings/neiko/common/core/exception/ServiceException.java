package com.schilings.neiko.common.core.exception;

import cn.hutool.core.util.StrUtil;
import com.schilings.neiko.common.model.result.BaseResultCode;
import lombok.Getter;

/**
 * <pre>{@code
 *
 * }
 * <p>通用的业务异常类</p>
 * </pre>
 *
 * @author Schilings
 */
@Getter
public class ServiceException extends BaseException {

	private final int code;

	public ServiceException(BaseResultCode baseResultCode) {
		this(baseResultCode.getCode(), baseResultCode.getMessage());
	}

	public ServiceException(BaseResultCode baseResultCode, Throwable throwable) {
		this(baseResultCode.getCode(), baseResultCode.getMessage(), throwable);
	}

	public ServiceException(int code, String message) {
		super(message);
		this.code = code;
	}

	public ServiceException(int code, String message, Throwable e) {
		super(message, e);
		this.code = code;
	}

	/**
	 * 用于需要format结果的异常
	 * @param code
	 * @param message
	 * @param args
	 */
	public ServiceException(int code, String message, Throwable e, Object... args) {
		super(StrUtil.format(message, args), e);
		this.code = code;
	}

}
