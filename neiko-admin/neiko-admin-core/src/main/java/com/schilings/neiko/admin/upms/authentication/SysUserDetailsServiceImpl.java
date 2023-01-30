package com.schilings.neiko.admin.upms.authentication;

import cn.hutool.core.collection.CollectionUtil;

import com.schilings.neiko.authorization.common.constant.UserAttributeNameConstants;
import com.schilings.neiko.authorization.common.userdetails.User;
import com.schilings.neiko.system.model.dto.UserInfoDTO;
import com.schilings.neiko.system.model.entity.SysUser;
import com.schilings.neiko.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

@Slf4j
@RequiredArgsConstructor
public class SysUserDetailsServiceImpl implements UserDetailsService {

	private final SysUserService sysUserService;

	private final ObjectProvider<UserInfoCoordinator> userInfoCoordinatorProviders;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SysUser sysUser = sysUserService.getByUsername(username);
		if (sysUser == null) {
			log.error("登陆：用户名错误，用户名：{}", username);
			throw new UsernameNotFoundException("username error!");
		}
		UserInfoDTO userInfoDTO = sysUserService.findUserInfo(sysUser);
		return getUserByUserInfo(userInfoDTO);
	}

	/**
	 * 根据UserInfo 获取 User
	 * @param userInfoDTO 用户信息DTO
	 * @return UserDetails
	 */
	public User getUserByUserInfo(UserInfoDTO userInfoDTO) {

		SysUser sysUser = userInfoDTO.getSysUser();
		Collection<String> roleCodes = userInfoDTO.getRoleCodes();
		Collection<String> permissions = userInfoDTO.getPermissions();

		Collection<String> dbAuthsSet = new HashSet<>();
		if (CollectionUtil.isNotEmpty(roleCodes)) {
			// 获取角色
			dbAuthsSet.addAll(roleCodes);
			// 获取资源
			dbAuthsSet.addAll(permissions);

		}
		Collection<? extends GrantedAuthority> authorities = AuthorityUtils
				.createAuthorityList(dbAuthsSet.toArray(new String[0]));

		// 默认将角色和权限放入属性中
		HashMap<String, Object> attributes = new HashMap<>(8);
		attributes.put(UserAttributeNameConstants.ROLE_CODES, roleCodes);
		attributes.put(UserAttributeNameConstants.PERMISSIONS, permissions);

		// 用户额外属性
		userInfoCoordinatorProviders.stream().toList()
				.forEach(userInfoCoordinators -> userInfoCoordinators.coordinateAttribute(userInfoDTO, attributes));

		return new User(sysUser.getUserId(), sysUser.getUsername(), sysUser.getPassword(), sysUser.getNickname(),
				sysUser.getAvatar(), sysUser.getStatus(), sysUser.getOrganizationId(), sysUser.getType(), authorities,
				attributes);
	}

}
