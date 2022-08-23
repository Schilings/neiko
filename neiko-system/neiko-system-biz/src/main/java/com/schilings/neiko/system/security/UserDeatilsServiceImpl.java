package com.schilings.neiko.system.security;


import com.schilings.neiko.common.security.component.UserDetailsService;
import com.schilings.neiko.system.model.dto.UserInfoDTO;
import com.schilings.neiko.system.model.entity.SysUser;
import com.schilings.neiko.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class UserDeatilsServiceImpl implements UserDetailsService<AuthUserDeatails> {

    private final SysUserService sysUserService;
    
    @Override
    public AuthUserDeatails loadUserByUsername(String username) {
        SysUser sysUser = sysUserService.getByUsername(username);
        if (sysUser != null) {
            UserInfoDTO userInfo = sysUserService.findUserInfo(sysUser);
            if (userInfo != null) {
                return new AuthUserDeatails(userInfo);
            }
        }
        return null;
    }

    @Override
    public AuthUserDeatails loadUserByUserId(String userId) {
        SysUser sysUser = sysUserService.getById(Long.valueOf(userId));
        if (sysUser != null) {
            UserInfoDTO userInfo = sysUserService.findUserInfo(sysUser);
            if (userInfo != null) {
                return new AuthUserDeatails(userInfo);
            }
        }
        return null;
    }
}
