package com.schilings.neiko.system.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.*;
import com.schilings.neiko.system.constant.SysUserConst;
import com.schilings.neiko.system.model.dto.UserInfoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Schema(title = "用户信息")
public class UserDeatailsImpl implements UserDetails {

	private String openId;

	private String userId;

	private String username;

	private String password;

	private String salt;

	private Set<String> roleSet;

	private Set<String> permissionSet;

	private boolean enabled;

	public UserDeatailsImpl() {

	}

	public UserDeatailsImpl(UserInfoDTO userInfoDTO) {
		Assert.notNull(userInfoDTO, "Param UserInfoDTO for AuthUserDeatails must not be null.");
		this.userId = userInfoDTO.getSysUser().getUserId().toString();
		this.username = userInfoDTO.getSysUser().getUsername();
		this.password = userInfoDTO.getSysUser().getPassword();
		this.salt = userInfoDTO.getSysUser().getSalt();
		this.enabled = userInfoDTO.getSysUser().getStatus().equals(SysUserConst.Status.NORMAL.getValue());
		this.roleSet = new HashSet<>(userInfoDTO.getRoleCodes());
		this.permissionSet = new HashSet<>(userInfoDTO.getPermissions());

	}

	@Override
	public String getOpenId() {
		return null;
	}

	@Override
	public String getUserId() {
		return userId;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getSalt() {
		return salt;
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	@JsonIgnore
	public Collection<PermissionAuthority> getPermissions() {
		return permissionSet.stream().map(PermissionAuthorityImpl::new).collect(Collectors.toSet());
	}

	@Override
	@JsonIgnore
	public Collection<RoleAuthority> getRoles() {
		return roleSet.stream().map(RoleAuthorityImpl::new).collect(Collectors.toSet());
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

}
