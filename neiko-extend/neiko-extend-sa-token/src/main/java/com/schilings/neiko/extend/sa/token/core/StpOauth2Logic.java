package com.schilings.neiko.extend.sa.token.core;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.annotation.SaCheckSafe;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Consts;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Util;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.util.SaFoxUtil;
import com.schilings.neiko.extend.sa.token.oauth2.annotation.Oauth2CheckScope;

public class StpOauth2Logic extends StpLogic {

	/**
	 * 初始化StpLogic, 并指定账号类型
	 * @param loginType 账号体系标识
	 */
	public StpOauth2Logic(String loginType) {
		super(loginType);
	}

	/**
	 * 根据注解(@Oauth2CheckScope)鉴权
	 * @param at 注解对象
	 */
	public void checkByAnnotation(Oauth2CheckScope at) {
		StpOauth2UserUtil.checkLogin();
		String accessToken = SaHolder.getRequest().getHeader(SaOAuth2Consts.Param.access_token);
		String[] scopeArray = at.value();
		SaOAuth2Util.checkScope(accessToken, scopeArray);
	}

	/**
	 * <p>
	 * 重写该逻辑，兼容自定义RBACAuthorityHolder
	 * </p>
	 * 获取当前会话账号id, 如果未登录，则抛出异常
	 * @return 账号id
	 */
	public Object getLoginId() {
		// 如果正在[临时身份切换], 则返回临时身份
		if (isSwitch()) {
			return getSwitchLoginId();
		}
		// 如果获取不到token，则抛出: 无token
		String tokenValue = getTokenValue();
		if (tokenValue == null) {
			throw NotLoginException.newInstance(loginType, NotLoginException.NOT_TOKEN);
		}
		// 查找此token对应loginId, 如果找不到则抛出：无效token
		String loginId = getLoginIdNotHandle(tokenValue);
		if (loginId == null) {
			throw NotLoginException.newInstance(loginType, NotLoginException.INVALID_TOKEN, tokenValue);
		}
		// 如果是已经过期，则抛出：已经过期
		if (loginId.equals(NotLoginException.TOKEN_TIMEOUT)) {
			throw NotLoginException.newInstance(loginType, NotLoginException.TOKEN_TIMEOUT, tokenValue);
		}
		// 如果是已经被顶替下去了, 则抛出：已被顶下线
		if (loginId.equals(NotLoginException.BE_REPLACED)) {
			throw NotLoginException.newInstance(loginType, NotLoginException.BE_REPLACED, tokenValue);
		}
		// 如果是已经被踢下线了, 则抛出：已被踢下线
		if (loginId.equals(NotLoginException.KICK_OUT)) {
			throw NotLoginException.newInstance(loginType, NotLoginException.KICK_OUT, tokenValue);
		}
		// 检查是否已经 [临时过期]
		checkActivityTimeout(tokenValue);
		// 如果配置了自动续签, 则: 更新[最后操作时间]
		if (getConfig().getAutoRenew()) {
			updateLastActivityToNow(tokenValue);
		}
		// 至此，返回loginId
		return loginId;
	}

}
