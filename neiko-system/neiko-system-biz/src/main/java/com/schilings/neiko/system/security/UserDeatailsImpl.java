package com.schilings.neiko.system.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.*;
import com.schilings.neiko.system.constant.SysUserConst;
import com.schilings.neiko.system.model.dto.UserInfoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.util.Assert;

import java.util.*;
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

	private Map<String, Object> attributes;

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
		this.attributes = new HashMap<>();
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

	@JsonIgnore
	public Set<String> getRoleSet() {
		return roleSet;
	}

	@JsonIgnore
	public Set<String> getPermissionSet() {
		return permissionSet;
	}

	@Override
	@JsonIgnore
	public Collection<String> getPermissions() {
		return permissionSet;
	}

	@Override
	@JsonIgnore
	public Collection<String> getRoles() {
		return roleSet;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

}
