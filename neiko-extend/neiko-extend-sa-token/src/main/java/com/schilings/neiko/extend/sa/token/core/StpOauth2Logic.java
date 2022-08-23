package com.schilings.neiko.extend.sa.token.core;


import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.annotation.SaCheckSafe;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Util;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.util.SaFoxUtil;
import com.schilings.neiko.extend.sa.token.oauth2.annotation.Oauth2CheckScope;

public class StpOauth2Logic extends StpLogic {
    
    /**
     * 初始化StpLogic, 并指定账号类型
     *
     * @param loginType 账号体系标识
     */
    public StpOauth2Logic(String loginType) {
        super(loginType);
    }

    /**
     * 根据注解(@Oauth2CheckScope)鉴权
     * @param at 注解对象 
     */
    public void checkByAnnotation(Oauth2CheckScope at) {
        String accessToken = SaHolder.getRequest().getHeader(SaManager.getConfig().getTokenName());
        String[] scopeArray = at.value();
        SaOAuth2Util.checkScope(accessToken,scopeArray);
    }
}
