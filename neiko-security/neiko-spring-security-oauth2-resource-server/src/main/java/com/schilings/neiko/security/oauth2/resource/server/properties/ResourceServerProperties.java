package com.schilings.neiko.security.oauth2.resource.server.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 资源服务器的配置文件，用于配置 token 鉴定方式。由于目前 neiko 授权服务器使用 不透明令牌，所以这里也暂时不做 jwt令牌支持的扩展
 *
 * @see org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "neiko.security.oauth2.resourceserver")
public class ResourceServerProperties {

	/**
	 * 忽略鉴权的 url 列表
	 */
	private List<String> ignoreUrls = new ArrayList<>();

	/**
	 * 当 sharedStoredToken 为 false 时生效。 主要用于配置远程端点
	 */
	private final Opaquetoken opaquetoken = new Opaquetoken();

	@Getter
	@Setter
	public static class Opaquetoken {

		/**
		 * Client id used to authenticate with the token introspection endpoint.
		 */
		private String clientId;

		/**
		 * Client secret used to authenticate with the token introspection endpoint.
		 */
		private String clientSecret;

		/**
		 * OAuth 2.0 endpoint through which token introspection is accomplished.
		 */
		private String introspectionUri;

	}

}
