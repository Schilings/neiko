package com.schilings.neiko.extend.sa.token.oauth2.component;

import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.context.model.SaResponse;
import cn.dev33.satoken.oauth2.config.SaOAuth2Config;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.extend.sa.token.holder.ApplicationEventPublisherHolder;

import java.util.HashMap;
import java.util.Map;

import static com.schilings.neiko.extend.sa.token.oauth2.ExtendOAuth2Handler.enhanceToken;

/**
 *
 * <p>
 * 自定义授权模式接口
 * </p>
 *
 * @author Schilings
 */
public interface OAuth2Granter {

	/**
	 * 是否支持该授权类型
	 * @param grantType
	 * @return
	 */
	boolean supports(String grantType);

	/**
	 * 授权
	 * @param req
	 * @param res
	 * @param cfg
	 * @return
	 */
	Object grantInternal(SaRequest req, SaResponse res, SaOAuth2Config cfg);

	default Object grant(SaRequest req, SaResponse res, SaOAuth2Config cfg) {
		Object o = grantInternal(req, res, cfg);
		Map<String, Object> token;
		if (o instanceof HashMap) {
			token = (Map<String, Object>) o;
		}
		else {
			return o;
		}
		// Token增强
		token = enhanceToken(token);
		// 发布登录成功事件
		ApplicationEventPublisherHolder.publishAuthenticationSuccessEvent(token);
		// 返回 Access-Token
		return R.ok(token);
	}

}
