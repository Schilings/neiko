package com.schilings.neiko.auth.login;


import cn.dev33.satoken.session.SaSessionCustomUtil;
import cn.dev33.satoken.stp.StpInterface;
import com.schilings.neiko.auth.model.dto.AuthClientDetails;
import com.schilings.neiko.common.security.component.ClientDetailsService;
import com.schilings.neiko.common.security.component.UserDetailsService;
import com.schilings.neiko.common.security.holder.RBACAuthorityHolder;
import com.schilings.neiko.common.security.pojo.PermissionAuthority;
import com.schilings.neiko.common.security.pojo.RoleAuthority;
import com.schilings.neiko.common.security.pojo.UserDetails;
import com.schilings.neiko.extend.sa.token.core.StpOauth2UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 
 * <p>自定义权限获取Service</p>
 * 
 * @author Schilings
*/
@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private final UserDetailsService userDetailsService;
    
    @Override
    public List<String> getRoleList(Object userId, String loginType) {
        Set<String> roles = RBACAuthorityHolder.getRoles((String) userId, () -> {
            UserDetails userDetails = userDetailsService.loadUserByUserId((String) userId);
            return userDetails.getRoles().stream().map(RoleAuthority::getRole).collect(Collectors.toSet());
        });
        return new ArrayList<>(roles);
    }
    
    @Override
    public List<String> getPermissionList(Object userId, String loginType) {
        Set<String> permissions = RBACAuthorityHolder.getPermissions((String) userId, () -> {
            UserDetails userDetails = userDetailsService.loadUserByUserId((String) userId);            
            return userDetails.getPermissions().stream().map(PermissionAuthority::getPermission).collect(Collectors.toSet());
        });
        return new ArrayList<>(permissions);
    }

   
}
