package com.schilings.neiko.extend.sa.token.password;

import cn.hutool.crypto.CryptoException;
import com.schilings.neiko.common.security.exception.SecurityException;
import com.schilings.neiko.common.security.utils.SecurityUtils;

public class DefaultPasswordEncoder {

	/**
	 * 密码加密，单向加密，不可逆
	 * @param rawPassword 明文密码
	 * @return 加密后的密文
	 */
	public static String encode(String rawPassword, String salt) {
		return SecurityUtils.md5BySalt(rawPassword, salt);
	}

	/**
	 * 将前端传递过来的密文解密为明文
	 * @param aesPass AES加密后的密文
	 * @return 明文密码
	 */
	public static String decodeAes(String aesPass, String secretKey) {
		try {
			return SecurityUtils.aesDecrypt(secretKey, aesPass);
		}
		catch (CryptoException ex) {
			throw new SecurityException(400, "密码密文解密异常！");
		}
	}

}
