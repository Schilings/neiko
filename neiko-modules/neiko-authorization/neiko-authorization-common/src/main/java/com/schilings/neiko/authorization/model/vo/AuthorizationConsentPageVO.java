package com.schilings.neiko.authorization.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

@Data
@Schema(title = "授权用户同意信息")
public class AuthorizationConsentPageVO {

	@Schema
	private Long id;

	@Schema(title = "客户端clientId")
	private String registeredClientId;

	@Schema(title = "主体")
	private String principalName;

	@Schema(title = "权限")
	private Set<String> authorities;

	@Schema(title = "创建时间")
	@JsonFormat(pattern = NORM_DATETIME_PATTERN, timezone = "GMT+8")
	private LocalDateTime createTime;

	@Schema(title = "修改时间")
	@JsonFormat(pattern = NORM_DATETIME_PATTERN, timezone = "GMT+8")
	private LocalDateTime updateTime;

}
