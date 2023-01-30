package com.schilings.neiko.authorization.model.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Duration;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("nk_oauth2_token_settings")
@Schema(title = "授权服务端客户端令牌配置信息表")
public class OAuth2TokenSettings {

    @TableId
    @Schema(title = "客户端ID")
    private String clientId;
    @Schema(title = "访问令牌有效时长,单位秒")
    private Long accessTokenTimeToLive;

    @Schema(title = "刷新令牌有效时长,单位秒")
    private Long refreshTokenTimeToLive;

    @Schema(title = "授权码有效时长,单位秒")
    private Long authorizationCodeTimeToLive;
    
    @Schema(title = "令牌格式")
    private String tokenFormat;
    @Schema(title = "刷新令牌是否可以重复使用")
    private Integer reuseRefreshTokens;

    @Schema(title = "ID Token签名算法")
    private String idTokenSignatureAlgorithm;
}
