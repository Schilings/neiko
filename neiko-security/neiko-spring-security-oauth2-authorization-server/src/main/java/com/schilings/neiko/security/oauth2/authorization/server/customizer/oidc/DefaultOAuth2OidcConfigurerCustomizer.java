package com.schilings.neiko.security.oauth2.authorization.server.customizer.oidc;


import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerConfigurerCustomizer;


import com.schilings.neiko.security.oauth2.authorization.server.customizer.oidc.userinfo.DefaultOidcUserInfoMapper;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.oidc.userinfo.OidcUserInfoMapper;
import com.schilings.neiko.security.oauth2.authorization.server.util.OAuth2ConfigurerUtils;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;



/**
 * 
 * <p> Enable OpenID Connect 1.0</p>
 * 
 * @author Schilings
*/
public class DefaultOAuth2OidcConfigurerCustomizer implements OAuth2AuthorizationServerConfigurerCustomizer {


    private OidcUserInfoMapper userInfoMapper = new DefaultOidcUserInfoMapper();

    
    @Override
    public void customize(OAuth2AuthorizationServerConfigurer configurer, HttpSecurity http) throws Exception {
        // UserInfo Endpoint是受保护的资源之一，需要解析BearerToken
        
        // 使用共享时自省 这样子JWT和OpaqueToken都能访问UserInfo Endpoint
        //http.oauth2ResourceServer().opaqueToken().introspector(new OidcOpaqueTokenIntrospector(OAuth2ConfigurerUtils.getAuthorizationService(http),OAuth2ConfigurerUtils.getRegisteredClientRepository(http)));
        // 推荐使用JWT
        http.oauth2ResourceServer().jwt().decoder(getJwtDecoder(http));
        
        configurer.oidc(oidc -> {
            oidc.userInfoEndpoint(userInfo -> {
                userInfo.userInfoMapper(this.userInfoMapper);
            });
        });
    }

    private JwtDecoder getJwtDecoder(HttpSecurity http) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(OAuth2ConfigurerUtils.getJwkSource(http));
    }

    public DefaultOAuth2OidcConfigurerCustomizer userInfoMapper(OidcUserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
        return this;
    }


}
