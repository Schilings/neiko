package com.schilings.neiko.extend.sa.token.holder;


import com.schilings.neiko.common.util.spring.SpringUtils;
import com.schilings.neiko.extend.sa.token.oauth2.component.*;
import lombok.Setter;
import org.springframework.beans.factory.ObjectProvider;

import java.util.List;


public class ExtendComponentHolder {

    @Setter
    public static ObjectProvider<List<TokenEnhancer>> tokenEnhancersProvider;
    @Setter
    public static ObjectProvider<List<OAuth2Granter>> oAuth2GrantersProvider;
    @Setter
    public static LoginService loginService;
    @Setter
    public static ClientDetailsService clientDetailsService;
    @Setter
    public static UserDetailsService userDetailsService;
    
}
