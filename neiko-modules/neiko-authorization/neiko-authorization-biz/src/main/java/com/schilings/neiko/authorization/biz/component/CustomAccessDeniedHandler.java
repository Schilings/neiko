package com.schilings.neiko.authorization.biz.component;


import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.common.model.result.SystemResultCode;
import com.schilings.neiko.common.util.json.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.getWriter().write(JsonUtils.toJson(R.result(SystemResultCode.FORBIDDEN, null)));
        response.getWriter().flush();
    }
}
