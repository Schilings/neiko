package com.schilings.neiko.samples.application.http;


import com.schilings.neiko.authorization.remote.AuthorizationConsentRemote;
import com.schilings.neiko.authorization.remote.AuthorizationRemote;
import com.schilings.neiko.authorization.remote.OAuth2RegisteredClientRemote;
import com.schilings.neiko.common.core.http.PageParamArgumentResolver;
import com.schilings.neiko.common.core.http.RequestParameterObjectArgumentResolver;
import com.schilings.neiko.log.remote.AccessLogRemote;
import com.schilings.neiko.log.remote.LoginLogRemote;
import com.schilings.neiko.log.remote.OperationLogRemote;
import com.schilings.neiko.notify.remote.AnnouncementRemote;
import com.schilings.neiko.notify.remote.UserAnnouncementRemote;
import com.schilings.neiko.samples.application.token.TokenHolder;
import com.schilings.neiko.system.remote.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;


@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class WebClientConfiguration {

    private final TokenHolder tokenHolder;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                //添加全局默认请求头
                .defaultHeader("source", "http-interface")
                //给请求添加过滤器，添加自定义的认证头
                .filter((request, next) -> {
                    ClientRequest filtered = ClientRequest.from(request)
                            .header("Authorization", tokenHolder.getTokenWithPrefix())
                            .build();
                    
                    return next.exchange(filtered);
                })
                //.baseUrl("http://127.0.0.1:9000")
                .build();
    }

    @Bean
    public HttpServiceProxyFactory httpServiceProxyFactory(WebClient webClient) {
        return HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient))
                .embeddedValueResolver(str ->{
                    //动态分配
                    if (str.startsWith("/authorization/"))
                        return "http://127.0.0.1:9000" + str;
                    
                    if (str.startsWith("/system/"))
                        return "http://127.0.0.1:9000" + str;

                    if (str.startsWith("/log/"))
                        return "http://127.0.0.1:9000" + str;

                    if (str.startsWith("/notify/"))
                        return "http://127.0.0.1:9000" + str;
                    
                    return str;
                    
                })
                .customArgumentResolver(new PageParamArgumentResolver())
                .customArgumentResolver(new RequestParameterObjectArgumentResolver())
                .build();
    }

    @Bean
    public OAuth2RegisteredClientRemote registeredClientRemote(HttpServiceProxyFactory factory) {
        return factory.createClient(OAuth2RegisteredClientRemote.class);
    }

    @Bean
    public AuthorizationRemote authorizationRemoteService(HttpServiceProxyFactory factory) {
        return factory.createClient(AuthorizationRemote.class);
    }
    @Bean
    public AuthorizationConsentRemote authorizationConsentRemote(HttpServiceProxyFactory factory) {
        return factory.createClient(AuthorizationConsentRemote.class);
    }

    @Bean
    public SysUserRemote sysUserRemote(HttpServiceProxyFactory factory) {
        return factory.createClient(SysUserRemote.class);
    }

    @Bean
    public SysMenuRemote sysMenuRemote(HttpServiceProxyFactory factory) {
        return factory.createClient(SysMenuRemote.class);
    }

    @Bean
    public SysConfigRemote sysConfigRemote(HttpServiceProxyFactory factory) {
        return factory.createClient(SysConfigRemote.class);
    }

    @Bean
    public SysOrganizationRemote sysOrganizationRemote(HttpServiceProxyFactory factory) {
        return factory.createClient(SysOrganizationRemote.class);
    }
    
    @Bean
    public SysDictRemote sysDictRemote(HttpServiceProxyFactory factory) {
        return factory.createClient(SysDictRemote.class);
    }

    @Bean
    public SysRoleRemote sysRoleRemote(HttpServiceProxyFactory factory) {
        return factory.createClient(SysRoleRemote.class);
    }

    @Bean
    public AccessLogRemote accessLogRemote(HttpServiceProxyFactory factory) {
        return factory.createClient(AccessLogRemote.class);
    }

    @Bean
    public OperationLogRemote operationLogRemote(HttpServiceProxyFactory factory) {
        return factory.createClient(OperationLogRemote.class);
    }

    @Bean
    public LoginLogRemote loginLogRemote(HttpServiceProxyFactory factory) {
        return factory.createClient(LoginLogRemote.class);
    }

    @Bean
    public AnnouncementRemote announcementRemote(HttpServiceProxyFactory factory) {
        return factory.createClient(AnnouncementRemote.class);
    }

    @Bean
    public UserAnnouncementRemote userAnnouncementRemote(HttpServiceProxyFactory factory) {
        return factory.createClient(UserAnnouncementRemote.class);
    }
    
    
}
