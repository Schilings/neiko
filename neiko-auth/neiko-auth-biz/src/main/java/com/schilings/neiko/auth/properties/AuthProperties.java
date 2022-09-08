package com.schilings.neiko.auth.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "neiko.auth")
public class AuthProperties {

	/**
	 * 是否开启前后端交互使用的AES对称加密算法
	 */
	private boolean enableAes = true;

	/**
	 * 前后端交互使用的AES对称加密算法的密钥，建议使用ASE对秘钥双重加密得到的 16 位字符
	 */
	private String aesSecretKey;
	
	/**
	 * 开放客户端，该客户端免access_token验证
	 */
	private OpenClient openClient = new OpenClient();

	@Data
	public static class OpenClient {

		private String clientId;

	}

}
