package com.schilings.neiko.common.core.request.wrapper;

import com.schilings.neiko.common.util.json.JsonUtils;
import com.schilings.neiko.common.util.json.TypeReference;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * <pre>
 * <p>Request包装类：解析请求中的Json，路径Param与Json请求体并存</p>
 * </pre>
 *
 * @author Schilings
 */
@Slf4j
public class RepeatJsonBodyRequestWrapper extends RepeatBodyRequestWrapper {

	@Getter
	protected final String jsonStr;

	@Getter
	protected final Map<String, Object> jsonMap;

	/**
	 * Constructs a request object wrapping the given request.
	 * @param request the {@link HttpServletRequest} to be wrapped.
	 * @throws IllegalArgumentException if the request is null
	 */
	public RepeatJsonBodyRequestWrapper(HttpServletRequest request) {
		super(request);
		this.jsonStr = toJsonStr(body);
		this.jsonMap = toJsonMap(jsonStr);
	}

	public RepeatJsonBodyRequestWrapper(RepeatBodyRequestWrapper request) {
		super(request);
		this.jsonStr = toJsonStr(request.getBody());
		this.jsonMap = toJsonMap(jsonStr);
	}

	private Map<String, Object> toJsonMap(String jsonStr) {
		try {
			return JsonUtils.toObj(jsonStr, new TypeReference<Map<String, Object>>() {
			});
		}
		catch (Exception e) {
			log.error("解析Json中数据异常", e);
		}
		return Collections.emptyMap();
	}

	private String toJsonStr(byte[] body) {
		try {
			return new String(body, getRequest().getCharacterEncoding());
		}
		catch (IOException e) {
			log.error("解析流中数据异常", e);
		}
		return "";
	}

}
