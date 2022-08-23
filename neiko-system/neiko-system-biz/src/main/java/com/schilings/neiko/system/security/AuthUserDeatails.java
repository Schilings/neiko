package com.schilings.neiko.system.security;


import com.schilings.neiko.common.security.pojo.GrantedAuthority;
import com.schilings.neiko.common.security.pojo.PermissionAuthority;
import com.schilings.neiko.common.security.pojo.RoleAuthority;
import com.schilings.neiko.common.security.pojo.UserDetails;
import com.schilings.neiko.system.model.dto.UserInfoDTO;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.stream.Collectors;

public class AuthUserDeatails implements UserDetails {

    private final UserInfoDTO userInfoDTO;

    public AuthUserDeatails(UserInfoDTO userInfoDTO) {
        this.userInfoDTO = userInfoDTO;
        Assert.notNull(this.userInfoDTO,"UserInfoDTO in AuthUserDeatails must not be null.");
    }

    @Override
    public String getOpenId() {
        return null;
    }

    @Override
    public String getUserId() {
        return userInfoDTO.getSysUser().getUserId().toString();
    }
    
    @Override
    public String getUsername() {
        return userInfoDTO.getSysUser().getUsername();
    }

    @Override
    public String getPassword() {
        return userInfoDTO.getSysUser().getPassword();
    }

    @Override
    public String getSalt() {
        return userInfoDTO.getSysUser().getSalt();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Collection<PermissionAuthority> getPermissions() {
        return userInfoDTO.getPermissions().stream().map(s -> new PermissionAuthority() {
            @Override
            public String getPermission() {
                return s;
            }
        }).collect(Collectors.toSet());
    }
    
    @Override
    public Collection<RoleAuthority> getRoles() {
        return userInfoDTO.getRoleCodes().stream().map(s -> new RoleAuthority() {
            @Override
            public String getRole() {
                return s;
            }
        }).collect(Collectors.toSet());
    }
}
