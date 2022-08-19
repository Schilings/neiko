package com.schilings.neiko.log.handler;


import com.schilings.neiko.common.log.access.handler.AccessLogHandler;
import com.schilings.neiko.log.model.entity.AccessLog;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CustomAccessLogHandler implements AccessLogHandler<AccessLog> {


    private static final String APPLICATION_JSON = "application/json";
    
    
    @Override
    public AccessLog buildLog(HttpServletRequest request, HttpServletResponse response, Long executionTime, Throwable throwable) {
        return null;
    }

    @Override
    public void saveLog(AccessLog accessLog) {

    }
}
