package com.schilings.neiko.auth.login;


import cn.dev33.satoken.oauth2.logic.SaOAuth2Consts;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Template;
import cn.dev33.satoken.oauth2.model.SaClientModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.schilings.neiko.auth.model.dto.AuthClientDetails;
import com.schilings.neiko.common.security.component.ClientDetailsService;
import com.schilings.neiko.common.security.pojo.ClientDetails;
import com.schilings.neiko.extend.sa.token.core.StpOauth2UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 
 * <p>Sa-Token OAuth2.0 整合实现</p>
 * 
 * @author Schilings
*/
@Component
@RequiredArgsConstructor
public class SaOAuth2TemplateImpl extends SaOAuth2Template {
    
    private final ClientDetailsService<AuthClientDetails> clientDetailsService;

    /**
     * 待改动
     * @param clientId
     * @return
     */
    @Override
    public SaClientModel getClientModel(String clientId) {
        AuthClientDetails clientDetails = this.clientDetailsService.loadClientByClientId(clientId);
        if (clientDetails != null) {
            if(clientDetails.getClientId().equals(clientId)) {
                SaClientModel saClientModel = new SaClientModel()
                        .setClientId(clientDetails.getClientId())
                        .setClientSecret(clientDetails.getClientSecret())
                        .setAllowUrl(String.join(",", clientDetails.getUrls()))
                        .setContractScope(String.join(",", clientDetails.getScope()))
                        .setIsCode(clientDetails.getAuthorizedGrantTypes().contains(SaOAuth2Consts.GrantType.authorization_code))
                        .setIsNewRefresh(clientDetails.getAuthorizedGrantTypes().contains(SaOAuth2Consts.GrantType.refresh_token))
                        .setIsPassword(clientDetails.getAuthorizedGrantTypes().contains(SaOAuth2Consts.GrantType.password))
                        .setIsImplicit(clientDetails.getAuthorizedGrantTypes().contains(SaOAuth2Consts.GrantType.implicit))
                        .setIsClient(clientDetails.getAuthorizedGrantTypes().contains(SaOAuth2Consts.GrantType.client_credentials))
                        .setIsAutoMode(true);
                if (ObjectUtil.isNotNull(clientDetails.getAccessTokenTimeout())) {
                    saClientModel.setAccessTokenTimeout(clientDetails.getAccessTokenTimeout());
                }
                if (ObjectUtil.isNotNull(clientDetails.getRefreshTokenTimeout())) {
                    saClientModel.setRefreshTokenTimeout(clientDetails.getRefreshTokenTimeout());
                }
                return saClientModel;
            }
        }
        return null;
    }
    
    @Override
    public String getOpenid(String clientId, Object loginId) {
        
        // 此为模拟数据，真实环境需要从数据库查询 
        return "";
    }


    /**
     * 重写 Access-Token 生成策略：复用登录会话的Token 
     * https://sa-token.dev33.cn/doc/index.html#/oauth2/oauth2-interworking
     * @param clientId
     * @param loginId
     * @param scope
     * @return
     */
    @Override
    public String randomAccessToken(String clientId, Object loginId, String scope) {
        return StpOauth2UserUtil.createLoginSession(loginId);
    }
    
}
