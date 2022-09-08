package com.schilings.neiko.auth.handler;

import cn.dev33.satoken.exception.*;
import cn.dev33.satoken.oauth2.exception.SaOAuth2Exception;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.common.model.result.SystemResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(value = Ordered.LOWEST_PRECEDENCE - 10)
@RestControllerAdvice
@RequiredArgsConstructor
public class AuthHandlerExceptionResolver {

	/**
	 * 未登录
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(NotLoginException.class)
	public R notLogin(NotLoginException exception) {
		return R.fail(SystemResultCode.UNAUTHORIZED, exception.getMessage());
	}

	/**
	 * 无角色
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(NotRoleException.class)
	public R notRole(NotRoleException exception) {
		return R.fail(SystemResultCode.FORBIDDEN, exception.getMessage());
	}

	/**
	 * 无权限
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(NotPermissionException.class)
	public R notPermission(NotPermissionException exception) {
		return R.fail(SystemResultCode.FORBIDDEN, exception.getMessage());
	}

	/**
	 * 无安全
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(NotSafeException.class)
	public R notSafe(NotSafeException exception) {
		return R.fail(SystemResultCode.FORBIDDEN, exception.getMessage());
	}

	/**
	 * 无BasicAuth
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(NotBasicAuthException.class)
	public R notBasicAuth(NotBasicAuthException exception) {
		return R.fail(SystemResultCode.FORBIDDEN, exception.getMessage());
	}

	/**
	 * OAuth2认证流程错误
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(SaOAuth2Exception.class)
	public R saOauth2(SaOAuth2Exception exception) {
		return R.fail(SystemResultCode.BAD_REQUEST, exception.getMessage());
	}

	/**
	 * Sa-Token其他异常错误
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(SaTokenException.class)
	public R saToken(SaTokenException exception) {
		return R.fail(SystemResultCode.BAD_REQUEST, exception.getMessage());
	}

	/**
	 * SecurityException错误
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(SecurityException.class)
	public R security(SecurityException exception) {
		return R.fail(SystemResultCode.BAD_REQUEST, exception.getMessage());
	}

}
