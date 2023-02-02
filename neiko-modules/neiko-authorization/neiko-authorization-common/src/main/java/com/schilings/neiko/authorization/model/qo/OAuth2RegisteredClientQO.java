package com.schilings.neiko.authorization.model.qo;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

@Data
@Schema(title = "授权服务端客户端基本信息查询对象")
@ParameterObject
@NoArgsConstructor
@Accessors(chain = true)
public class OAuth2RegisteredClientQO {

	@Parameter(description = "客户端id")
	private String clientId;

	@Parameter(description = "客户端名称")
	private String clientName;

	@Parameter(description = "客户端支持的认证方式")
	private String clientAuthenticationMethod;

	@Parameter(description = "客户端支持的授权方式")
	private String authorizationGrantType;

	@Parameter(description = "客户端支持的作用域")
	private String scope;

	@Parameter(description = "开始时间")
	@DateTimeFormat(pattern = NORM_DATETIME_PATTERN)
	private String startTime;

	@Parameter(description = "结束时间")
	@DateTimeFormat(pattern = NORM_DATETIME_PATTERN)
	private String endTime;

}
