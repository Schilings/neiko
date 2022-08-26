package com.schilings.neiko.system.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "neiko.system")
public class SystemProperties {

	/**
	 * 是否开启前后端交互使用的AES对称加密算法
	 */
	private boolean enableAes = true;

	/**
	 * 前后端交互使用的AES对称加密算法的密钥，建议使用ASE对秘钥双重加密得到的 16 位字符
	 */
	private String aesSecretKey;

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
