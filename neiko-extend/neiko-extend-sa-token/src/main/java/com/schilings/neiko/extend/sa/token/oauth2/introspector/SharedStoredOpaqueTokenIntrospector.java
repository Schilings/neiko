package com.schilings.neiko.extend.sa.token.oauth2.introspector;

import com.schilings.neiko.extend.sa.token.oauth2.pojo.Authentication;

/**
 * 
 * <p>共享存储的不透明令牌的处理器</p>
 * <p>当资源服务器与授权服务器共享了 TokenDao,比如使用redis或者数据库。</p>
 * 
 * @author Schilings
*/
public class SharedStoredOpaqueTokenIntrospector implements OpaqueTokenIntrospector {
    
    @Override
    public Authentication introspect(String accessToken) {
        //TODO do nothing
        return null;
    }
}
