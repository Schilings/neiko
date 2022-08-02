package com.schilings.neiko.common.model.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <pre>{@code
 *
 * }
 * <p>一般接口通用返回封装类</p>
 * </pre>
 *
 * @author Schilings
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Schema(title = "通用的返回体结构")
public class R<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(title = "返回状态码", defaultValue = "0")
	private int code;

	@Schema(title = "返回信息", defaultValue = "Success")
	private String message;

	@Schema(title = "数据", nullable = true, defaultValue = "null")
	private T data;

	// ok

	public static <T> R<T> ok() {
		return result(BaseResultCode.SUCCESS, null);
	}

	public static <T> R<T> ok(String message) {
		return result(BaseResultCode.SUCCESS.getCode(), message, null);
	}

	public static <T> R<T> ok(T data) {
		return result(BaseResultCode.SUCCESS, data);
	}

	public static <T> R<T> ok(String message, T data) {
		return result(BaseResultCode.SUCCESS.getCode(), message, data);
	}

	// fail

	public static <T> R<T> fail() {
		return result(BaseResultCode.FAIL, null);
	}

	public static <T> R<T> fail(String message) {
		return result(BaseResultCode.FAIL.getCode(), message, null);
	}

	public static <T> R<T> fail(T data) {
		return result(BaseResultCode.FAIL, data);
	}

	public static <T> R<T> fail(int code, String message) {
		return result(code, message, null);
	}

	public static <T> R<T> fail(String message, T data) {
		return result(BaseResultCode.FAIL.getCode(), message, data);
	}

	public static <T> R<T> fail(ResultCode failMsg, String message) {
		return result(failMsg.getCode(), message, null);
	}

	// result

	public static <T> R<T> result(int code, String message, T data) {
		return new R<T>().setData(data).setCode(code).setMessage(message);
	}

	public static <T> R<T> result(BaseResultCode resultCode, T data) {
		return new R<T>().setData(data).setCode(resultCode.getCode()).setMessage(resultCode.getMessage());
	}

	@JsonIgnore
	public boolean isOK() {
		return BaseResultCode.SUCCESS.getCode().equals(code);
	}

	@JsonIgnore
	public boolean isFailed() {
		return !isOK();
	}

}
