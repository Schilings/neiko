package com.schilings.neiko.authorization.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.schilings.neiko.common.util.LocalDateTimeUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("nk_oauth2_client_settings")
@Schema(title = "授权服务端客户端配置信息表")
public class OAuth2ClientSettings {

	@TableId
	@Schema(title = "对应的客户端ID")
	private String clientId;

	@Schema(title = "是否需要ProofKey")
	private Integer requireProofKey;

	@Schema(title = "是否需要用户授权同意")
	private Integer requireAuthorizationConsent;

	@Schema(title = "jwkSetUrl")
	private String jwkSetUrl;

	@Schema(title = "签名算法")
	private String signingAlgorithm;

}
