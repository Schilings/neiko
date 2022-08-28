package com.schilings.neiko.samples.admin;

import com.schilings.neiko.common.log.access.handler.AccessLogHandler;
import com.schilings.neiko.common.log.operation.handler.OperationLogHandler;
import com.schilings.neiko.log.handler.CustomAccessLogHandler;
import com.schilings.neiko.log.handler.CustomOperationLogHandler;
import com.schilings.neiko.log.handler.LoginLogHandler;
import com.schilings.neiko.log.model.entity.AccessLog;
import com.schilings.neiko.log.model.entity.OperationLog;
import com.schilings.neiko.log.service.AccessLogService;
import com.schilings.neiko.log.service.LoginLogService;
import com.schilings.neiko.log.service.OperationLogService;
import com.schilings.neiko.log.thread.AccessLogSaveThread;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 日志处理类注册
 */
@Configuration(proxyBeanMethods = false)
public class LogHandlerConfig {

	/**
	 * 访问日志保存
	 * @param accessLogService 访问日志Service
	 * @return CustomAccessLogHandler
	 */
	@Bean
	@ConditionalOnBean(AccessLogService.class)
	@ConditionalOnClass(CustomAccessLogHandler.class)
	@ConditionalOnMissingBean(AccessLogHandler.class)
	public AccessLogHandler<AccessLog> customAccessLogHandler(AccessLogService accessLogService) {
		return new CustomAccessLogHandler(new AccessLogSaveThread(accessLogService));
	}

	/**
	 * 操作日志处理器
	 * @param operationLogService 操作日志Service
	 * @return CustomOperationLogHandler
	 */
	@Bean
	@ConditionalOnBean(OperationLogService.class)
	@ConditionalOnClass(CustomOperationLogHandler.class)
	@ConditionalOnMissingBean(OperationLogHandler.class)
	public OperationLogHandler<OperationLog> customOperationLogHandler(OperationLogService operationLogService) {
		return new CustomOperationLogHandler(operationLogService);
	}

	/**
	 * 登录日志处理，监听登录时间记录登录登出
	 * @param loginLogService 操作日志Service
	 * @return CustomOperationLogHandler
	 */
	@Bean
	@ConditionalOnBean(LoginLogService.class)
	@ConditionalOnMissingBean(LoginLogHandler.class)
	public LoginLogHandler loginLogHandler(LoginLogService loginLogService) {
		return new LoginLogHandler(loginLogService);
	}

}
