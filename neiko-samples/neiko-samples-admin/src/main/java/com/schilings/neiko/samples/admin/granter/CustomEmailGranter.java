package com.schilings.neiko.samples.admin.granter;


import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.context.model.SaResponse;
import cn.dev33.satoken.oauth2.config.SaOAuth2Config;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Consts;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Util;
import cn.dev33.satoken.oauth2.model.AccessTokenModel;
import cn.dev33.satoken.oauth2.model.RequestAuthModel;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.common.security.constant.SecurityConstants;
import com.schilings.neiko.extend.sa.token.core.StpOAuth2UserUtil;
import com.schilings.neiko.extend.sa.token.holder.ApplicationEventPublisherHolder;
import com.schilings.neiko.extend.sa.token.oauth2.component.OAuth2Granter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomEmailGranter implements OAuth2Granter {
    @Override
    public boolean supports(String grantType) {
        return grantType.equals("email");
    }

    @Override
    public Object grantInternal(SaRequest req, SaResponse res, SaOAuth2Config cfg) {
        // 1、获取请求参数
        String username = req.getParamNotNull(SaOAuth2Consts.Param.username);
        String password = req.getParamNotNull(SaOAuth2Consts.Param.password);
        String clientId = req.getParamNotNull(SaOAuth2Consts.Param.client_id);
        String clientSecret = req.getParamNotNull(SaOAuth2Consts.Param.client_secret);
        String scope = req.getParam(SaOAuth2Consts.Param.scope, "");

        // 2、校验 ClientScope和ClientSecret
        SaOAuth2Util.checkContract(clientId, scope);
        SaOAuth2Util.checkClientSecret(clientId, clientSecret);

        // 3、防止因前端误传token造成逻辑干扰
        SaHolder.getStorage().set(StpOAuth2UserUtil.stpLogic.splicingKeyJustCreatedSave(), "no-token");

        // 4、调用API 开始登录，如果没能成功登录，则直接退出
        Object retObj = cfg.getDoLoginHandle().apply(username, password);
        if (StpOAuth2UserUtil.isLogin() == false) {
            return retObj;
        }

        // 5、构建 ra对象
        RequestAuthModel ra = new RequestAuthModel();
        ra.clientId = clientId;
        ra.loginId = StpOAuth2UserUtil.getLoginId();
        ra.scope = scope;

        // 7、生成 Access-Token
        AccessTokenModel at = SaOAuth2Util.generateAccessToken(ra, true);

        // 8、Token拓展
        return at.toLineMap();
    }
}
