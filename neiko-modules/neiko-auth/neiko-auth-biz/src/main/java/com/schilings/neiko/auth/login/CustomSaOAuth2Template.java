package com.schilings.neiko.auth.login;

import cn.dev33.satoken.oauth2.exception.SaOAuth2Exception;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Util;
import cn.dev33.satoken.oauth2.model.AccessTokenModel;
import cn.dev33.satoken.oauth2.model.SaClientModel;
import cn.dev33.satoken.util.SaFoxUtil;
import com.schilings.neiko.auth.checker.PasswordChecker;
import com.schilings.neiko.auth.checker.OpenClientChecker;
import com.schilings.neiko.common.security.constant.SecurityConstants;
import com.schilings.neiko.extend.sa.token.bean.ExtendSaOAuth2Template;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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
		if (scopes == null || scopes.length == 0) {
			return;
		}
		AccessTokenModel at = checkAccessToken(accessToken);
		List<String> scopeList = SaFoxUtil.convertStringToList(at.scope);
		if (scopeList.contains(SecurityConstants.Scope.all)) {
			return;
		}
		for (String scope : scopes) {
			SaOAuth2Exception.throwBy(scopeList.contains(scope) == false, "该 Access-Token 不具备 Scope：" + scope);
		}
	}

}
