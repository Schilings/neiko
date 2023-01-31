package com.schilings.neiko.security.oauth2.resource.server.autoconfigure;



import com.schilings.neiko.security.oauth2.resource.server.web.SecurityExceptionResolver;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.AbstractRequestMatcherRegistry;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.ClassUtils;

@AutoConfiguration(after = ResourceServerAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnBean(ResourceServerConfigurationAdapter.class)
public class MvcSecurityAutoConfiguration {

    private static final String HANDLER_MAPPING_INTROSPECTOR = "org.springframework.web.servlet.handler.HandlerMappingIntrospector";

    private static final boolean mvcPresent;
    static {
        mvcPresent = ClassUtils.isPresent(HANDLER_MAPPING_INTROSPECTOR,
                AbstractRequestMatcherRegistry.class.getClassLoader());
    }

    @Bean
    @ConditionalOnBean(AccessDeniedHandler.class)
    public SecurityExceptionResolver securityExceptionResolver(AccessDeniedHandler accessDeniedHandler) {
        return new SecurityExceptionResolver(accessDeniedHandler);
    }
    
}
