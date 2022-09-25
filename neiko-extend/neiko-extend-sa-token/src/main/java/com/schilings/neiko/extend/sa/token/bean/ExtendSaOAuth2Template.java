package com.schilings.neiko.extend.sa.token.bean;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.oauth2.exception.SaOAuth2Exception;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Template;
import cn.dev33.satoken.oauth2.model.AccessTokenModel;
import cn.dev33.satoken.oauth2.model.RefreshTokenModel;
import cn.dev33.satoken.util.SaFoxUtil;
import cn.hutool.core.util.ObjectUtil;
import com.schilings.neiko.extend.sa.token.core.StpOAuth2UserUtil;
import com.schilings.neiko.extend.sa.token.holder.ExtendComponentHolder;
import com.schilings.neiko.extend.sa.token.oauth2.ExtendClientModel;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.ClientDetails;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * <p>
 * Sa-Token-OAuth2 模块
 * </p>
 *
 * @author Schilings
 */
@Slf4j
public class ExtendSaOAuth2Template extends SaOAuth2Template {

	/**
	 * 待改动
	 * @param clientId
	 * @return
	 */
	@Override
	public ExtendClientModel getClientModel(String clientId) {
		ClientDetails clientDetails = ExtendComponentHolder.clientDetailsService.loadClientByClientId(clientId);
		if (clientDetails != null) {
			if (clientDetails.getClientId().equals(clientId)) {
				ExtendClientModel saClientModel = new ExtendClientModel();
				// 客户端账号密码
				saClientModel.setClientId(clientDetails.getClientId());
				saClientModel.setClientSecret(clientDetails.getClientSecret());
				// 支持的url
				saClientModel.setAllowUrl(String.join(",", clientDetails.getUrls()));
				// 支持的作用域
				saClientModel.setContractScope(String.join(",", clientDetails.getScope()));
				// 支持的认证模式
				saClientModel.setGrantTypes(clientDetails.getAuthorizedGrantTypes());
				saClientModel.setIsAutoMode(false);
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
	 * 重写 Access-Token 生成策略
	 * <p>
	 * https://sa-token.dev33.cn/doc/index.html#/oauth2/oauth2-interworking
	 * </p>
	 * @return
	 */
	@Override
	public String randomAccessToken(String clientId, Object loginId, String scope) {
		return StpOAuth2UserUtil.createLoginSession(loginId);
	}

	/**
	 * 重写Access-Token校验逻辑添加前缀可选项
	 * @param accessToken
	 * @return
	 */
	@Override
	public AccessTokenModel getAccessToken(String accessToken) {
		if (accessToken == null) {
			return null;
		}
		// 如果配置了前缀.则减去前缀
		String tokenPrefix = StpOAuth2UserUtil.stpLogic.getConfig().getTokenPrefix();
		if (!SaFoxUtil.isEmpty(tokenPrefix)) {
			if (!SaFoxUtil.isEmpty(accessToken) && accessToken.startsWith(tokenPrefix + " ")) {
				accessToken = accessToken.substring(tokenPrefix.length() + " ".length());
			}
			else {
				accessToken = null;
			}
		}
		return (AccessTokenModel) SaManager.getSaTokenDao().getObject(splicingAccessTokenSaveKey(accessToken));
	}

	/**
	 * 刷新Model：根据 Refresh-Token 生成一个新的 Access-Token
	 * @param refreshToken Refresh-Token值
	 * @return 新的 Access-Token
	 */
	@Override
	public AccessTokenModel refreshAccessToken(String refreshToken) {

		// 获取 Refresh-Token 信息
		RefreshTokenModel rt = getRefreshToken(refreshToken);
		SaOAuth2Exception.throwBy(rt == null, "无效refresh_token: " + refreshToken);

		// 如果配置了[每次刷新产生新的Refresh-Token]
		if (checkClientModel(rt.clientId).getIsNewRefresh()) {
			// 删除旧 Refresh-Token
			deleteRefreshToken(rt.refreshToken);

			// 创建并保持新的 Refresh-Token
			rt = convertRefreshTokenToRefreshToken(rt);
			saveRefreshToken(rt);
			saveRefreshTokenIndex(rt);
		}

		// 删除旧 Access-Token
		deleteAccessToken(getAccessTokenValue(rt.clientId, rt.loginId));

		// 生成新 Access-Token
		AccessTokenModel at = convertRefreshTokenToAccessToken(rt);

		// 保存新 Access-Token
		saveAccessToken(at);
		saveAccessTokenIndex(at);

		// 返回新 Access-Token
		return at;
	}

}
