package com.schilings.neiko.extend.sa.token.oauth2.annotation;

import cn.dev33.satoken.annotation.SaCheckBasic;
import cn.dev33.satoken.basic.SaBasicTemplate;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Http Basic 认证：只有通过 Basic 认证后才能进入该方法
 * <p>
 * 可标注在函数、类上（效果等同于标注在此类的所有方法上）
 *
 */
@SaCheckBasic
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface OAuth2CheckBasic {

	/**
	 * 领域
	 * @return see note
	 */
	@AliasFor(annotation = SaCheckBasic.class)
	String realm() default SaBasicTemplate.DEFAULT_REALM;

	/**
	 * 需要校验的账号密码，格式形如 sa:123456
	 * @return see note
	 */
	@AliasFor(annotation = SaCheckBasic.class)

	String account() default "";

}
