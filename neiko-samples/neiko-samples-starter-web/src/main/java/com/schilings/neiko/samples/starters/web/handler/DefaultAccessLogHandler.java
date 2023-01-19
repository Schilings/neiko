package com.schilings.neiko.samples.starters.web.handler;

import com.schilings.neiko.common.core.request.wrapper.RepeatBodyRequestWrapper;
import com.schilings.neiko.common.log.access.handler.AccessLogHandler;
import com.schilings.neiko.common.util.json.JsonUtils;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class DefaultAccessLogHandler implements AccessLogHandler<String> {

	@Override
	public String buildLog(HttpServletRequest request, HttpServletResponse response, Long executionTime,
			Throwable throwable) {
		if (request instanceof RepeatBodyRequestWrapper) {
			RepeatBodyRequestWrapper requestWrapper = (RepeatBodyRequestWrapper) request;
			return JsonUtils.toJson(requestWrapper.getParameterMap());
		}
		return "";
	}

	@Override
	public void saveLog(String accessLog) {
		System.out.println(accessLog);
	}

}
