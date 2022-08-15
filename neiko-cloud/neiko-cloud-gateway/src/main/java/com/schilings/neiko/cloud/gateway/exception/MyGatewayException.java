package com.schilings.neiko.cloud.gateway.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_GATEWAY, reason = "user-id字段不能为空")
public class MyGatewayException extends Exception{
}
