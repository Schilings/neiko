package com.schilings.neiko.security.oauth2.authorization.server.configurer;


import com.schilings.neiko.security.oauth2.authorization.server.OAuth2AuthorizationServerExtensionConfigurer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * 
 * <p>希望最晚触发</p>
 * 
 * @author Schilings
*/
@Order(Ordered.LOWEST_PRECEDENCE)
public class LastTriggeredAuthenticatedConfigurer extends OAuth2AuthorizationServerExtensionConfigurer<LastTriggeredAuthenticatedConfigurer, HttpSecurity> {
    @Override
    public void init(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().anyRequest().authenticated();
    }
}
