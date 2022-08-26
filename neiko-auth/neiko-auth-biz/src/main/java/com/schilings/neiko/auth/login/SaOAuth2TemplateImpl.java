package com.schilings.neiko.auth.login;

import cn.dev33.satoken.oauth2.exception.SaOAuth2Exception;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Consts;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Template;
import cn.dev33.satoken.oauth2.model.SaClientModel;
import cn.hutool.core.util.ObjectUtil;
import com.schilings.neiko.auth.check.PasswordChecker;
import com.schilings.neiko.auth.check.OpenClientChecker;
import com.schilings.neiko.auth.client.AuthClientDetails;
import com.schilings.neiko.auth.properties.AuthProperties;
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
public class SaOAuth2TemplateImpl extends SaOAuth2Template {

	private final PasswordChecker passwordChecker;

	private final OpenClientChecker openClientChecker;

	private final AuthProperties authProperties;

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
			if (clientDetails.getClientId().equals(clientId)) {
				SaClientModel saClientModel = new SaClientModel()
						// 客户端账号密码
						.setClientId(clientDetails.getClientId()).setClientSecret(clientDetails.getClientSecret())
						// 支持的url
						.setAllowUrl(String.join(",", clientDetails.getUrls()))
						// 支持的作用域
						.setContractScope(String.join(",", clientDetails.getScope()))
						// 支持的认证模式
						.setIsCode(clientDetails.getAuthorizedGrantTypes()
								.contains(SaOAuth2Consts.GrantType.authorization_code))
						.setIsNewRefresh(clientDetails.getAuthorizedGrantTypes()
								.contains(SaOAuth2Consts.GrantType.refresh_token))
						.setIsPassword(
								clientDetails.getAuthorizedGrantTypes().contains(SaOAuth2Consts.GrantType.password))
						.setIsImplicit(
								clientDetails.getAuthorizedGrantTypes().contains(SaOAuth2Consts.GrantType.implicit))
						.setIsClient(clientDetails.getAuthorizedGrantTypes()
								.contains(SaOAuth2Consts.GrantType.client_credentials))
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

	// ------------------- 资源校验API-------------------

	/**
	 * 重写 Access-Token 生成策略
	 * <p>
	 * https://sa-token.dev33.cn/doc/index.html#/oauth2/oauth2-interworking
	 * </p>
	 * @return
	 */
	@Override
	public String randomAccessToken(String clientId, Object loginId, String scope) {
		if (openClientChecker.isOpenByClientId(clientId)) {
			return authProperties.getOpenClient().getAccessToken();
		}
		return super.randomAccessToken(clientId, loginId, scope);
	}

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
	public void checkScope(String accessToken, String... scopes) {
		if (openClientChecker.isOpenByAccessToken(accessToken)) {
			return;
		}
		super.checkScope(accessToken, scopes);
	}

}
