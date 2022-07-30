package com.schilings.neiko.autoconfigure.web.exception;

import com.schilings.neiko.autoconfigure.mail.sender.MailSender;
import com.schilings.neiko.autoconfigure.web.exception.handler.DefaultGlobalExceptionHandler;
import com.schilings.neiko.autoconfigure.web.exception.handler.GlobalExceptionHandler;
import com.schilings.neiko.autoconfigure.web.exception.handler.MailGlobalExceptionHandler;
import com.schilings.neiko.autoconfigure.web.exception.resolver.GlobalHandlerExceptionResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
@AutoConfiguration
@EnableConfigurationProperties(ExceptionHandleProperties.class)
public class ExceptionAutoConfiguration {

	@Value("${spring.application.name: unknown-application}")
	private String applicationName;

	/**
	 * 默认的日志处理器
	 * @return DefaultExceptionHandler
	 */
	@Bean
	@ConditionalOnMissingBean(GlobalExceptionHandler.class)
	@ConditionalOnProperty(prefix = "neiko.web.exception", matchIfMissing = true, name = "type", havingValue = "NONE")
	public GlobalExceptionHandler defaultGlobalExceptionHandler() {
		return new DefaultGlobalExceptionHandler();
	}

	/**
	 * 邮件消息通知的日志处理器
	 *
	 * @author lingting 2020-06-12 00:32:40
	 */
	@Bean
	@ConditionalOnMissingBean(GlobalExceptionHandler.class)
	@ConditionalOnProperty(prefix = "neiko.web.exception", name = "type", havingValue = "MAIL")
	public GlobalExceptionHandler mailGlobalExceptionHandler(ExceptionHandleProperties exceptionHandleProperties,
			ApplicationContext context) {
		return new MailGlobalExceptionHandler(exceptionHandleProperties, context.getBean(MailSender.class),
				applicationName);
	}

	/**
	 * 默认的异常处理器
	 * @return GlobalHandlerExceptionResolver
	 */
	@Bean
	@ConditionalOnMissingBean(GlobalHandlerExceptionResolver.class)
	public GlobalHandlerExceptionResolver globalExceptionHandlerResolver(
			GlobalExceptionHandler globalExceptionHandler) {
		return new GlobalHandlerExceptionResolver(globalExceptionHandler);
	}

}
