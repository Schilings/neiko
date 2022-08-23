package com.schilings.neiko.common.security.properties;


import lombok.Getter;
import lombok.Setter;

/**
 * 
 * <p></p>
 * 
 * @author Schilings
*/
public class AuthPropertiesHolder {


    @Getter
    @Setter
    private static AuthProperties authProperties;
    
    public static long authorityCacheTimeout() {
        return authProperties.getAuthorityCache().getTimeout();
    }
}
