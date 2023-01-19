package com.schilings.neiko.sample.oauth2.web;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@Order(value = Ordered.LOWEST_PRECEDENCE) // 默认最低优先级
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalHandlerExceptionResolver {


	public static final String PROD_ERR_MSG = "系统异常，请联系管理员";

	public static final String NLP_MSG = "空指针异常!";

	/**
	 * 全局异常捕获
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Exception handleGlobalException(Exception e, HttpServletRequest request) {
		log.error("请求地址: {}, 全局异常信息 ex={}", request.getRequestURI(), e.getMessage(), e);
		return e;
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Exception accessDeniedException(AccessDeniedException e, HttpServletRequest request) {
		log.error("请求地址: {}, AccessDeniedException异常信息 ex={}", request.getRequestURI(), e.getMessage(), e);
		return e;
	}

	@ExceptionHandler(AuthenticationException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Exception authenticationException(AuthenticationException e, HttpServletRequest request) {
		log.error("请求地址: {}, AccessDeniedException异常信息 ex={}", request.getRequestURI(), e.getMessage(), e);
		return e;
	}
}
