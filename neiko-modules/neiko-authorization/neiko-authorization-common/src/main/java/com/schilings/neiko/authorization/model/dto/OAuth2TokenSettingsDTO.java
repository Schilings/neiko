package com.schilings.neiko.authorization.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Duration;

@Data
@Schema(title = "授权服务端客户端令牌配置信息DTO")
public class OAuth2TokenSettingsDTO {

	@Schema(title = "客户端ID")
	@NotEmpty(message = "clientId不能为空")
	private String clientId;

	@Schema(title = "访问令牌有效时长,@see Duration.parse(String)")
	private Long accessTokenTimeToLive;

	@Schema(title = "令牌格式")
	private String tokenFormat;

	@Schema(title = "刷新令牌是否可以重复使用")
	private Integer reuseRefreshTokens;

	@Schema(title = "刷新令牌有效时长，@see Duration.parse(String)")
	private Long refreshTokenTimeToLive;

	@Schema(title = "ID Token签名算法")
	private String idTokenSignatureAlgorithm;

	@Schema(title = "授权码有效时长,单位秒")
	private Long authorizationCodeTimeToLive;

}
