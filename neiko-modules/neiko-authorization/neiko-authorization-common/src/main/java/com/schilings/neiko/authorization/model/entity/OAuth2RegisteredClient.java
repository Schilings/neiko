package com.schilings.neiko.authorization.model.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.schilings.neiko.common.model.entity.LogicDeletedBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;



@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("nk_oauth2_registered_client")
@Schema(title = "授权服务端客户端基本信息表")
public class OAuth2RegisteredClient extends LogicDeletedBaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    @Schema(title = "ID")
    private Long id;
    
    @Schema(title = "客户端id")
    private String clientId;
    
    @Schema(title = "clientId生效时间")
    private String clientIdIssuedAt;
    
    @Schema(title = "客户端secret")
    private String clientSecret;
    
    @Schema(title = "clientSecret失效时间")
    private String clientSecretExpiresAt;
    
    @Schema(title = "客户端名称")
    private String clientName;
    
    @Schema(title = "客户端支持的认证方式")
    private String clientAuthenticationMethods;
    
    @Schema(title = "客户端支持的授权方式")
    private String authorizationGrantTypes;
    
    @Schema(title = "客户端配置的回调地址")
    private String redirectUris;
    
    @Schema(title = "客户端支持的作用域")
    private String scopes;
    
}
