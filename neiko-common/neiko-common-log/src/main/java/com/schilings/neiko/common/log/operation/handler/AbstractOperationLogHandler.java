package com.schilings.neiko.common.log.operation.handler;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import com.schilings.neiko.common.util.json.JsonUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class AbstractOperationLogHandler<T> implements OperationLogHandler<T> {

	@Getter
	private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

	/**
	 * <p>
	 * 忽略记录的参数类型列表
	 * </p>
	 * 忽略判断时只针对方法入参类型，如果入参为对象，其某个属性需要忽略的无法处理，可以使用 @JsonIgnore 进行忽略。
	 */
	private final List<Class<?>> ignoredParamClasses = ListUtil.toList(ServletRequest.class, ServletResponse.class,
			MultipartFile.class);

	/**
	 * 添加忽略记录的参数类型
	 * @param clazz 参数类型
	 */
	public void addIgnoredParamClass(Class<?> clazz) {
		ignoredParamClasses.add(clazz);
	}

	/**
	 * 当前执行对象
	 * @param joinPoint
	 * @return
	 */
	public Object getCurrentBean(ProceedingJoinPoint joinPoint) {
		return joinPoint.getThis();
	}

	/**
	 * 当前执行方法
	 * @param joinPoint
	 * @return
	 */
	public Method getMethod(ProceedingJoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		return methodSignature.getMethod();
	}

	/**
	 * 返回类型
	 * @param joinPoint
	 * @return
	 */
	public Class getReturnType(ProceedingJoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		return ((MethodSignature) signature).getReturnType();
	}

	/**
	 * 解析参数 key:value
	 * @param joinPoint
	 * @return
	 */
	public String getParams(ProceedingJoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		String strClassName = joinPoint.getTarget().getClass().getName();
		String strMethodName = signature.getName();
		log.debug("[getParams]，获取方法参数[类名]:{},[方法]:{}", strClassName, strMethodName);

		MethodSignature methodSignature = (MethodSignature) signature;
		String[] parameterNames = methodSignature.getParameterNames();
		Class[] parameterTypes = methodSignature.getParameterTypes();
		Object[] args = joinPoint.getArgs();
		if (ArrayUtil.isEmpty(parameterNames)) {
			return null;
		}
		Map<String, Object> paramsMap = new HashMap<>();
		for (int i = 0; i < parameterNames.length; i++) {
			Object arg = args[i];
			if (arg == null) {
				paramsMap.put(parameterNames[i], null);
				continue;
			}
			Class<?> argClass = arg.getClass();
			// 忽略部分类型的参数记录
			for (Class<?> ignoredParamClass : ignoredParamClasses) {
				if (ignoredParamClass.isAssignableFrom(argClass)) {
					arg = "ignored param type: " + argClass;
					break;
				}
			}
			paramsMap.put(parameterNames[i], arg);
		}

		String params = "";
		try {
			// 入参类中的属性可以通过注解进行数据落库脱敏以及忽略等操作
			params = JsonUtils.toJson(paramsMap);
		}
		catch (Exception e) {
			log.error("[getParams]，获取方法参数异常，[类名]:{},[方法]:{}", strClassName, strMethodName, e);
		}

		return params;
	}

}
