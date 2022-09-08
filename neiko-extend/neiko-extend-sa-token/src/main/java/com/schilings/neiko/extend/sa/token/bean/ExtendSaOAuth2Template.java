package com.schilings.neiko.extend.sa.token.bean;

import cn.dev33.satoken.oauth2.logic.SaOAuth2Template;
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

}
