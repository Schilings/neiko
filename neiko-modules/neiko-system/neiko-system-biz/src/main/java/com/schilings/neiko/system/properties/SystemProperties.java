package com.schilings.neiko.system.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "neiko.system")
public class SystemProperties {
	
	/**
	 * 添加用户校验密码的规则：值为正则表达式，当为空时，不对密码规则进行校验
	 */
	private String passwordRule;

	/**
	 * 超级管理员的配置
	 */
	private Administrator administrator = new Administrator();

	@Getter
	@Setter
	public static class Administrator {

		/**
		 * 指定id的用户为超级管理员
		 */
		private Long userId = 0L;

		/**
		 * 指定 username 为超级管理员
		 */
		private String username;

	}

}
