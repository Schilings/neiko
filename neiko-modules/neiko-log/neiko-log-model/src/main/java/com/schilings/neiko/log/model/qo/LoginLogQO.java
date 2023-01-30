package com.schilings.neiko.log.model.qo;

import com.schilings.neiko.log.enums.LoginEventTypeEnum;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

/**
 *
 * <p>
 * 登录日志表多条件查询QO
 * </p>
 *
 * @author Schilings
 */
@Data
@Schema(title = "后台登录日志查询对象")
@ParameterObject
public class LoginLogQO {

	private static final long serialVersionUID = 1L;

	/**
	 * 追踪ID
	 */
	@Parameter(description = "追踪ID")
	private String traceId;

	/**
	 * 客户端ID
	 */
	@Parameter(description = "客户端ID")
	private String clientId;

	/**
	 * 用户名
	 */
	@Parameter(description = "用户名")
	private String username;

	/**
	 * 操作信息
	 */
	@Parameter(description = "请求IP")
	private String ip;

	/**
	 * 状态
	 */
	@Parameter(description = "状态")
	private Integer status;

	/**
	 * 事件类型 登陆/登出
	 *
	 * @see LoginEventTypeEnum
	 */
	@Parameter(description = "事件类型")
	private Integer eventType;

	/**
	 * 登陆时间区间（开始时间）
	 */
	@DateTimeFormat(pattern = NORM_DATETIME_PATTERN)
	@Parameter(description = "开始时间（登陆时间区间）")
	private LocalDateTime startTime;

	/**
	 * 登陆时间区间（结束时间）
	 */
	@DateTimeFormat(pattern = NORM_DATETIME_PATTERN)
	@Parameter(description = "结束时间（登陆时间区间）")
	private LocalDateTime endTime;

}
