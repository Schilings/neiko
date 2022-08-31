package com.schilings.neiko.autoconfigure.oss;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * oss 自动配置类
 */
@AutoConfiguration
@EnableConfigurationProperties(OssProperties.class)
public class OssAutoConfiguration {

	@Bean
	@ConditionalOnClass(S3Client.class)
	@ConditionalOnMissingBean(OssClient.class)
	@ConditionalOnProperty(prefix = "neiko.oss", name = "access-key")
	public OssClient ossClient(OssProperties properties) {
		return new OssClient(properties);
	}

}
