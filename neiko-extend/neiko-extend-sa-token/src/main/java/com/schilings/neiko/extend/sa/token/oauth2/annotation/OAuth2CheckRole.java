package com.schilings.neiko.extend.sa.token.oauth2.annotation;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.schilings.neiko.extend.sa.token.core.StpOAuth2UserUtil;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * <p>
 * 角色认证：必须具有指定角色标识才能进入该方法 可标注在函数、类上（效果等同于标注在此类的所有方法上）
 * </p>
 *
 * @author Schilings
 */
@SaCheckRole(type = StpOAuth2UserUtil.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface OAuth2CheckRole {

	/**
	 * 需要校验的角色标识
	 * @return 需要校验的角色标识
	 */
	@AliasFor(annotation = SaCheckRole.class)
	String[] value() default {};

	/**
	 * 验证模式：AND | OR，默认AND
	 * @return 验证模式
	 */
	@AliasFor(annotation = SaCheckRole.class)
	SaMode mode() default SaMode.AND;

}
