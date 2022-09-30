package com.schilings.neiko.system.security;

import cn.hutool.core.util.StrUtil;
import com.schilings.neiko.extend.sa.token.password.DefaultPasswordEncoder;
import com.schilings.neiko.system.properties.SystemProperties;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 密码相关的操作的辅助类
 */
@Component
public class PasswordHelper {

	private final SystemProperties systemProperties;

	private final Pattern passwordPattern;

	public PasswordHelper(SystemProperties systemProperties) {
		this.systemProperties = systemProperties;
		String passwordRule = systemProperties.getPasswordRule();
		this.passwordPattern = StrUtil.isEmpty(passwordRule) ? null : Pattern.compile(passwordRule);
	}

	/**
	 * 校验密码是否符合规则
	 * @param rawPassword 明文密码
	 * @return 符合返回 true
	 */
	public boolean validateRule(String rawPassword) {
		rawPassword = decodeAes(rawPassword);
		if (passwordPattern == null) {
			return true;
		}
		Matcher matcher = passwordPattern.matcher(rawPassword);
		return matcher.matches();
	}

	/**
	 * 将前端传递过来的密文解密为明文
	 * @param aesPass AES加密后的密文
	 * @return 明文密码
	 */
	public String decodeAes(String aesPass) {
		if (systemProperties.isEnableAes()) {
			return DefaultPasswordEncoder.decodeAes(aesPass, systemProperties.getAesSecretKey());
		}
		return aesPass;
	}

	/**
	 * 对前端传入的密码进行加密
	 * @param asePassword 可能为ase密码
	 * @param salt 盐
	 * @return
	 */
	public String encode(String asePassword, String salt) {
		String rawPassword = decodeAes(asePassword);
		return DefaultPasswordEncoder.encode(rawPassword, salt);
	}

}
