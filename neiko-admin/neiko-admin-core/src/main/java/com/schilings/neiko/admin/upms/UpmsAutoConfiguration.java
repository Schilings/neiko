package com.schilings.neiko.admin.upms;

import com.schilings.neiko.auth.properties.AuthProperties;
import com.schilings.neiko.common.core.snowflake.SnowflakeIdGenerator;
import com.schilings.neiko.extend.sa.token.EnableAuthorizationServer;
import com.schilings.neiko.system.properties.SystemProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@MapperScan({ 
		"com.gitee.sunchenbin.mybatis.actable.dao.*", // 自动建表
		"com.schilings.neiko.**.mapper" })
@ComponentScan({ 
		"com.gitee.sunchenbin.mybatis.actable.manager.*", // 自动建表
		"com.schilings.neiko.admin.upms",
		"com.schilings.neiko.system", 
		"com.schilings.neiko.log",
		"com.schilings.neiko.auth" })
@EnableAsync
@AutoConfiguration
@EnableAuthorizationServer
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
