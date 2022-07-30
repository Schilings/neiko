package com.schilings.neiko.autoconfigure.web.exception.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.schilings.neiko.autoconfigure.web.exception.ExceptionHandleProperties;
import com.schilings.neiko.autoconfigure.web.exception.domain.ExceptionMessage;
import com.schilings.neiko.autoconfigure.web.exception.domain.ExceptionNoticeResponse;
import com.schilings.neiko.common.util.web.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * <p>具有通知能力的异常处理，单独启动一个线程处理异常消息推送</p>
 * </pre>
 *
 * @author Schilings
 */

@Slf4j
public abstract class AbstractNoticeGlobalExceptionHandler extends Thread
		implements GlobalExceptionHandler, InitializingBean {

	private final BlockingQueue<Throwable> queue = new LinkedBlockingQueue<>();

	private static final String NULL_MESSAGE_KEY = "";

	protected final ExceptionHandleProperties config;

	/**
	 * 通知消息存放 e.message 堆栈信息
	 */
	private final Map<String, ExceptionMessage> messages;

	/**
	 * 本地物理地址
	 */
	private String mac;

	/**
	 * 本地hostname
	 */
	private String hostname;

	/**
	 * 本地ip
	 */
	private String ip;

	/**
	 * 请求地址
	 */
	private String requestUri;

	private final String applicationName;

	protected AbstractNoticeGlobalExceptionHandler(ExceptionHandleProperties config, String applicationName) {
		this.config = config;
		messages = new ConcurrentHashMap<>(config.getMax() * 2);
		this.applicationName = applicationName;
		try {
			InetAddress ia = InetAddress.getLocalHost();
			hostname = ia.getHostName();
			ip = ia.getHostAddress();

			byte[] macByte = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < macByte.length; i++) {
				sb.append(String.format("%02X%s", macByte[i], (i < macByte.length - 1) ? "-" : ""));
			}
			this.mac = sb.toString();
		}
		catch (Exception e) {
			mac = "获取失败!";
		}
	}

	@Override
	@SuppressWarnings("all")
	public void run() {
		String key;
		// 计时器，
		TimeInterval interval = new TimeInterval();
		long threadId = Thread.currentThread().getId();
		while (true) {
			int i = 0;
			// 若设定的时间间隔内异常数小于阈值
			// 直接发生的异常数大于阈值 或者 经过了一个时间间隔 就会直接处理全部的异常和重新计时
			while (i < config.getMax() && interval.intervalSecond() < config.getTime()) {
				Throwable t = null;
				try {
					// 从异常消息队列中取出消息，取不到就阻塞等待
					// 如果 i=0,即 当前未处理异常，则等待超时时间为 1 小时， 否则为 10 秒
					t = queue.poll(i == 0 ? TimeUnit.HOURS.toSeconds(1) : 10, TimeUnit.SECONDS);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				// 如果取到消息
				if (t != null) {
					key = t.getMessage() == null ? NULL_MESSAGE_KEY : t.getMessage();
					// i++
					if (i++ == 0) {
						// 第一次收到数据, 重置计时，
						interval.restart();
						messages.put(key, init(t).setKey(key).setThreadId(threadId));
					}
					else {
						// 相同异常
						if (messages.containsKey(key)) {
							messages.put(key, messages.get(key).increment());
						}
						else {
							messages.put(key, init(t).setKey(key).setThreadId(threadId));
						}
					}
				}
			}
			// 一次处理结束
			if (messages.size() > 0) {
				// 如果需要发送的消息不为空
				messages.forEach((k, v) -> {
					try {
						ExceptionNoticeResponse response = send(v);
						if (!response.isSuccess()) {
							log.error("消息通知发送失败! msg: {}", response.getErrMsg());
						}
					}
					catch (Exception e) {
						log.error("消息通知时发生异常", e);
					}
				});
				messages.clear();
			}
			interval.restart();
		}
	}

	public ExceptionMessage init(Throwable t) {
		return ExceptionMessage.builder().number(1).mac(mac).applicationName(applicationName).hostname(hostname).ip(ip)
				.requestUri(requestUri)
				.stack(ExceptionUtil.stacktraceToString(t, config.getLength()).replace("\\r", "")).time(DateUtil.now())
				.build();
	}

	/**
	 * 发送通知 模板方法
	 * @param sendMessage 发送的消息
	 * @return 返回消息发送状态，如果发送失败需要设置失败信息
	 * @author lingting 2020-06-12 00:37:23
	 */
	public abstract ExceptionNoticeResponse send(ExceptionMessage sendMessage);

	@Override
	public void handle(Throwable throwable) {
		try {
			this.requestUri = WebUtils.getRequest().getRequestURI();
			// 只有不是忽略的异常类才会插入异常消息队列
			if (!config.getIgnoreExceptions().contains(throwable.getClass())) {
				queue.put(throwable);
			}
		}
		catch (InterruptedException e) {
			// 中断
			Thread.currentThread().interrupt();
		}
		catch (Exception e) {
			log.error("往异常消息队列插入新异常时出错", e);
		}
	}

	@Override
	public void afterPropertiesSet() {
		this.setName("exception-notice");
		this.start();
	}

}
