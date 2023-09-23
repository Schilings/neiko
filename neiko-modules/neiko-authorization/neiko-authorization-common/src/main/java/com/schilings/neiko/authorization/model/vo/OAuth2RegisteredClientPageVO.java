package com.schilings.neiko.authorization.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

@Data
@Schema(title = "授权服务端客户端基本信息分页视图")
public class OAuth2RegisteredClientPageVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(title = "id")
	private Long id;

	@Schema(title = "客户端id")
	private String clientId;

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

	@Schema(title = "创建时间")
	@JsonFormat(pattern = NORM_DATETIME_PATTERN, timezone = "GMT+8")
	private LocalDateTime createTime;

	@Schema(title = "修改时间")
	@JsonFormat(pattern = NORM_DATETIME_PATTERN, timezone = "GMT+8")
	private LocalDateTime updateTime;

}
