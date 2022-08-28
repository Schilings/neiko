package com.schilings.neiko.extend.sa.token.oauth2.pojo;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
public class AuthenticationImpl implements Authentication {

    private Object token;

    private UserDetails userDetails;

    private boolean authenticated;
    
    
    @Override
    public Object getTokenDetails() {
        return token;
    }

    @Override
    public UserDetails getUserDetails() {
        return userDetails;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }
}
