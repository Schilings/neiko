package com.schilings.neiko.common.excel.aop;

import com.schilings.neiko.common.excel.annotation.ResponseExcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.HttpHeadersReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.ViewMethodReturnValueHandler;

import javax.servlet.http.HttpServletResponse;

/**
 * <pre>
 * <p>{@link HttpHeadersReturnValueHandler} {@link ViewMethodReturnValueHandler}</p>
 * </pre>
 *
 * @author Schilings
 */
@Slf4j
public class ResponseExcelReturnValueHandler implements HandlerMethodReturnValueHandler {

	/**
	 * 只处理@ResponseExcel 声明的方法
	 * @param returnParameter
	 * @return
	 */
	@Override
	public boolean supportsReturnType(MethodParameter returnParameter) {
		return returnParameter.getMethodAnnotation(ResponseExcel.class) != null;
	}

	/**
	 * 处理逻辑
	 * @param returnValue
	 * @param returnParameter
	 * @param mavContainer
	 * @param webRequest
	 * @throws Exception
	 */
	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnParameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
		// 标识处理结束
		mavContainer.setRequestHandled(true);
		// response
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		Assert.state(response != null, "No HttpServletResponse");
		// 直接
		ResponseExcel responseExcel = returnParameter.getMethodAnnotation(ResponseExcel.class);
		Assert.state(responseExcel != null, "No @ResponseExcel");
		// 处理结果:response写入

	}

}
