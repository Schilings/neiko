package com.schilings.neiko.authorization.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

@Data
@Schema(title = "授权信息")
public class AuthorizationPageVO {

	@Schema(title = "客户端ClentId")
	private String registeredClientId;

	@Schema(title = "授权主体名称")
	private String principalName;

	@Schema(title = "授权方式")
	private String authorizationGrantType;

	@Schema(title = "state值")
	private String state;

	@Schema(title = "AuthorizationCode授权码")
	private String authorizationCodeValue;

	@Schema(title = "AuthorizationCode授权码生效时间")
	@JsonFormat(pattern = NORM_DATETIME_PATTERN, timezone = "GMT+8")
	private Instant authorizationCodeIssuedAt;

	@Schema(title = "AuthorizationCode授权码失效时间")
	@JsonFormat(pattern = NORM_DATETIME_PATTERN, timezone = "GMT+8")
	private Instant authorizationCodeExpiresAt;

	@Schema(title = "AccessToken访问令牌")
	private String accessTokenValue;

	@Schema(title = "AccessToken访问令牌生效时间")
	@JsonFormat(pattern = NORM_DATETIME_PATTERN, timezone = "GMT+8")
	private Instant accessTokenIssuedAt;

	@Schema(title = "AccessToken访问令牌失效时间")
	@JsonFormat(pattern = NORM_DATETIME_PATTERN, timezone = "GMT+8")
	private Instant accessTokenExpiresAt;

	@Schema(title = "AccessToken访问令牌类型")
	private String accessTokenType;

	@Schema(title = "AccessToken访问令牌作用域")
	private Set<String> accessTokenScopes;

	@Schema(title = "RefreshToken刷新令牌")
	private String refreshTokenValue;

	@Schema(title = "RefreshToken刷新令牌生效时间")
	@JsonFormat(pattern = NORM_DATETIME_PATTERN, timezone = "GMT+8")
	private Instant refreshTokenIssuedAt;

	@Schema(title = "RefreshToken刷新令牌失效时间")
	@JsonFormat(pattern = NORM_DATETIME_PATTERN, timezone = "GMT+8")
	private Instant refreshTokenExpiresAt;

	@Schema(title = "OidcIdToken令牌")
	private String oidcIdTokenValue;

	@Schema(title = "OidcIdToken令牌生效时间")
	@JsonFormat(pattern = NORM_DATETIME_PATTERN, timezone = "GMT+8")
	private Instant oidcIdTokenIssuedAt;

	@Schema(title = "OidcIdToken令牌失效时间")
	@JsonFormat(pattern = NORM_DATETIME_PATTERN, timezone = "GMT+8")
	private Instant oidcIdTokenExpiresAt;

	@Schema(title = "创建时间")
	@JsonFormat(pattern = NORM_DATETIME_PATTERN, timezone = "GMT+8")
	private LocalDateTime createTime;

	@Schema(title = "修改时间")
	@JsonFormat(pattern = NORM_DATETIME_PATTERN, timezone = "GMT+8")
	private LocalDateTime updateTime;

}
