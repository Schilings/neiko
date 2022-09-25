package com.schilings.neiko.admin.upms;

import com.schilings.neiko.auth.properties.AuthProperties;
import com.schilings.neiko.common.core.snowflake.SnowflakeIdGenerator;
import com.schilings.neiko.extend.sa.token.EnableOAuth2AuthorizationServer;
import com.schilings.neiko.extend.sa.token.EnableOAuth2ResourceServer;
import com.schilings.neiko.system.properties.SystemProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@MapperScan({ "com.schilings.neiko.**.mapper" })
@ComponentScan({ "com.schilings.neiko.admin.upms", "com.schilings.neiko.file", "com.schilings.neiko.system",
		"com.schilings.neiko.log", "com.schilings.neiko.notify", "com.schilings.neiko.auth" })
@EnableAsync
@AutoConfiguration
@EnableOAuth2AuthorizationServer
@EnableOAuth2ResourceServer
@EnableConfigurationProperties({ AuthProperties.class, SystemProperties.class })
public class UpmsAutoConfiguration {

	/**
	 * 雪花ID生成器
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public SnowflakeIdGenerator snowflakeIdGenerator() {
		return new SnowflakeIdGenerator();
	}

}
