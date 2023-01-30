package com.schilings.neiko.security.oauth2.resource.server.customizer.jwt;


import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

public class DefaultJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    public DefaultJwtAuthenticationConverter() {
        this.jwtAuthenticationConverter = new JwtAuthenticationConverter();
        //不改变原本逻辑，只是更改如下
        this.jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new DefaultJwtGrantedAuthoritiesConverter());
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        return this.jwtAuthenticationConverter.convert(jwt);
    }
    
}
