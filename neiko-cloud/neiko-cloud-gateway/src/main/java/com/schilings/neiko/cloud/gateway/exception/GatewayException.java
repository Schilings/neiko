package com.schilings.neiko.cloud.gateway.exception;

import com.schilings.neiko.common.core.exception.BaseException;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_GATEWAY)
public class GatewayException extends BaseException {

	@Getter
	@Setter
	private HttpStatus httpStatus;

	public GatewayException(String message) {
		super(message);
		this.httpStatus = HttpStatus.BAD_GATEWAY;
	}

	public int getCode() {
		return this.httpStatus.value();
	}

}
