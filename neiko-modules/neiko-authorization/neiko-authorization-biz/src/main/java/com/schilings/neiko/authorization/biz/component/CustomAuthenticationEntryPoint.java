package com.schilings.neiko.authorization.biz.component;

import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.common.model.result.SystemResultCode;
import com.schilings.neiko.common.util.json.JsonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		response.getWriter().write(JsonUtils.toJson(R.result(SystemResultCode.UNAUTHORIZED, null)));
		response.getWriter().flush();
	}

}
