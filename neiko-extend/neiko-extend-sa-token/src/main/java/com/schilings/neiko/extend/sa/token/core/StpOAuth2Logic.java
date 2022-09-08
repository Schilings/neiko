package com.schilings.neiko.extend.sa.token.core;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Consts;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Util;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.util.SaFoxUtil;
import cn.dev33.satoken.util.SaTokenConsts;
import com.schilings.neiko.extend.sa.token.holder.ApplicationEventPublisherHolder;
import com.schilings.neiko.extend.sa.token.holder.RBACAuthorityHolder;
import com.schilings.neiko.extend.sa.token.oauth2.annotation.OAuth2CheckScope;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StpOAuth2Logic extends StpLogic {

	/**
	 * 初始化StpLogic, 并指定账号类型
	 * @param loginType 账号体系标识
	 */
	public StpOAuth2Logic(String loginType) {
		super(loginType);
	}

	/**
	 * 根据注解(@Oauth2CheckScope)鉴权
	 * @param at 注解对象
	 */
	public void checkByAnnotation(OAuth2CheckScope at) {
		StpOAuth2UserUtil.checkLogin();
		String accessToken = SaHolder.getRequest().getHeader(SaOAuth2Consts.Param.access_token);
		String[] scopeArray = at.value();
		SaOAuth2Util.checkScope(accessToken, scopeArray);
	}

	/**
	 * 获取当前TokenValue
	 * @return 当前tokenValue
	 */
	public String getTokenValue() {
		// 1. 获取
		String tokenValue = getTokenValueNotCut();

		// 2. 如果打开了前缀模式，则裁剪掉
		String tokenPrefix = getConfig().getTokenPrefix();
		if (SaFoxUtil.isEmpty(tokenPrefix) == false) {
			// 如果token并没有按照指定的前缀开头，则视为未提供token
			if (SaFoxUtil.isEmpty(tokenValue)
					|| tokenValue.startsWith(tokenPrefix + SaTokenConsts.TOKEN_CONNECTOR_CHAT) == false) {
				tokenValue = null;
			}
			else {
				// 则裁剪掉前缀
				tokenValue = tokenValue.substring(tokenPrefix.length() + SaTokenConsts.TOKEN_CONNECTOR_CHAT.length());
			}
		}

		// 3. 返回
		return tokenValue;
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

	/**
	 * 重写该逻辑，优先判断缓存中有无 获取：指定账号的角色集合
	 * @param loginId 指定账号id
	 */
	public List<String> getRoleList(Object loginId) {
		List<String> roles = RBACAuthorityHolder.getRoles((String) loginId);
		// 存放了有值或空值
		if (roles != null) {// 空值为Collections.emptyList();
			return roles;
		}
		// 否则
		return SaManager.getStpInterface().getRoleList(loginId, loginType);
	}

	/**
	 * 重写该逻辑，优先判断缓存中有无 获取：指定账号的权限码集合
	 * @param loginId 指定账号id
	 */
	public List<String> getPermissionList(Object loginId) {
		// 1. 声明权限码集合
		List<String> permissionList = new ArrayList<>();
		boolean notEmpty = false;
		// 2. 遍历角色列表，查询拥有的权限码
		List<String> roleList = getRoleList(loginId);
		if (roleList != null && roleList.isEmpty()) {// 空值为Collections.emptyList();
			notEmpty = true;
		}
		for (String roleId : roleList) {
			List<String> permissions = RBACAuthorityHolder.getPermissions(roleId);
			if (permissions != null) {// 空值为Collections.emptyList();
				notEmpty = true;
				permissionList.addAll(permissions);
			}
		}
		// 存放了有值或空值
		if (notEmpty) {
			return permissionList;
		}
		// 否则
		return SaManager.getStpInterface().getPermissionList(loginId, loginType);
	}

}
