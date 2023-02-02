package com.schilings.neiko.sample.resource.server.http;


import com.schilings.neiko.authorization.remote.AuthorizationConsentRemote;
import com.schilings.neiko.authorization.remote.AuthorizationRemote;
import com.schilings.neiko.authorization.remote.OAuth2RegisteredClientRemote;
import com.schilings.neiko.sample.resource.server.http.token.TokenHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
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
                .baseUrl("http://127.0.0.1:9000")
                .build();
    }

    @Bean
    public HttpServiceProxyFactory httpServiceProxyFactory(WebClient webClient) {
        return HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient))
                .embeddedValueResolver(new CustomStringValueResolver())
                .customArgumentResolver(new PageParamArgumentResolver())
                .customArgumentResolver(new RequestParamObjectArgumentResolver())
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
    
}
