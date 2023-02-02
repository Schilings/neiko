package com.schilings.neiko.security.captcha;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 验证码验证器
 *
 */
public interface CaptchaValidator {

	/**
	 * 校验验证码
	 * @param request the current request
	 * @return {@link CaptchaValidateResult}
	 */
	CaptchaValidateResult validate(HttpServletRequest request);

}
