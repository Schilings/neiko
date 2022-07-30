package com.schilings.neiko.autoconfigure.web.api.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <pre>{@code
 *
 * }
 * <p>接口版本控制配置信息</p>
 * </pre>
 *
 * @author Schilings
 */

@Data
@ConfigurationProperties(prefix = ApiProperties.PREFIX)
public class ApiProperties {

	public static final String PREFIX = "neiko.web.api";

	private Version version = new Version();

	@Data
	public static class Version {

		private boolean enable = true;

	}

}
