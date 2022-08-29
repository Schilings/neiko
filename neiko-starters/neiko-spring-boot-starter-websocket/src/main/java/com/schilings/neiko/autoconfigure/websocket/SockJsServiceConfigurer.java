package com.schilings.neiko.autoconfigure.websocket;

import org.springframework.web.socket.config.annotation.SockJsServiceRegistration;

/**
 * SockJsService 配置类
 */
public interface SockJsServiceConfigurer {

	/**
	 * 配置 sockjs 相关
	 * @param sockJsServiceRegistration sockJsService 注册类
	 */
	void config(SockJsServiceRegistration sockJsServiceRegistration);

}
