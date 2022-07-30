package com.schilings.neiko.autoconfigure.log;

import com.schilings.neiko.common.log.operation.aspect.OperationLogAspect;
import com.schilings.neiko.common.log.operation.handler.OperationLogHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;


public class OperationLogAutoConfiguration {

	/**
	 * 注册操作日志Aspect
	 * @return OperationLogAspect
	 */
	@Bean
	@ConditionalOnBean(OperationLogHandler.class)
	public <T> OperationLogAspect<T> operationLogAspect(OperationLogHandler<T> operationLogHandler) {
		return new OperationLogAspect<>(operationLogHandler);
	}

}
