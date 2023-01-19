package com.schilings.neiko.autoconfigure.web.exception.resolver;

import com.schilings.neiko.autoconfigure.web.exception.handler.GlobalExceptionHandler;
import com.schilings.neiko.common.core.exception.ServiceException;
import com.schilings.neiko.common.model.constants.GlobalConstants;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.common.model.result.SystemResultCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;

import static com.schilings.neiko.common.model.result.BaseResultCode.LOGIC_CHECK_ERROR;

@Slf4j
@Order(value = Ordered.LOWEST_PRECEDENCE) // 默认最低优先级
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalHandlerExceptionResolver {

	private final GlobalExceptionHandler globalExceptionHandler;

	@Value("${spring.profiles.active:prod}")
	private String profile;

	public static final String PROD_ERR_MSG = "系统异常，请联系管理员";

	public static final String NLP_MSG = "空指针异常!";

	/**
	 * 全局异常捕获
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R<String> handleGlobalException(Exception e, HttpServletRequest request) {
		log.error("请求地址: {}, 全局异常信息 ex={}", request.getRequestURI(), e.getMessage(), e);
		globalExceptionHandler.handle(e);
		// 当为生产环境, 不适合把具体的异常信息展示给用户, 比如数据库异常信息.
		String errorMessage = GlobalConstants.ENV_PROD.equals(profile) ? PROD_ERR_MSG : e.getLocalizedMessage();
		return R.fail(SystemResultCode.SERVER_ERROR, errorMessage);
	}

	/**
	 * 空指针异常捕获
	 */
	@ExceptionHandler(NullPointerException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R<String> handleNullPointerException(NullPointerException e, HttpServletRequest request) {
		log.error("请求地址: {}, 空指针异常 ex={}", request.getRequestURI(), e.getMessage(), e);
		globalExceptionHandler.handle(e);
		// 当为生产环境, 不适合把具体的异常信息展示给用户, 比如数据库异常信息.
		String errorMessage = GlobalConstants.ENV_PROD.equals(profile) ? PROD_ERR_MSG : NLP_MSG;
		return R.fail(SystemResultCode.SERVER_ERROR, errorMessage);
	}

	/**
	 * MethodArgumentTypeMismatchException 参数类型转换异常
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public R<String> handleMethodArgumentTypeMismatchException(Exception e, HttpServletRequest request) {
		log.error("请求地址: {}, 请求入参异常 ex={}", request.getRequestURI(), e.getMessage(), e);
		globalExceptionHandler.handle(e);
		String errorMessage = GlobalConstants.ENV_PROD.equals(profile) ? PROD_ERR_MSG : e.getMessage();
		return R.fail(SystemResultCode.BAD_REQUEST, errorMessage);
	}

	/**
	 * 请求方式有问题 - MediaType 异常 - Method 异常
	 */
	@ExceptionHandler({ HttpMediaTypeNotSupportedException.class, HttpRequestMethodNotSupportedException.class })
	public R<String> requestNotSupportedException(Exception e, HttpServletRequest request) {
		log.error("请求地址: {}, 请求方式异常 ex={}", request.getRequestURI(), e.getMessage(), e);
		globalExceptionHandler.handle(e);
		return R.fail(SystemResultCode.BAD_REQUEST, e.getLocalizedMessage());
	}

	/**
	 * IllegalArgumentException 异常捕获，主要用于Assert
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<String> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
		log.error("请求地址: {}, 非法数据输入 ex={}", request.getRequestURI(), e.getMessage(), e);
		globalExceptionHandler.handle(e);
		return R.fail(SystemResultCode.BAD_REQUEST, e.getMessage());
	}

	/**
	 * 校验参数异常 validation Exception
	 */
	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<String> handleBodyValidException(BindException e, HttpServletRequest request) {
		BindingResult bindingResult = e.getBindingResult();
		String errorMsg = bindingResult.getErrorCount() > 0 ? bindingResult.getAllErrors().get(0).getDefaultMessage()
				: "未获取到错误信息!";
		log.error("请求地址: {}, 参数绑定异常 ex={}", request.getRequestURI(), errorMsg);
		globalExceptionHandler.handle(e);

		return R.fail(SystemResultCode.BAD_REQUEST, errorMsg);
	}

	/**
	 * 单体参数校验异常 validation Exception
	 */
	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R<String> handleValidationException(ValidationException e, HttpServletRequest request) {
		log.error("请求地址: {}, 参数校验异常 ex={}", request.getRequestURI(), e.getMessage());
		globalExceptionHandler.handle(e);
		return R.fail(SystemResultCode.BAD_REQUEST, e.getLocalizedMessage());
	}

	/**
	 * SpringMVC 参数不正确
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = MissingServletRequestParameterException.class)
	public R<String> missingServletRequestParameterExceptionHandler(HttpServletRequest req,
			MissingServletRequestParameterException e) {
		log.error("请求地址: {}, 请求入参异常 ex={}", req.getRequestURI(), e.getMessage(), e);
		globalExceptionHandler.handle(e);
		return R.fail(SystemResultCode.BAD_REQUEST, e.getMessage());
	}

	/**
	 * 自定义业务异常捕获 业务异常响应码推荐使用200 用 result 结构中的code做为业务错误码标识
	 */
	@ExceptionHandler(ServiceException.class)
	@ResponseStatus(HttpStatus.OK)
	public R<String> handleBallCatException(ServiceException e, HttpServletRequest request) {
		log.error("请求地址: {}, 业务异常信息 ex={}", request.getRequestURI(), e.getMessage());
		globalExceptionHandler.handle(e);
		return R.fail(e.getCode(), e.getMessage());
	}

}
