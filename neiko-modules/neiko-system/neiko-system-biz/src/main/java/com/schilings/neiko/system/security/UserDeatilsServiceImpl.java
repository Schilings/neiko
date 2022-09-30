package com.schilings.neiko.system.security;

import com.schilings.neiko.extend.sa.token.oauth2.component.UserDetailsService;
import com.schilings.neiko.system.model.dto.UserInfoDTO;
import com.schilings.neiko.system.model.entity.SysUser;
import com.schilings.neiko.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDeatilsServiceImpl implements UserDetailsService<UserDeatailsImpl> {

	private final SysUserService sysUserService;

	@Override
	public UserDeatailsImpl loadUserByUsername(String username) {
		SysUser sysUser = sysUserService.getByUsername(username);
		// 存在且可用
		if (sysUser != null) {
			UserInfoDTO userInfo = sysUserService.findUserInfo(sysUser);
			if (userInfo != null) {
				return new UserDeatailsImpl(userInfo);
			}
		}
		return null;
	}

}
