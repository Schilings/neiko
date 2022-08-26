package com.schilings.neiko.auth.check;

import com.schilings.neiko.auth.properties.AuthProperties;
import com.schilings.neiko.extend.sa.token.password.DefaultPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordChecker {

	private final AuthProperties authProperties;

	/**
	 * 密码匹配
	 * @param fontPass 前端传入的AES加密后的用户输入的密码
	 * @param backPass 后台数据中用户已经不可逆转加密的密码
	 * @param salt 后台数据用户加密所用的盐
	 * @return
	 */
	public boolean check(String fontPass, String backPass, String salt) {
		String plainPass;
		if (authProperties.isEnableAes()) {
			// 解析出明文密码
			plainPass = DefaultPasswordEncoder.decodeAes(fontPass, authProperties.getAesSecretKey());
		}
		else {
			plainPass = fontPass;
		}
		return DefaultPasswordEncoder.encode(plainPass, salt).equals(backPass);
	}

}
