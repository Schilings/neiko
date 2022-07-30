package com.schilings.neiko.autoconfigure.web.exception;

import com.schilings.neiko.autoconfigure.web.exception.enums.ExceptionHandleTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Data
@ConfigurationProperties(prefix = ExceptionHandleProperties.PREFIX)
public class ExceptionHandleProperties {

	public static final String PREFIX = "neiko.web.exception";

	/**
	 * 处理类型,默认不通知
	 */
	private ExceptionHandleTypeEnum type = ExceptionHandleTypeEnum.NONE;

	/**
	 * 忽略指定异常，请注意：只会忽略填写的异常类，而不会忽略该异常类的子类
	 */
	private Set<Class<? extends Throwable>> ignoreExceptions = new HashSet<>();

	/**
	 * 通知间隔时间 单位秒 默认 5分钟
	 */
	private long time = TimeUnit.MINUTES.toSeconds(5);

	/**
	 * 消息阈值 即便间隔时间没有到达设定的时间， 但是异常发生的数量达到阈值 则立即发送消息
	 */
	private int max = 5;

	/**
	 * 堆栈转string 的长度
	 */
	private int length = 3000;

	/**
	 * 接收异常通知邮件的邮箱
	 */
	private Set<String> receiveEmails = new HashSet<>();

}
