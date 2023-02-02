package com.schilings.neiko.admin.upms.authentication;

import cn.hutool.core.collection.CollectionUtil;

import com.schilings.neiko.authorization.common.constant.UserAttributeNameConstants;
import com.schilings.neiko.authorization.common.userdetails.User;
import com.schilings.neiko.common.util.web.WebUtils;
import com.schilings.neiko.security.oauth2.authorization.server.util.OAuth2EndpointUtils;
import com.schilings.neiko.system.constant.SysUserConst;
import com.schilings.neiko.system.model.dto.UserInfoDTO;
import com.schilings.neiko.system.model.entity.SysUser;
import com.schilings.neiko.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class SysUserDetailsServiceImpl implements UserDetailsService {

	private static final String USER_INFO = "user_info";

	private static final String SYSTEM_USER_INFO = SysUserConst.Type.SYSTEM.name().toLowerCase() + "_" + USER_INFO;

	private static final String CUSTOMER_USER_INFO = SysUserConst.Type.CUSTOMER.name().toLowerCase() + "_" + USER_INFO;

	private final SysUserService sysUserService;

	private final ObjectProvider<UserInfoCoordinator> userInfoCoordinatorProviders;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SysUser sysUser = null;
		OAuth2ClientAuthenticationToken authenticatedClient = getAuthenticatedClient();
		if (authenticatedClient != null) {
			// 根据scope返回不同类型的用户
			Set<String> scopes = getScopes();
			// 如果同时有，是否拒绝该请求?
			if (scopes.contains(SYSTEM_USER_INFO)) {
				sysUser = sysUserService.getByUsernameAndType(username, SysUserConst.Type.SYSTEM.getValue());
			}
			else if (scopes.contains(CUSTOMER_USER_INFO)) {
				sysUser = sysUserService.getByUsernameAndType(username, SysUserConst.Type.CUSTOMER.getValue());
			}
			// 没有携带就查无用户

		}
		else {
			// 不是OAuth2 Password 走全部用户逻辑
			sysUser = sysUserService.getByUsername(username);
		}
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

	private OAuth2ClientAuthenticationToken getAuthenticatedClient() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			OAuth2ClientAuthenticationToken clientPrincipal = null;
			if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
				clientPrincipal = (OAuth2ClientAuthenticationToken) authentication;
			}
			if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
				clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
			}
			if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
				return clientPrincipal;
			}
		}
		return null;
	}

	private Set<String> getScopes() {
		// 如果是OAuth2 密码登录
		MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(WebUtils.getRequest());
		// scope
		String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
		if (!StringUtils.hasText(scope)) {
			return Collections.emptySet();
		}
		Set<String> requestedScopes = null;
		if (StringUtils.hasText(scope)) {
			requestedScopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
		}
		return requestedScopes;
	}

}
