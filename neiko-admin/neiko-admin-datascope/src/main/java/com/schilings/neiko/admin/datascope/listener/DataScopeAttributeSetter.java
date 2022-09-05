package com.schilings.neiko.admin.datascope.listener;


import cn.dev33.satoken.SaManager;
import com.schilings.neiko.admin.datascope.component.UserDataScope;
import com.schilings.neiko.admin.datascope.component.UserDataScopeProcessor;
import com.schilings.neiko.common.security.constant.UserAttributeNameConstants;
import com.schilings.neiko.extend.sa.token.core.StpOAuth2UserUtil;
import com.schilings.neiko.extend.sa.token.holder.RBACAuthorityHolder;
import com.schilings.neiko.extend.sa.token.oauth2.event.authentication.AuthenticationSuccessEvent;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.Authentication;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
public class DataScopeAttributeSetter {

    private final UserDataScopeProcessor dataScopeProcessor;
    
    @EventListener(value = AuthenticationSuccessEvent.class)
    public void dataScopeAttributeSetter(AuthenticationSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
        UserDetails userDetails = authentication.getUserDetails();
        UserDataScope userDataScope = dataScopeProcessor.mergeScopeType(
                (Long) StpOAuth2UserUtil.getLoginId(), SaManager.getStpInterface().getRoleList(userDetails.getUserId(), StpOAuth2UserUtil.getLoginType())
        );
        userDetails.getAttributes().put(UserAttributeNameConstants.USER_DATA_SCOPE, userDataScope);
        RBACAuthorityHolder.setUserDetails(userDetails);
    }
}
