package com.schilings.neiko.cloud.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = NeikoGatewayProperties.PREFIX)
public class NeikoGatewayProperties {

	public static final String PREFIX = "spring.cloud.gateway.neiko";

	/**
	 * 请求日志
	 */
	private Boolean requestLog = true;

}
