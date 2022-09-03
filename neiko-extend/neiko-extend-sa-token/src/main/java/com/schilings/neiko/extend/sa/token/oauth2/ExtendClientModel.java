package com.schilings.neiko.extend.sa.token.oauth2;


import cn.dev33.satoken.oauth2.logic.SaOAuth2Consts;
import cn.dev33.satoken.oauth2.model.SaClientModel;
import org.springframework.util.CollectionUtils;

import java.util.Set;

public class ExtendClientModel extends SaClientModel {
    
    public Set<String> grantTypes;

    public Set<String> getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(Set<String> grantTypes) {
        this.grantTypes = grantTypes;
        setIsCode(grantTypes.contains(SaOAuth2Consts.GrantType.authorization_code));
        setIsNewRefresh(grantTypes.contains(SaOAuth2Consts.GrantType.refresh_token));
        setIsPassword(grantTypes.contains(SaOAuth2Consts.GrantType.password));
        setIsImplicit(grantTypes.contains(SaOAuth2Consts.GrantType.implicit));
        setIsClient(grantTypes.contains(SaOAuth2Consts.GrantType.client_credentials));
    }

    /**
     * 是否支持指定授权模式
     * @param grantType
     * @return
     */
    public boolean supportGrantType(String grantType) {
        return !CollectionUtils.isEmpty(this.grantTypes) && this.grantTypes.contains(grantType);
    }
}
