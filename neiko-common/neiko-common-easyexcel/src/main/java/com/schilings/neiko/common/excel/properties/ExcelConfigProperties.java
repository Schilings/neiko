package com.schilings.neiko.common.excel.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = ExcelConfigProperties.PREFIX)
public class ExcelConfigProperties {

	public static final String PREFIX = "neiko.excel";

	/**
	 * 模板路径
	 */
	private String templatePath = "temp";

}
