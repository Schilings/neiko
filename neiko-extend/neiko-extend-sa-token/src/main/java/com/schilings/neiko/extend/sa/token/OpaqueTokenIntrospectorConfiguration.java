package com.schilings.neiko.extend.sa.token;

import com.schilings.neiko.extend.sa.token.oauth2.introspector.OpaqueTokenIntrospector;
import com.schilings.neiko.extend.sa.token.oauth2.introspector.OpaqueTokenIntrospectorWrapper;
import com.schilings.neiko.extend.sa.token.oauth2.introspector.RemoteOpaqueTokenIntrospector;
import com.schilings.neiko.extend.sa.token.oauth2.introspector.SharedStoredOpaqueTokenIntrospector;
import com.schilings.neiko.extend.sa.token.properties.OAuth2ResourceServerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@ConditionalOnSaTokenEnabled
public class OpaqueTokenIntrospectorConfiguration {

	/**
	 * 当资源服务器和授权服务器的 token 共享存储时，配合 Spring-security-oauth2 的 TokenStore，用于解析其生成的不透明令牌
	 * @return SharedStoredOpaqueTokenIntrospector
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = "neiko.sa-token.oauth2.resource-server", name = "shared-stored-token",
			havingValue = "true", matchIfMissing = true)
	public OpaqueTokenIntrospector sharedStoredOpaqueTokenIntrospector() {
		return new SharedStoredOpaqueTokenIntrospector();
	}

	/**
	 * 当资源服务器和授权服务器的 token 存储无法共享时，通过远程调用的方式，向授权服务鉴定 token，并同时获取对应的授权信息
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = "ballcat.sa-token.oauth2.resource-server", name = "shared-stored-token",
			havingValue = "false")
	public OpaqueTokenIntrospector opaqueTokenIntrospector(
			OAuth2ResourceServerProperties oAuth2ResourceServerProperties) {
		OAuth2ResourceServerProperties.Opaquetoken opaqueToken = oAuth2ResourceServerProperties.getOpaqueToken();
		return new RemoteOpaqueTokenIntrospector(opaqueToken.getIntrospectionUri(), opaqueToken.getClientId(),
				opaqueToken.getClientSecret());
	}

	/**
	 * OpaqueTokenIntrospector封装类
	 * @param opaqueTokenIntrospector
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public OpaqueTokenIntrospectorWrapper opaqueTokenIntrospectorWrapper(
			OpaqueTokenIntrospector opaqueTokenIntrospector) {
		return new OpaqueTokenIntrospectorWrapper(opaqueTokenIntrospector);
	}

}
