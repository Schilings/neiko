package com.schilings.neiko.extend.sa.token;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * <p>
 * 开启自定义资源中心
 * </p>
 *
 * @author Schilings
 */
@Import(OAuth2ResourceServerConfiguration.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface EnableOAuth2ResourceServer {

}
