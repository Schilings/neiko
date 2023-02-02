package com.schilings.neiko.authorization.model.dto;

import com.schilings.neiko.authorization.model.entity.OAuth2ClientSettings;
import com.schilings.neiko.common.core.validation.group.CreateGroup;
import com.schilings.neiko.common.core.validation.group.UpdateGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.util.Set;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

@Data
@Schema(title = "授权服务端客户端基本信息DTO")
public class OAuth2RegisteredClientDTO {

	@NotNull(message = "id不能为空", groups = UpdateGroup.class)
	@Schema(title = "ID")
	private Long id;

	@NotEmpty(message = "clientId不能为空")
	@Schema(title = "客户端id")
	private String clientId;

	@Schema(title = "clientId生效时间")
	private String clientIdIssuedAt;

	@NotEmpty(message = "clientSecret不能为空")
	@Schema(title = "客户端secret")
	private String clientSecret;

	@Schema(title = "clientSecret失效时间")
	private String clientSecretExpiresAt;

	@NotEmpty(message = "客户端名称不能为空")
	@Schema(title = "客户端名称")
	private String clientName;

	@NotEmpty(message = "授权方式不能为空")
	@Schema(title = "客户端支持的授权方式")
	private Set<String> clientAuthenticationMethods;

	@NotEmpty(message = "授权方式不能为空")
	@Schema(title = "客户端支持的授权方式")
	private Set<String> authorizationGrantTypes;

	@Schema(title = "客户端配置的回调地址")
	private Set<String> redirectUris;

	@Schema(title = "客户端支持的作用域")
	private Set<String> scopes;

	private OAuth2ClientSettingsDTO clientSettings;

	private OAuth2TokenSettingsDTO tokenSettings;

}
