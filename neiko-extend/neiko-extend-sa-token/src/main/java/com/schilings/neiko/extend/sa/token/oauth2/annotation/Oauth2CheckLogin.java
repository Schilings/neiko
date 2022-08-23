package com.schilings.neiko.extend.sa.token.oauth2.annotation;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.schilings.neiko.extend.sa.token.core.StpOauth2UserUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * <p>登录认证：只有登录之后才能进入该方法
 * 可标注在函数、类上（效果等同于标注在此类的所有方法上）</p>
 * 
 * @author Schilings
*/
@SaCheckLogin(type = StpOauth2UserUtil.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Oauth2CheckLogin {
    
    
}
