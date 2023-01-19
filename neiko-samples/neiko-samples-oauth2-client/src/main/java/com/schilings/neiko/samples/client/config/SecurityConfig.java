package com.schilings.neiko.samples.client.config;


import com.schilings.neiko.security.oauth2.client.federated.identity.FederatedIdentityConfigurer;
import com.schilings.neiko.security.oauth2.client.federated.identity.OAuth2UserMerger;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.Collection;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,UserDetailsService userDetailsService) throws Exception {
        
        FederatedIdentityConfigurer identityConfigurer = new FederatedIdentityConfigurer(httpSecurity);

        identityConfigurer.filterProccessUri(FederatedIdentityConfigurer.FILTER_PROCESSES_URI);
        identityConfigurer.authorizationRequestUri(FederatedIdentityConfigurer.AUTHORIZATION_REQUEST_PATTERN);

        identityConfigurer.wechatOAuth2Login();
        identityConfigurer.workWechatOAuth2Login();

        identityConfigurer.oauth2UserMerger(new SimpleOAuth2UserMerger(userDetailsService));


        httpSecurity
                //不然就算登录，没有被这个SecurityFilterChain拦截，也没用
                .securityMatchers().requestMatchers(new AntPathRequestMatcher("/**"))
                .and()
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/assets/**", "/webjars/**", "/login").permitAll()
                                .anyRequest().authenticated()
                )
                .apply(identityConfigurer);
        
        return httpSecurity.build();
        
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }


    @RequiredArgsConstructor
    static class SimpleOAuth2UserMerger implements OAuth2UserMerger {

        private final UserDetailsService userDetailsService;

        @Override
        public OAuth2User merge(OAuth2LoginAuthenticationToken authenticationToken) {
            System.out.println("[OAuth2登录返回的用户信息OAuth2User]: " + authenticationToken.getPrincipal());

            if (authenticationToken.getPrincipal() instanceof DefaultOAuth2User) {
                DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authenticationToken.getPrincipal();

                try {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(defaultOAuth2User.getName());
                    System.out.println("[根据用户信息的Name作为Username查询本地用户信息UserDetails]: " + userDetails);
                } catch (UsernameNotFoundException e) {
                    System.out.println("[根据用户信息的Name作为Username查询本地用户信息UserDetails]: " + "not found");
                }
                
                //手动加两个权限
                Collection<GrantedAuthority> authorities = new ArrayList<>();
                authorities.addAll(defaultOAuth2User.getAuthorities());
                authorities.add(new SimpleGrantedAuthority("SCOPE_" + "message.read"));
                authorities.add(new SimpleGrantedAuthority("SCOPE_" + "message.write"));
                String userNameAttributeName = authenticationToken.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
                return new DefaultOAuth2User(authorities, defaultOAuth2User.getAttributes(), userNameAttributeName);

            } else if (authenticationToken.getPrincipal() instanceof DefaultOidcUser){
                DefaultOidcUser defaultOAuth2User = (DefaultOidcUser) authenticationToken.getPrincipal();
                return defaultOAuth2User;

            }
            return authenticationToken.getPrincipal();
        }
    }
}
