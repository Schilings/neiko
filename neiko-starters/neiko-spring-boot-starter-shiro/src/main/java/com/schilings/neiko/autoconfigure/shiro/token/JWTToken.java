package com.schilings.neiko.autoconfigure.shiro.token;

import lombok.AllArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author Ken-Chy129
 * @date 2022/8/4 14:09
 */
@AllArgsConstructor
public class JWTToken implements AuthenticationToken {

    private String token;
    
    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
    
    public String getToken() { return token; }
}
