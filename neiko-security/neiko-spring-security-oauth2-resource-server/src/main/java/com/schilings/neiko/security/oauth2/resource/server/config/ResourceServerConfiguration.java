package com.schilings.neiko.security.oauth2.resource.server.config;



import com.schilings.neiko.security.oauth2.resource.server.properties.ResourceServerProperties;
import com.schilings.neiko.security.oauth2.resource.server.customizer.opaque.SpringRemoteOpaqueTokenIntrospector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

class ResourceServerConfiguration {


    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(JwtDecoder.class)
    static class JwtConfiguration {

    }
    
    
    @Configuration(proxyBeanMethods = false)
    static class OpaqueTokenConfiguration {
        
        
        @Bean
        @ConditionalOnMissingBean(OpaqueTokenIntrospector.class)
        @ConditionalOnProperty(name = "neiko.security.oauth2.resourceserver.opaquetoken.introspection-uri")
        public SpringRemoteOpaqueTokenIntrospector opaqueTokenIntrospector(ResourceServerProperties properties) {
            ResourceServerProperties.Opaquetoken opaqueToken = properties.getOpaquetoken();
            return new SpringRemoteOpaqueTokenIntrospector(opaqueToken.getIntrospectionUri(), opaqueToken.getClientId(),
                    opaqueToken.getClientSecret());
        }


        //没有必要注入为Bean。默认是这个,注入容易造成循环依赖
        //而且注入AuthenticationProvider会导致Spring Security的很多默认配置不生效
        //例如DaoAuthenticationProvider等等不生效
        //但是对单独的资源服务器，OpaqueToken模式是手动new OpaqueTokenAuthenticationProvider的
        //如果不配置UserDetailService就不会提供默认的AuthenticationProvider
        //这会导致一个
        //@Bean
        //@ConditionalOnMissingBean
        //@ConditionalOnBean(OpaqueTokenIntrospector.class)
        //public OpaqueTokenAuthenticationProvider opaqueTokenAuthenticationProvider(OpaqueTokenIntrospector opaqueTokenIntrospector) {
        //    return new OpaqueTokenAuthenticationProvider(opaqueTokenIntrospector);
        //}


        //下面这个是Spring Security Resource Server 的默认自动装配，我们在其后面自动装配
        //@ConditionalOnMissingBean 所以我们注入不到，因为已经注入
        //@Bean
        //@ConditionalOnMissingBean
        //@ConditionalOnProperty(name = "spring.security.oauth2.resourceserver.opaquetoken.introspection-uri")
        //public SpringOpaqueTokenIntrospector opaqueTokenIntrospector(OAuth2ResourceServerProperties properties) {
        //    OAuth2ResourceServerProperties.Opaquetoken opaqueToken = properties.getOpaquetoken();
        //    return new SpringOpaqueTokenIntrospector(opaqueToken.getIntrospectionUri(), opaqueToken.getClientId(),
        //            opaqueToken.getClientSecret());
        //}

        //public NimbusOpaqueTokenIntrospector nimbusOpaqueTokenIntrospector(OAuth2ResourceServerProperties properties) {
        //    OAuth2ResourceServerProperties.Opaquetoken opaqueToken = properties.getOpaquetoken();
        //    return new NimbusOpaqueTokenIntrospector(opaqueToken.getIntrospectionUri(), opaqueToken.getClientId(),
        //            opaqueToken.getClientSecret());
        //}


    }

    
}
