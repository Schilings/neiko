package com.schilings.neiko.security.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 前后端交互中密码使用 AES 加密，模式: CBC，padding: PKCS5，偏移量暂不定制和密钥相同。 <br/>
 * 服务端OAuth2中，密码使用BCrypt方式加密
 *
 */
public final class PasswordUtils {

	private PasswordUtils() {
	}

	/**
	 * 创建一个密码加密的代理，方便后续切换密码的加密算法
	 * @see PasswordEncoderFactories#createDelegatingPasswordEncoder()
	 * @return DelegatingPasswordEncoder
	 */
	public static PasswordEncoder createDelegatingPasswordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		DelegatingPasswordEncoder delegatingPasswordEncoder = (DelegatingPasswordEncoder) PasswordEncoderFactories
				.createDelegatingPasswordEncoder();
		// 设置默认的密码解析器，以便兼容历史版本的密码
		delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(bCryptPasswordEncoder);
		return delegatingPasswordEncoder;
	}

	/**
	 * 将前端传递过来的密文解密为明文
	 * @param aesPass AES加密后的密文
	 * @param secretKey 密钥
	 * @return 明文密码
	 */
	public static String decodeAES(String aesPass, String secretKey) {
		byte[] secretKeyBytes = secretKey.getBytes();
		AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, secretKeyBytes, secretKeyBytes);
		byte[] result = aes.decrypt(Base64.decode(aesPass.getBytes(StandardCharsets.UTF_8)));
		return new String(result, StandardCharsets.UTF_8);
	}

	/**
	 * 将明文密码加密为密文
	 * @param password 明文密码
	 * @param secretKey 密钥
	 * @return AES加密后的密文
	 */
	public static String encodeAESBase64(String password, String secretKey) {
		byte[] secretKeyBytes = secretKey.getBytes();
		AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, secretKeyBytes, secretKeyBytes);
		return aes.encryptBase64(password, StandardCharsets.UTF_8);
	}

}
