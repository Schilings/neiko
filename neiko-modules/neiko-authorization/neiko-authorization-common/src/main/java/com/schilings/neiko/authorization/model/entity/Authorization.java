package com.schilings.neiko.authorization.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("nk_authorization")
@Schema(title = "授权信息")
public class Authorization {

	@TableId
	@Schema(title = "id")
	private String id;

	@Schema(title = "客户端ClentId")
	private String registeredClientId;

	@Schema(title = "授权主体名称")
	private String principalName;

	@Schema(title = "授权方式")
	private String authorizationGrantType;

	@Schema(title = "额外属性")
	private String attributes;

	@Schema(title = "state值")
	private String state;

	@Schema(title = "AuthorizationCode授权码")
	private String authorizationCodeValue;

	@Schema(title = "AuthorizationCode授权码生效时间")
	private String authorizationCodeIssuedAt;

	@Schema(title = "AuthorizationCode授权码失效时间")
	private String authorizationCodeExpiresAt;

	@Schema(title = "AuthorizationCode授权码元数据")
	private String authorizationCodeMetadata;

	@Schema(title = "AccessToken访问令牌")
	private String accessTokenValue;

	@Schema(title = "AccessToken访问令牌生效时间")
	private String accessTokenIssuedAt;

	@Schema(title = "AccessToken访问令牌失效时间")
	private String accessTokenExpiresAt;

	@Schema(title = "AccessToken访问令牌元数据")
	private String accessTokenMetadata;

	@Schema(title = "AccessToken访问令牌类型")
	private String accessTokenType;

	@Schema(title = "AccessToken访问令牌作用域")
	private String accessTokenScopes;

	@Schema(title = "RefreshToken刷新令牌")
	private String refreshTokenValue;

	@Schema(title = "RefreshToken刷新令牌生效时间")
	private String refreshTokenIssuedAt;

	@Schema(title = "RefreshToken刷新令牌失效时间")
	private String refreshTokenExpiresAt;

	@Schema(title = "RefreshToken刷新令牌元数据")
	private String refreshTokenMetadata;

	@Schema(title = "OidcIdToken令牌")
	private String oidcIdTokenValue;

	@Schema(title = "OidcIdToken令牌生效时间")
	private String oidcIdTokenIssuedAt;

	@Schema(title = "OidcIdToken令牌失效时间")
	private String oidcIdTokenExpiresAt;

	@Schema(title = "OidcIdToken令牌元数据")
	private String oidcIdTokenMetadata;

	@Schema(title = "OidcIdToken令牌声明")
	private String oidcIdTokenClaims;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(title = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(title = "修改时间")
	private LocalDateTime updateTime;

}
