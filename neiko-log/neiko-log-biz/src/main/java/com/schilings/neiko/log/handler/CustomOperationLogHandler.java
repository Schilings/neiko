package com.schilings.neiko.log.handler;


import com.schilings.neiko.common.log.operation.annotation.OperationLogging;
import com.schilings.neiko.common.log.operation.handler.AbstractOperationLogHandler;
import com.schilings.neiko.log.model.entity.OperationLog;
import com.schilings.neiko.log.service.OperationLogService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;

@RequiredArgsConstructor
public class CustomOperationLogHandler extends AbstractOperationLogHandler<OperationLog> {

    private final OperationLogService operationLogService;
    
    @Override
    public OperationLog buildLog(OperationLogging operationLogging, ProceedingJoinPoint joinPoint) {
        return null;
    }

    @Override
    public OperationLog recordExecutionInfo(OperationLog log, ProceedingJoinPoint joinPoint, long executionTime, Throwable throwable, boolean isSaveResult, Object result) {
        return null;
    }

    @Override
    public void handleLog(OperationLog operationLog) {

    }
}
