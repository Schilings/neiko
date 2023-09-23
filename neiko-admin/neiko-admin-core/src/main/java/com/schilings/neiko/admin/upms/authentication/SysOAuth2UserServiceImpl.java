package com.schilings.neiko.admin.upms.authentication;

import cn.hutool.core.collection.CollectionUtil;
import com.schilings.neiko.authorization.biz.federated.OAuth2UserService;
import com.schilings.neiko.authorization.common.constant.UserAttributeNameConstants;
import com.schilings.neiko.authorization.common.userdetails.User;
import com.schilings.neiko.system.constant.SysUserConst;
import com.schilings.neiko.system.model.dto.SysUserDTO;
import com.schilings.neiko.system.model.dto.UserInfoDTO;
import com.schilings.neiko.system.model.entity.SysUser;
import com.schilings.neiko.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class SysOAuth2UserServiceImpl implements OAuth2UserService {

	private final SysUserService sysUserService;

	private final ObjectProvider<UserInfoCoordinator> userInfoCoordinatorProviders;

	@Override
	public OAuth2User loadUser(OAuth2User oAuth2User, String userNameAttributeName) {
		// 通过username or phone or email
		String profile = oAuth2User.getName();
		SysUser sysUser = sysUserService.getOAuth2UserIfUnkonw(profile, profile, profile);
		if (sysUser == null) {
			addSysUser(oAuth2User, userNameAttributeName);
			// 直接返回
			return oAuth2User;
		}
		// 如果已存在该用户
		UserInfoDTO userInfoDTO = sysUserService.findUserInfo(sysUser);
		return getUserByUserInfo(userInfoDTO, oAuth2User);
	}

	public void addSysUser(OAuth2User oAuth2User, String userNameAttributeName) {
		SysUserDTO sysUserDTO = new SysUserDTO();
		// 作为username
		sysUserDTO.setUsername(oAuth2User.getName());
		// 随机密码
		sysUserDTO.setPass(UUID.randomUUID().toString());
		// 随机用户名
		sysUserDTO.setNickname(UUID.randomUUID().toString());
		// 状态正常
		sysUserDTO.setStatus(SysUserConst.Status.NORMAL.getValue());
		// 性别未知
		sysUserDTO.setSex(SysUserConst.Sex.UNKONW.getValue());
		// 持久化
		sysUserService.addSysUser(sysUserDTO);
	}

	/**
	 * 根据UserInfo和OAuth2User 获取 User
	 * @param userInfoDTO 用户信息DTO
	 * @param oAuth2User
	 * @return UserDetails
	 */
	public User getUserByUserInfo(UserInfoDTO userInfoDTO, OAuth2User oAuth2User) {

		SysUser sysUser = userInfoDTO.getSysUser();
		Collection<String> roleCodes = userInfoDTO.getRoleCodes();
		Collection<String> permissions = userInfoDTO.getPermissions();

		Collection<String> dbAuthsSet = new HashSet<>();
		if (CollectionUtil.isNotEmpty(roleCodes)) {
			// 获取角色
			dbAuthsSet.addAll(roleCodes);
			// 获取资源
			dbAuthsSet.addAll(permissions);
			// 合并OAuth2User
			dbAuthsSet.addAll(AuthorityUtils.authorityListToSet(oAuth2User.getAuthorities()));
		}
		List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(dbAuthsSet.toArray(new String[0]));

		// 默认将角色和权限放入属性中
		HashMap<String, Object> attributes = new HashMap<>(8);
		attributes.put(UserAttributeNameConstants.ROLE_CODES, roleCodes);
		attributes.put(UserAttributeNameConstants.PERMISSIONS, permissions);
		// 合并OAuth2User
		attributes.putAll(oAuth2User.getAttributes());

		// 用户额外属性
		userInfoCoordinatorProviders.stream().toList()
				.forEach(userInfoCoordinators -> userInfoCoordinators.coordinateAttribute(userInfoDTO, attributes));

		return new User(sysUser.getUserId(), sysUser.getUsername(), sysUser.getPassword(), sysUser.getNickname(),
				sysUser.getAvatar(), sysUser.getStatus(), sysUser.getOrganizationId(), sysUser.getType(), authorities,
				attributes);
	}

}
