package com.schilings.neiko.autoconfigure.job;

import com.schilings.neiko.autoconfigure.job.properties.XxlExecutorProperties;
import com.schilings.neiko.autoconfigure.job.properties.XxlJobProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * xxl 初始化
 */
@Slf4j
@EnableConfigurationProperties(XxlJobProperties.class)
public class XxlJobAutoConfiguration {

	@Bean
	public XxlJobSpringExecutor xxlJobSpringExecutor(XxlJobProperties xxlJobProperties) {
		log.info(">>>>>>>>>>> xxl-job config init.");
		XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
		xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdmin().getAddresses());
		XxlExecutorProperties executorProperties = xxlJobProperties.getExecutor();
		xxlJobSpringExecutor.setAppname(executorProperties.getAppname());
		xxlJobSpringExecutor.setIp(executorProperties.getIp());
		xxlJobSpringExecutor.setPort(executorProperties.getPort());
		xxlJobSpringExecutor.setAccessToken(executorProperties.getAccessToken());
		xxlJobSpringExecutor.setLogPath(executorProperties.getLogPath());
		xxlJobSpringExecutor.setLogRetentionDays(executorProperties.getLogRetentionDays());
		xxlJobSpringExecutor.setAddress(executorProperties.getAddress());
		return xxlJobSpringExecutor;
	}

}
