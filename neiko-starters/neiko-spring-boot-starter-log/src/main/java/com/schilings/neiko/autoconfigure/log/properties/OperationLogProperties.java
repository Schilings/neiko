package com.schilings.neiko.autoconfigure.log.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 操作日志配置

 */
@Data
@ConfigurationProperties(prefix = OperationLogProperties.PREFIX)
public class OperationLogProperties {

	public static final String PREFIX = "neiko.log.operation";

	/**
	 * 开启操作日志的记录
	 */
	private boolean enabled = true;

}
