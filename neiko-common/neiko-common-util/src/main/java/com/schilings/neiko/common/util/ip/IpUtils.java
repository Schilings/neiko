package com.schilings.neiko.common.util.ip;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.extra.servlet.ServletUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletRequest;

/**
 * <pre>{@code
 *
 * }
 * <p>IP工具类</p>
 * </pre>
 *
 * @author Schilings
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IpUtils {

	/**
	 * 如果在前端和服务端中间还有一层Node服务 在Node对前端数据进行处理并发起新请求时，需携带此头部信息 便于获取真实IP
	 */
	public static final String NODE_FORWARDED_IP = "Node-Forwarded-IP";

	/**
	 * 获取客户端IP
	 */
	public static String getIpAddr(HttpServletRequest request) {
		return getIpAddr(request, NODE_FORWARDED_IP);
	}

	/**
	 * 获取客户端IP
	 *
	 * 参考 huTool 稍微调整了下headers 顺序
	 */
	public static String getIpAddr(HttpServletRequest request, String... otherHeaderNames) {
		String[] headers = { "X-Real-IP", "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP",
				"HTTP_X_FORWARDED_FOR" };
		if (ArrayUtil.isNotEmpty(otherHeaderNames)) {
			headers = ArrayUtil.addAll(headers, otherHeaderNames);
		}
		return getClientIPByHeader(request, headers);
	}

	public static String getClientIPByHeader(HttpServletRequest request, String... headerNames) {
		String ip;
		for (String header : headerNames) {
			ip = request.getHeader(header);
			if (!NetUtil.isUnknown(ip)) {
				return NetUtil.getMultistageReverseProxyIp(ip);
			}
		}

		ip = request.getRemoteAddr();
		return NetUtil.getMultistageReverseProxyIp(ip);
	}

}
