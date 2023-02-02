package com.schilings.neiko.authorization.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(title = "授权服务端客户端配置信息表")
public class OAuth2ClientSettingsVO {

	@Schema(title = "对应的客户端ID")
	private String clientId;

	@Schema(title = "是否需要ProofKey")
	private boolean requireProofKey;

	@Schema(title = "是否需要用户授权同意")
	private boolean requireAuthorizationConsent;

	@Schema(title = "jwkSetUrl")
	private String jwkSetUrl;

	@Schema(title = "签名算法")
	private String signingAlgorithm;

}
