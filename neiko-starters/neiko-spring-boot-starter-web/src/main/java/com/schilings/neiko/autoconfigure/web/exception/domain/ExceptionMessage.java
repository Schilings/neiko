package com.schilings.neiko.autoconfigure.web.exception.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Builder
@Accessors(chain = true)
public class ExceptionMessage {

	/**
	 * 用于筛选重复异常
	 */
	private String key;

	/**
	 * 消息
	 */
	private String message;

	/**
	 * 数量
	 */
	private int number;

	/**
	 * 堆栈
	 */
	private String stack;

	/**
	 * 最新的触发时间
	 */
	private String time;

	/**
	 * 机器地址
	 */
	private String mac;

	/**
	 * 线程id
	 */
	private long threadId;

	/**
	 * 服务名
	 */
	private String applicationName;

	/**
	 * hostname
	 */
	private String hostname;

	/**
	 * ip
	 */
	private String ip;

	/**
	 * 请求地址
	 */
	private String requestUri;

	/**
	 * 数量自增
	 * @author lingting 2020-09-03 20:31
	 */
	public ExceptionMessage increment() {
		number++;
		return this;
	}

	@Override
	public String toString() {
		return "服务名称：" + applicationName + "\nip：" + ip + "\nhostname：" + hostname + "\n机器地址：" + mac + "\n触发时间：" + time
				+ "\n请求地址：" + requestUri + "\n线程id：" + threadId + "\n数量：" + number + "\n堆栈：" + stack;
	}

}
