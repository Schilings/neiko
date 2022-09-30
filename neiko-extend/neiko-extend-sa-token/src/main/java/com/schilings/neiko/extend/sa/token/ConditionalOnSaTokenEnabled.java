package com.schilings.neiko.extend.sa.token;

import cn.dev33.satoken.spring.oauth2.SaOAuth2BeanInject;
import cn.dev33.satoken.spring.oauth2.SaOAuth2BeanRegister;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;


import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ConditionalOnClass(value = {SaOAuth2BeanRegister.class, SaOAuth2BeanInject.class})
public @interface ConditionalOnSaTokenEnabled {
}
