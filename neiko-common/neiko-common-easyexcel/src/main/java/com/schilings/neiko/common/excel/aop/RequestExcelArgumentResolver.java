package com.schilings.neiko.common.excel.aop;

import com.alibaba.excel.EasyExcel;
import com.schilings.neiko.common.excel.annotation.RequestExcel;
import com.schilings.neiko.common.excel.handler.response.converters.LocalDateStringConverter;
import com.schilings.neiko.common.excel.handler.response.converters.LocalDateTimeStringConverter;
import com.schilings.neiko.common.excel.handler.request.ListAnalysisEventListener;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestPartMethodArgumentResolver;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * <pre>
 * <p>{@link RequestPartMethodArgumentResolver}</p>
 * </pre>
 *
 * @author Schilings
 */
public class RequestExcelArgumentResolver extends AbstractMessageConverterMethodArgumentResolver {

	public RequestExcelArgumentResolver() {
		super(Collections.emptyList());

	}

	public RequestExcelArgumentResolver(List<HttpMessageConverter<?>> converters) {
		super(converters);
	}

	public RequestExcelArgumentResolver(List<HttpMessageConverter<?>> converters,
			List<Object> requestResponseBodyAdvice) {
		super(converters, requestResponseBodyAdvice);
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		// 注解
		if (parameter.hasParameterAnnotation(RequestExcel.class)) {
			// List类型
			Class<?> parameterType = parameter.getParameterType();
			if (!parameterType.isAssignableFrom(List.class)) {
				throw new IllegalArgumentException(
						"Excel upload request resolver error, @RequestExcel parameter is not List " + parameterType);
			}
			return true;
		}
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		// 请求
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		Assert.state(request != null, "No HttpServletRequest");
		// 注解
		RequestExcel requestExcel = parameter.getParameterAnnotation(RequestExcel.class);
		boolean isRequired = ((requestExcel == null || requestExcel.required()) && !parameter.isOptional());
		// 处理自定义read listener
		Class<? extends ListAnalysisEventListener<?>> readListenerClass = requestExcel.readListener();
		ListAnalysisEventListener<?> readListener = BeanUtils.instantiateClass(readListenerClass);
		// 文件名，不设置默认参数名
		parameter = parameter.nestedIfOptional();
		String name = getPartName(parameter, requestExcel);
		// 请求输入流
		InputStream inputStream = resolveMultipartRequest(name, parameter, request);
		if (inputStream == null && isRequired) {
			throw new IllegalArgumentException("Required request excel '" + name + "' is not present");
		}

		// 解析文件为List
		try {
			// 获取目标类型
			Type targetType = parameter.getGenericParameterType();
			Class<?> targetClass = (targetType instanceof Class ? (Class<?>) targetType : null);
			if (targetClass == null) {
				ResolvableType resolvableType = ResolvableType.forMethodParameter(parameter);
				targetClass = resolvableType.getGeneric(0).resolve();
			}

			// 这里需要指定读用哪个 class 去读，然后读取第一个 sheet 文件流会自动关闭
			EasyExcel.read(inputStream, targetClass, readListener)
					// 转换器
					.registerConverter(LocalDateStringConverter.INSTANCE)
					.registerConverter(LocalDateTimeStringConverter.INSTANCE)
					// 是否忽略空行
					.ignoreEmptyRow(requestExcel.ignoreEmptyRow()).sheet().doRead();

		}
		catch (Exception e) {
			if (isRequired) {
				throw e;
			}
		}
		// 监听器取出数据
		Object arg = readListener.getList();
		// 校验失败的数据处理 交给 BindResult
		if (binderFactory != null) {
			WebDataBinder binder = binderFactory.createBinder(webRequest, readListener.getErrors(), name);
			if (arg != null) {
				// 校验
				validateIfApplicable(binder, parameter);
				if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
					throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
				}
			}
			if (mavContainer != null) {
				mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
			}
		}
		// 为空但required
		if (arg == null && isRequired) {
			throw new IllegalArgumentException("Required request excel '" + name + "' is not present");
		}
		// 返回结果
		return adaptArgumentIfNecessary(arg, parameter);
	}

	/**
	 * 参数名
	 * @param methodParam
	 * @param requestExcel
	 * @return
	 */
	private String getPartName(MethodParameter methodParam, @Nullable RequestExcel requestExcel) {
		String partName = (requestExcel != null ? requestExcel.name() : "");
		if (partName.isEmpty()) {
			partName = methodParam.getParameterName();
			if (partName == null) {
				throw new IllegalArgumentException(
						"Request Excel name for argument type [" + methodParam.getNestedParameterType().getName()
								+ "] not specified, and parameter name information not found in class file either.");
			}
		}
		return partName;
	}

	/**
	 * 解析请求体
	 * @param name
	 * @param parameter
	 * @param request
	 * @return
	 */
	@SneakyThrows
	public InputStream resolveMultipartRequest(String name, MethodParameter parameter, HttpServletRequest request) {
		if (request instanceof MultipartRequest) {
			// 返回匹配的请求对象，如果没有该类型可用，则返回null
			MultipartHttpServletRequest multipartRequest = WebUtils.getNativeRequest(request,
					MultipartHttpServletRequest.class);
			// 判断是为MultipartFile
			boolean isMultipart = (multipartRequest != null || isMultipartContent(request));
			if (!isMultipart) {
				return request.getInputStream();
			}
			if (multipartRequest == null) {
				multipartRequest = new StandardMultipartHttpServletRequest(request);
			}
			return multipartRequest.getFile(name).getInputStream();

		}
		else {
			return request.getInputStream();
		}

	}

	/**
	 * 是否为文件上传
	 * @param request
	 * @return
	 */
	private static boolean isMultipartContent(HttpServletRequest request) {
		String contentType = request.getContentType();
		return (contentType != null && contentType.toLowerCase().startsWith("multipart/"));
	}

}
