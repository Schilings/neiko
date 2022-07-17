package com.schilings.neiko.autoconfigure.web.validation.exception.handler;


import com.schilings.neiko.autoconfigure.web.validation.exception.ValidationExceptionHandler;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.common.model.result.ServiceResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * <pre>{@code
 *      
 * }
 * <p>全局处理校验参数报错异常</p>
 * </pre>
 * @author Schilings
*/
@ControllerAdvice(annotations = {ValidationExceptionHandler.class})
@Slf4j
public class GlobalValidationExceptionHandler {

    /**
     * 处理 MissingServletRequestParameterException 异常
     *
     * SpringMVC 参数不正确
     */
    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public R missingServletRequestParameterExceptionHandler(HttpServletRequest req, MissingServletRequestParameterException ex) {
        log.debug("[missingServletRequestParameterExceptionHandler]", ex);
        // 包装结果
        return R.fail(ServiceResultCode.MISSING_REQUEST_PARAM_ERROR);
    }


    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public R handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder sb = new StringBuilder("校验失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append("：").append(fieldError.getDefaultMessage()).append(", ");
        }
        String msg = sb.toString();
        return R.fail(msg);
    }
    

    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public R constraintViolationExceptionHandler(HttpServletRequest req, ConstraintViolationException ex) {
        log.debug("[constraintViolationExceptionHandler]", ex);
        // 拼接错误
        StringBuilder detailMessage = new StringBuilder();
        for (ConstraintViolation<?> constraintViolation : ex.getConstraintViolations()) {
            // 使用 ; 分隔多个错误
            if (detailMessage.length() > 0) {
                detailMessage.append(";");
            }
            // 拼接内容到其中
            detailMessage.append(constraintViolation.getMessage());
        }
        // 包装 CommonResult 结果
        return R.fail(ServiceResultCode.INVALID_REQUEST_PARAM_ERROR.getCode(),
                ServiceResultCode.INVALID_REQUEST_PARAM_ERROR.getMessage() + ":" + detailMessage.toString());
    }

    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public R bindExceptionHandler(HttpServletRequest req, BindException ex) {
        log.debug("[bindExceptionHandler]", ex);
        // 拼接错误
        StringBuilder detailMessage = new StringBuilder();
        for (ObjectError objectError : ex.getAllErrors()) {
            // 使用 ; 分隔多个错误
            if (detailMessage.length() > 0) {
                detailMessage.append(";");
            }
            // 拼接内容到其中
            detailMessage.append(objectError.getDefaultMessage());
        }
        return R.fail(ServiceResultCode.INVALID_REQUEST_PARAM_ERROR.getCode(),
                ServiceResultCode.INVALID_REQUEST_PARAM_ERROR.getMessage() + ":" + detailMessage.toString());
    }
}
