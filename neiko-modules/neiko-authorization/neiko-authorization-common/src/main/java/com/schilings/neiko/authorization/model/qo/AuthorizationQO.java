package com.schilings.neiko.authorization.model.qo;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

@Data
@ParameterObject
@Schema(title = "授权信息")
public class AuthorizationQO {

	@Parameter(description = "客户端ClentId")
	private String registeredClientId;

	@Parameter(description = "授权主体名称")
	private String principalName;

	@Parameter(description = "授权方式")
	private String authorizationGrantType;

	@Parameter(description = "state值")
	private String state;

	@Parameter(description = "AuthorizationCode授权码")
	private String authorizationCodeValue;

	@Parameter(description = "AccessToken访问令牌")
	private String accessTokenValue;

	@Parameter(description = "AccessToken访问令牌作用域")
	private String accessTokenScopes;

	@Parameter(description = "RefreshToken刷新令牌")
	private String refreshTokenValue;

	@Parameter(description = "OidcIdToken令牌")
	private String oidcIdTokenValue;

	@Parameter(description = "开始时间")
	@DateTimeFormat(pattern = NORM_DATETIME_PATTERN)
	private String startTime;

	@Parameter(description = "结束时间")
	@DateTimeFormat(pattern = NORM_DATETIME_PATTERN)
	private String endTime;

}
