package com.schilings.neiko.extend.sa.token.bean;


import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.SaLoginModel;
import com.schilings.neiko.extend.sa.token.holder.ApplicationEventPublisherHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

import java.util.Arrays;

/**
 * 
 * <p>Sa-Token 侦听器</p>
 * 
 * @author Schilings
*/
@Slf4j
@RequiredArgsConstructor
public class ExtendSaTokenListener implements SaTokenListener {


    @Override
    public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginModel loginModel) {
        //发布初始化权限事件
        ApplicationEventPublisherHolder.publisbAuthorityInitEvent(loginId, loginType, tokenValue);
    }


    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
        // 发布登出事件
        ApplicationEventPublisherHolder.publishLogoutSuccessEvent();
        // 删除缓存
        ApplicationEventPublisherHolder.publishRoleAuthorityChangedEvent(Arrays.asList((String)loginId));
    }
    
    @Override
    public void doKickout(String loginType, Object loginId, String tokenValue) {
        // 删除缓存
        ApplicationEventPublisherHolder.publishRoleAuthorityChangedEvent(Arrays.asList((String)loginId));
    }

    @Async
    @Override
    public void doReplaced(String loginType, Object loginId, String tokenValue) {

    }

    @Async
    @Override
    public void doDisable(String loginType, Object loginId, long disableTime) {

    }


    @Override
    public void doUntieDisable(String loginType, Object loginId) {
    }

    @Override
    public void doCreateSession(String id) {
    }
    

    @Override
    public void doLogoutSession(String id) {

    }
}
