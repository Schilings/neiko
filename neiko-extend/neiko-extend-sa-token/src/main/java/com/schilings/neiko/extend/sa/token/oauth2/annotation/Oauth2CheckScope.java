package com.schilings.neiko.extend.sa.token.oauth2.annotation;

import com.schilings.neiko.extend.sa.token.core.StpOauth2UserUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作用域认证：只有客户端作用域符合才能进入该方法
 * <p>
 * 可标注在函数、类上（效果等同于标注在此类的所有方法上）
 * @author kong
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface Oauth2CheckScope {

	/**
	 * 需要校验的权限码
	 * @return 需要校验的权限码
	 */
	String[] value() default {};

	String type() default StpOauth2UserUtil.TYPE;

}
