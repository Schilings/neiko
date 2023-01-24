package com.schilings.neiko.authorization.biz.handler;

import com.schilings.neiko.authorization.common.event.OAuth2TokenRevocationAuthenticationSuccessEvent;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.common.util.json.JsonUtils;
import com.schilings.neiko.security.oauth2.authorization.server.ApplicationEventAuthenticationSuccessHandler;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class OAuth2TokenRevocationAuthenticationSuccessHandler extends ApplicationEventAuthenticationSuccessHandler {

	public OAuth2TokenRevocationAuthenticationSuccessHandler(AuthenticationSuccessHandler delegate) {
		super(delegate);
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		getDelegate().onAuthenticationSuccess(request, response, authentication);
		getApplicationEventPublisher()
				.publishEvent(new OAuth2TokenRevocationAuthenticationSuccessEvent(authentication));
		response.setContentType(MediaType.APPLICATION_JSON.getType());
		response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		response.getWriter().write(JsonUtils.toJson(R.ok()));
		response.getWriter().flush();
	}

}
