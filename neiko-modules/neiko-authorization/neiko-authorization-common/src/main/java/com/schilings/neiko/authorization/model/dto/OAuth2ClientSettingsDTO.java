package com.schilings.neiko.authorization.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(title = "客户端配置信息DTO")
public class OAuth2ClientSettingsDTO {

    @NotEmpty(message = "clientId不能为空")
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
