package com.schilings.neiko.common.core.request.wrapper;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * <pre>
 * <p>Request包装类：允许 body 重复读取</p>
 * <p>{@link HttpServletRequestWrapper}该父类有足够多的可用API</p>
 * </pre>
 *
 * @author Schilings
 */
@Slf4j
public class RepeatBodyRequestWrapper extends HttpServletRequestWrapper {

	/**
	 * 请求体
	 */
	@Getter
	protected final byte[] body;

	/**
	 * 返回此请求参数的 java.util.Map。 请求参数是随请求发送的额外信息。对于 HTTP servlet，参数包含在查询字符串或发布的表单数据中。
	 */
	protected final Map<String, String[]> parameterMap;

	/**
	 * Constructs a request object wrapping the given request.
	 * @param request the {@link HttpServletRequest} to be wrapped.
	 * @throws IllegalArgumentException if the request is null
	 */
	public RepeatBodyRequestWrapper(HttpServletRequest request) {
		super(request);
		this.parameterMap = super.getParameterMap();
		this.body = getByteBody(request);
	}

	/**
	 * 解析请求体
	 * @param request
	 * @return
	 */
	private static byte[] getByteBody(HttpServletRequest request) {
		byte[] body = new byte[0];
		try {
			body = StreamUtils.copyToByteArray(request.getInputStream());
		}
		catch (IOException e) {
			log.error("解析流中数据异常", e);
		}
		return body;
	}

	@Override
	public BufferedReader getReader() {
		return ObjectUtils.isEmpty(body) ? null : new BufferedReader(new InputStreamReader(getInputStream()));
	}

	@Override
	public ServletInputStream getInputStream() {
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
		return new ServletInputStream() {
			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
				// doNoting
			}

			@Override
			public int read() {
				return byteArrayInputStream.read();
			}
		};
	}

	/**
	 * 重写 getParameterMap() 方法 解决 undertow 中流被读取后，会进行标记，从而导致无法正确获取 body 中的表单数据的问题
	 * @see io.undertow.servlet.spec.HttpServletRequestImpl#readStarted
	 * @return Map<String, String[]> parameterMap
	 */
	@Override
	public Map<String, String[]> getParameterMap() {
		return parameterMap;
	}

}
