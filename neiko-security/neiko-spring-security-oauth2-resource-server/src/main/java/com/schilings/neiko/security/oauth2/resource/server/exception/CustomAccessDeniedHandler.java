package com.schilings.neiko.security.oauth2.resource.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * {@link AccessDeniedException}
 * </p>
 * <p>
 * {@link BearerTokenAccessDeniedHandler}
 * </p>
 *
 * @author Schilings
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		String utf8 = StandardCharsets.UTF_8.toString();

		response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.setCharacterEncoding(utf8);
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.getWriter().write("未授权");

	}

}
