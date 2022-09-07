package com.schilings.neiko.extend.sa.token.oauth2.component.impl;


import cn.dev33.satoken.oauth2.logic.SaOAuth2Util;
import com.schilings.neiko.extend.sa.token.core.StpOAuth2UserUtil;
import com.schilings.neiko.extend.sa.token.oauth2.component.OpaqueTokenIntrospector;

/**
 * 
 * <p>共享存储的不透明令牌的处理器</p>
 * <p>当资源服务器与授权服务器共享了 TokenDao,比如使用redis或者数据库。</p>
 * 
 * @author Schilings
*/
public class SharedStoredOpaqueTokenIntrospector implements OpaqueTokenIntrospector {
    
    
    
    @Override
    public Object introspect(String token) {
//        SaOAuth2Util.getAccessToken()
//        StpOAuth2UserUtil.getTokenInfo()
        
        return null;
    }
}
