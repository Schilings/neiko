package com.schilings.neiko.authorization.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.schilings.neiko.authorization.model.entity.OAuth2ClientSettings;
import com.schilings.neiko.authorization.model.entity.OAuth2TokenSettings;
import com.schilings.neiko.common.core.desensitize.annotation.JsonSimpleDesensitize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

@Data
@Schema(title = "授权服务端客户端基本信息")
public class OAuth2RegisteredClientInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(title = "id")
	private Long id;

	@Schema(title = "客户端id")
	private String clientId;

	@Schema(title = "clientId生效时间")
	@JsonFormat(pattern = NORM_DATETIME_PATTERN, timezone = "GMT+8")
	private Instant clientIdIssuedAt;

	@Schema(title = "客户端secret")
	@JsonSimpleDesensitize
	private String clientSecret;

	@Schema(title = "clientSecret失效时间")
	@JsonFormat(pattern = NORM_DATETIME_PATTERN, timezone = "GMT+8")
	private Instant clientSecretExpiresAt;

	@Schema(title = "客户端名称")
	private String clientName;

	@Schema(title = "客户端支持的认证方式")
	private Set<String> clientAuthenticationMethods;

	@Schema(title = "客户端支持的授权方式")
	private Set<String> authorizationGrantTypes;

	@Schema(title = "客户端配置的回调地址")
	private Set<String> redirectUris;

	@Schema(title = "客户端支持的作用域")
	private Set<String> scopes;

	@Schema(title = "授权服务端客户端配置信息")
	private OAuth2ClientSettingsVO clientSettings;

	@Schema(title = "授权服务端客户端令牌配置信息")
	private OAuth2TokenSettingsVO tokenSettings;

}
