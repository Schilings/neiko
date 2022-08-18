package com.schilings.neiko.samples.shiro.realm;

import cn.hutool.core.util.ObjectUtil;
import com.schilings.neiko.autoconfigure.shiro.exception.SubjectNotExistException;
import com.schilings.neiko.autoconfigure.shiro.realm.JWTRealm;
import com.schilings.neiko.autoconfigure.shiro.token.JWTRepository;
import com.schilings.neiko.autoconfigure.shiro.token.JWTToken;
import com.schilings.neiko.samples.shiro.entity.User;
import com.schilings.neiko.samples.shiro.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * <p></p>
 * </pre>
 *
 * @author <a href="https://github.com/kil1ua">Ken-Chy129</a>
 * @date 2022/8/10 14:25
 */
@Component
public class MyRealm extends JWTRealm {
    
    @Autowired
    private JWTRepository jWTRepository;
    
    @Autowired
    private UserService userService;
    
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRole("admin");
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JWTToken jwtToken = (JWTToken) authenticationToken;
        String token = jwtToken.getToken();
        String username = jWTRepository.verify(token).getClaim("username").asString();
        User user = userService.selectByUsername(username);
        if (ObjectUtil.isNull(user)) {
            throw new SubjectNotExistException("cannot find '" + username + "'");
        }
        return new SimpleAuthenticationInfo(token, token, getName());
    }
}
