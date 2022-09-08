package com.schilings.neiko.extend.sa.token.oauth2.event;

import cn.dev33.satoken.SaManager;
import com.schilings.neiko.extend.sa.token.holder.RBACAuthorityHolder;
import com.schilings.neiko.extend.sa.token.oauth2.event.authority.AuthorityInitEvent;
import com.schilings.neiko.extend.sa.token.oauth2.event.authority.PermissionAuthorityChangedEvent;
import com.schilings.neiko.extend.sa.token.oauth2.event.authority.RoleAuthorityChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

/**
 *
 * <p>
 * 处理权限变更等事件
 * </p>
 *
 * @author Schilings
 */
@RequiredArgsConstructor
public class AuthorityEventListener {

	@Async
	@EventListener(AuthorityInitEvent.class)
	public void authorityInit(AuthorityInitEvent event) {
		SaManager.getStpInterface().getRoleList(event.getLoginId(), event.getLoginType());
		SaManager.getStpInterface().getPermissionList(event.getLoginId(), event.getLoginType());
	}

	@Async
	@EventListener(RoleAuthorityChangedEvent.class)
	public void refreshUserRole(RoleAuthorityChangedEvent event) {
		event.getUserId().forEach(RBACAuthorityHolder::deleteRoles);
	}

	@Async
	@EventListener(PermissionAuthorityChangedEvent.class)
	public void refreshRolePermission(PermissionAuthorityChangedEvent event) {
		event.getRoleCode().forEach(RBACAuthorityHolder::deletePermissions);
	}

}
