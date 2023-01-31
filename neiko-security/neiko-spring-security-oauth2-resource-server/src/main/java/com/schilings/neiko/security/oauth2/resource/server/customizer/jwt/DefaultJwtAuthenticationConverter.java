package com.schilings.neiko.security.oauth2.resource.server.customizer.jwt;



import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

public class DefaultJwtAuthenticationConverter extends JwtAuthenticationConverter {
    
    public DefaultJwtAuthenticationConverter() {
        //自定义GrantedAuthoritiesConverter
        setJwtGrantedAuthoritiesConverter(new DefaultJwtGrantedAuthoritiesConverter());
    }
    
    
}
