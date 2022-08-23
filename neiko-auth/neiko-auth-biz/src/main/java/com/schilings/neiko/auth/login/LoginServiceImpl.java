package com.schilings.neiko.auth.login;


import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.common.security.component.UserDetailsService;
import com.schilings.neiko.common.security.event.RoleAuthorityChangedEvent;
import com.schilings.neiko.common.security.pojo.UserDetails;
import com.schilings.neiko.common.util.spring.SpringUtils;
import com.schilings.neiko.extend.sa.token.core.StpOauth2UserUtil;
import com.schilings.neiko.extend.sa.token.oauth2.component.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


/**
 * 
 * <p>自定义登录流程Service</p>
 * 
 * @author Schilings
*/
@Component
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserDetailsService userDetailsService;
    
    @Override
    public Object notLoginView() {
        return R.fail("未登录");
    }

    @Override
    public Object confirmView(String clientId, String scope) {
        return R.fail("暂时无授权认证");
    }

    /**
     * 密码式（Password）登录
     * @param username 前端请求进来的username
     * @param password 前端请求进来的password
     * @return
     */
    @Override
    public Object passwordLogin(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails != null) {
            if(username.equals(userDetails.getUsername()) && password.equals(userDetails.getPassword())) {
                StpOauth2UserUtil.login(userDetails.getUserId());
                return R.ok();
            }
        }
        return R.fail("账号名或密码错误");
    }


    /**
     * 注销登录
     * @param loginId
     */
    @Override
    public void logout(String loginId) {
        StpOauth2UserUtil.logout(loginId);
        //删除缓存
        SpringUtils.publishEvent(new RoleAuthorityChangedEvent(loginId));
    }
}
