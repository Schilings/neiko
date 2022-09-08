package com.schilings.neiko.auth.login;

import cn.dev33.satoken.oauth2.exception.SaOAuth2Exception;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Template;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Util;
import cn.dev33.satoken.oauth2.model.AccessTokenModel;
import cn.dev33.satoken.oauth2.model.SaClientModel;
import cn.hutool.core.util.ObjectUtil;
import com.schilings.neiko.auth.checker.PasswordChecker;
import com.schilings.neiko.auth.checker.OpenClientChecker;
import com.schilings.neiko.auth.client.AuthClientDetails;
import com.schilings.neiko.auth.properties.AuthProperties;
import com.schilings.neiko.extend.sa.token.bean.ExtendSaOAuth2Template;
import com.schilings.neiko.extend.sa.token.oauth2.ExtendClientModel;
import com.schilings.neiko.extend.sa.token.oauth2.component.ClientDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 *
 * <p>
 * Sa-Token OAuth2.0 整合实现
 * </p>
 *
 * @author Schilings
 */
@Component
@RequiredArgsConstructor
public class CustomSaOAuth2Template extends ExtendSaOAuth2Template {

	private final PasswordChecker passwordChecker;

	private final OpenClientChecker openClientChecker;

	// ------------------- 资源校验API-------------------

	/**
	 * 校验：clientId 与 clientSecret 是否正确
	 * @param clientId 应用id
	 * @param clientSecret 秘钥
	 * @return SaClientModel对象
	 */
	@Override
	public SaClientModel checkClientSecret(String clientId, String clientSecret) {
		SaClientModel cm = checkClientModel(clientId);
		boolean allow = openClientChecker.isOpenByClientId(clientId)
				|| passwordChecker.check(clientSecret, cm.clientSecret, null);
		SaOAuth2Exception.throwBy(cm.clientSecret == null || allow == false, "无效client_secret: " + clientSecret);
		return cm;
	}

	/**
	 * 校验：指定 Access-Token 是否具有指定 Scope
	 * @param accessToken Access-Token
	 * @param scopes 需要校验的权限列表
	 */
	@Override
	public void checkScope(String accessToken, String... scopes) {
		AccessTokenModel accessTokenModel = SaOAuth2Util.getAccessToken(accessToken);
		if (openClientChecker.isOpenByClientId(accessTokenModel.clientId)) {
			return;
		}
		super.checkScope(accessToken, scopes);
	}

}
