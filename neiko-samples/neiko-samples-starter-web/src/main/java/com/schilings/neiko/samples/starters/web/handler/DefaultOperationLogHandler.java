package com.schilings.neiko.samples.starters.web.handler;


import com.schilings.neiko.common.log.operation.annotation.OperationLogging;
import com.schilings.neiko.common.log.operation.handler.AbstractOperationLogHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Component
public class DefaultOperationLogHandler extends AbstractOperationLogHandler<String> {
    @Override
    public String buildLog(OperationLogging operationLogging, ProceedingJoinPoint joinPoint) {
        return getParams(joinPoint);
    }
    
    @Override
    public String recordExecutionInfo(String log, ProceedingJoinPoint joinPoint, long executionTime, Throwable throwable, boolean isSaveResult, Object result) {
        return log;
    }
    
    @Override
    public void handleLog(String operationLog) {
        System.out.println(operationLog);
    }
}
