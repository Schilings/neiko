package com.schilings.neiko.autoconfigure.web.api;

import com.schilings.neiko.autoconfigure.web.api.annotation.ApiVersion;
import com.schilings.neiko.autoconfigure.web.api.condition.ApiVesrsionCondition;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * @author Schilings <pre>{@code
 *
 * }
 * <p>{@link RequestMappingHandlerMapping}实现了InitializingBean接口的，所以Bena初始化就已经识别好了HandlerMethod</p>
 * </pre>
 */
@Deprecated
public class ApiRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

	/**
	 * 给定类型是否是具有处理程序方法的处理程序。 期望处理程序具有类型级别的Controller注释或类型级别的RequestMapping注释
	 * @param beanType
	 * @return
	 */
	@Override
	protected boolean isHandler(Class<?> beanType) {
		boolean f = super.isHandler(beanType);
		// 下面不需要，不然原本的RequestMapping逻辑无法实现
		// boolean hasClz = AnnotatedElementUtils.hasAnnotation(beanType,
		// ApiVersion.class);
		// Set<Method> methods = MethodIntrospector.selectMethods(beanType,
		// (ReflectionUtils.MethodFilter) method -> {
		// return AnnotatedElementUtils.hasAnnotation(method, ApiVersion.class);
		// });
		// return (f && (!methods.isEmpty() || hasClz));
		return f;
	}

	/**
	 * 见RequestMappingInfo，这里面有多个RequestCondition实现类，借鉴借鉴
	 */
	/**
	 * 提供自定义类型级别的请求条件。自定义RequestCondition可以是任何类型，只要所有调用该方法返回的条件类型相同，以确保自定义请求条件可以组合和比较。
	 * 考虑为自定义条件类型扩展AbstractRequestCondition并使用CompositeRequestCondition提供多个自定义条件。
	 * @param handlerType 为其创建条件的处理程序类型
	 * @return 条件，或null
	 */
	@Override
	protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
		ApiVersion apiVersion = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
		return createCondition(apiVersion);
	}

	/**
	 * 提供自定义方法级请求条件。自定义RequestCondition可以是任何类型，只要所有调用该方法返回的条件类型相同，以确保自定义请求条件可以组合和比较。
	 * 考虑为自定义条件类型扩展AbstractRequestCondition并使用CompositeRequestCondition提供多个自定义条件。
	 * @param method 为其创建条件的处理程序方法
	 * @return 条件，或nul
	 */
	@Override
	protected RequestCondition<?> getCustomMethodCondition(Method method) {
		ApiVersion apiVersion = AnnotationUtils.findAnnotation(method, ApiVersion.class);
		return createCondition(apiVersion);

	}

	private RequestCondition<ApiVesrsionCondition> createCondition(ApiVersion apiVersion) {
		return apiVersion == null ? null : new ApiVesrsionCondition(
				ApiVesrsionCondition.versionStrToNum(apiVersion.value()), apiVersion.value());
	}

}
