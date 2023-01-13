package com.schilings.neiko.log.handler;

import cn.hutool.core.util.URLUtil;
import com.schilings.neiko.authorization.common.util.SecurityUtils;
import com.schilings.neiko.common.log.constants.LogConstant;
import com.schilings.neiko.common.log.operation.annotation.OperationLogging;
import com.schilings.neiko.common.log.operation.enums.LogStatusEnum;
import com.schilings.neiko.common.log.operation.handler.AbstractOperationLogHandler;
import com.schilings.neiko.common.util.ip.IpUtils;
import com.schilings.neiko.common.util.json.JsonUtils;
import com.schilings.neiko.common.util.web.WebUtils;

import com.schilings.neiko.log.model.entity.OperationLog;
import com.schilings.neiko.log.service.OperationLogService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 *
 * <p>
 * 操作日志处理类
 * </p>
 *
 * @author Schilings
 */
@RequiredArgsConstructor
public class CustomOperationLogHandler extends AbstractOperationLogHandler<OperationLog> {

	private final OperationLogService operationLogService;

	/**
	 * 解析请求与注解生成日志实体
	 * @param operationLogging 操作日志注解
	 * @param joinPoint 当前执行方法的切点信息
	 * @return
	 */
	@Override
	public OperationLog buildLog(OperationLogging operationLogging, ProceedingJoinPoint joinPoint) {
		// 获取Request
		HttpServletRequest request = WebUtils.getRequest();

		// @formatter:off
		OperationLog operationLog = new OperationLog()
				.setCreateTime(LocalDateTime.now())
				.setIp(IpUtils.getIpAddr(request))
				.setMethod(request.getMethod())
				.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT))
				.setUri(URLUtil.getPath(request.getRequestURI()))
				.setType(operationLogging.type().getValue())
				.setMsg(operationLogging.msg())
				.setTraceId(MDC.get(LogConstant.TRACE_ID));
		// @formatter:on

		// 请求参数
		if (operationLogging.recordParams()) {
			operationLog.setParams(getParams(joinPoint));
		}

		// 操作用户
		Optional.ofNullable(SecurityUtils.getUser()).ifPresent(x -> operationLog.setOperator(x.getUsername()));

		return operationLog;
	}

	/**
	 * 对日志实体补充记录操作信息
	 * @param operationLog
	 * @param joinPoint 当前执行方法的切点信息
	 * @param executionTime 方法执行时长
	 * @param throwable 方法执行的异常，为 null 则表示无异常
	 * @param isSaveResult 是否记录返回值
	 * @param result 方法执行的返回值
	 * @return
	 */
	@Override
	public OperationLog recordExecutionInfo(OperationLog operationLog, ProceedingJoinPoint joinPoint,
			long executionTime, Throwable throwable, boolean isSaveResult, Object result) {
		// 执行时长
		operationLog.setTime(executionTime);
		// 执行状态
		LogStatusEnum logStatusEnum = throwable == null ? LogStatusEnum.SUCCESS : LogStatusEnum.FAIL;
		operationLog.setStatus(logStatusEnum.getValue());
		// 执行结果
		if (isSaveResult) {
			Optional.ofNullable(result).ifPresent(x -> operationLog.setResult(JsonUtils.toJson(x)));
		}
		return operationLog;
	}

	/**
	 * 对操作实体进行处理（落库等）
	 * @param operationLog 操作日志
	 */
	@Override
	public void handleLog(OperationLog operationLog) {
		// 异步保存
		operationLogService.saveAsync(operationLog);
	}

}
