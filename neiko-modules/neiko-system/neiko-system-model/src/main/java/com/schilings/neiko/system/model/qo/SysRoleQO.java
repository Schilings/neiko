package com.schilings.neiko.system.model.qo;

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
 * SysRole多条件查询QO
 * </p>
 *
 * @author Schilings
 */
@Data
@Schema(title = "角色查询对象")
@ParameterObject
public class SysRoleQO {

	private static final long serialVersionUID = 1L;

	@Parameter(description = "角色名称")
	private String name;

	@Parameter(description = "角色标识")
	private String code;

	@Parameter(description = "开始时间")
	@DateTimeFormat(pattern = NORM_DATETIME_PATTERN)
	private LocalDateTime startTime;

	@Parameter(description = "结束时间")
	@DateTimeFormat(pattern = NORM_DATETIME_PATTERN)
	private LocalDateTime endTime;

}
