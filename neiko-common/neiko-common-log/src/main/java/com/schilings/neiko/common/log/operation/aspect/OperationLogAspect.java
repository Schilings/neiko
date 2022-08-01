package com.schilings.neiko.common.log.operation.aspect;

import com.schilings.neiko.common.log.operation.annotation.OperationLogging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.schilings.neiko.common.log.operation.handler.OperationLogHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class OperationLogAspect<T> {

	private final OperationLogHandler<T> handler;

	@Pointcut("execution(@(@com.schilings.neiko.common.log.operation.annotation.OperationLogging *) * *(..)) "
			+ "|| @annotation(com.schilings.neiko.common.log.operation.annotation.OperationLogging)")
	public void pointCut() {
		// do nothing
	}

	@Around("pointCut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		// 开始时间
		long startTime = System.nanoTime();

		// 获取目标方法
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		// 获取注解
		OperationLogging annotation = AnnotatedElementUtils.getMergedAnnotation(method, OperationLogging.class);
		Assert.notNull(annotation, "OperationLogging annotation should not be null!");

		// 创建日志dto
		T logDto = handler.buildLog(annotation, joinPoint);
		Assert.notNull(logDto, "OperationLogging Log must not be null. ");

		Throwable throwable = null;
		Object result = null;
		try {
			result = joinPoint.proceed();
			return result;
		}
		catch (Throwable e) {
			throwable = e;
			throw e;
		}
		finally {
			// 是否保存响应内容
			boolean isSaveResult = annotation.recordResult();
			// 操作日志记录处理
			handleLog(joinPoint, startTime, logDto, throwable, isSaveResult, result);
		}

	}

	private void handleLog(ProceedingJoinPoint joinPoint, long startTime, T logDto, Throwable throwable,
			boolean isSaveResult, Object result) {
		try {
			// 结束时间
			long executionTime = System.nanoTime() - startTime;
			// 记录执行信息
			handler.recordExecutionInfo(logDto, joinPoint, TimeUnit.NANOSECONDS.toSeconds(executionTime), throwable,
					isSaveResult, result);
			// 处理操作日志
			handler.handleLog(logDto);
		}
		catch (Exception e) {
			log.error("记录操作日志异常：{}", logDto);
		}
	}

}
