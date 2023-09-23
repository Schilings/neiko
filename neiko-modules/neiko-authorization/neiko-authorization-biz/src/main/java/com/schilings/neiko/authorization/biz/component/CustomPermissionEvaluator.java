package com.schilings.neiko.authorization.biz.component;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

@Slf4j
public class CustomPermissionEvaluator {

	/**
	 * 判断接口是否有xxx:xxx权限
	 * @param permission 权限
	 * @return {boolean}
	 */
	public boolean hasPermission(String permission) {
		if (StrUtil.isBlank(permission)) {
			return false;
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return false;
		}
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		return authorities.stream().map(GrantedAuthority::getAuthority).filter(StringUtils::hasText)
				// 1. a:b 下所有权限都可以pass
				// requirePermission = a:b:* ,yourPermission = a:b:c ---> pass
				// requirePermission = a:b:* ,yourPermission = a:b:d ---> pass
				// 2. a:b:c权限都可以pass
				// requirePermission = a:b:c ,yourPermission = a:b:c ---> pass
				// requirePermission = a:b:c ,yourPermission = a:b:* ---> pass
				.anyMatch(x -> PatternMatchUtils.simpleMatch(permission, x)
						|| PatternMatchUtils.simpleMatch(x, permission));
	}

}
