package com.schilings.neiko.authorization.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.boot.convert.DurationFormat;
import org.springframework.boot.convert.DurationStyle;

import java.time.Duration;

@Data
@Schema(title = "授权服务端客户端令牌配置信息表")
public class OAuth2TokenSettingsVO {

	@Schema(title = "客户端ID")
	private String clientId;

	@Schema(title = "访问令牌有效时长")
	@DurationFormat(DurationStyle.SIMPLE)
	private Duration accessTokenTimeToLive;

	@Schema(title = "令牌格式")
	private String tokenFormat;

	@Schema(title = "刷新令牌是否可以重复使用")
	private boolean reuseRefreshTokens;

	@Schema(title = "刷新令牌有效时长,单位秒")
	@DurationFormat(DurationStyle.SIMPLE)
	private Duration refreshTokenTimeToLive;

	@Schema(title = "ID Token签名算法")
	private String idTokenSignatureAlgorithm;

	@Schema(title = "授权码有效时长,单位秒")
	private Duration authorizationCodeTimeToLive;

}
