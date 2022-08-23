package com.schilings.neiko.admin.upms.config.satoken;


import cn.dev33.satoken.interceptor.SaAnnotationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 
 * <p>Sa-Token配置</p>
 * 
 * @author Schilings
*/
@Configuration(proxyBeanMethods = false)
public class SaTokenConfiguration {
    
    @Configuration
    public class SaTokenConfigure implements WebMvcConfigurer {
        /**
         * <p> 注册Sa-Token的注解拦截器，打开注解式鉴权功能 </p>
        */
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            // 注册注解拦截器，并排除不需要注解鉴权的接口地址 (与登录拦截器无关)
            registry.addInterceptor(new SaAnnotationInterceptor()).addPathPatterns("/**");
        }
    }
}
