package com.schilings.neiko.security.oauth2.resource.server.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
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
	 * 主要用于配置远程端点
	 */
	private final Opaquetoken opaquetoken = new Opaquetoken();

	/**
	 * 主要用于配置远程端点
	 */
	private final Jwt jwt = new Jwt();

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

	@Getter
	@Setter
	public static class Jwt {

		/**
		 * JSON Web Key URI to use to verify the JWT token.
		 */
		private String jwkSetUri;

		/**
		 * JSON Web Algorithms used for verifying the digital signatures.
		 */
		private List<String> jwsAlgorithms = List.of("RS256");

		/**
		 * URI that can either be an OpenID Connect discovery endpoint or an OAuth 2.0
		 * Authorization Server Metadata endpoint defined by RFC 8414.
		 */
		private String issuerUri;

		/**
		 * Location of the file containing the public key used to verify a JWT.
		 */
		private Resource publicKeyLocation;

		/**
		 * Identifies the recipients that the JWT is intended for.
		 */
		private List<String> audiences = new ArrayList<>();

		public String readPublicKey() throws IOException {
			String key = "spring.security.oauth2.resourceserver.public-key-location";
			Assert.notNull(this.publicKeyLocation, "PublicKeyLocation must not be null");
			if (!this.publicKeyLocation.exists()) {
				throw new InvalidConfigurationPropertyValueException(key, this.publicKeyLocation,
						"Public key location does not exist");
			}
			try (InputStream inputStream = this.publicKeyLocation.getInputStream()) {
				return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
			}
		}

	}

}
