package com.schilings.neiko.autoconfigure.shiro.realm;

import com.schilings.neiko.autoconfigure.shiro.token.JWTToken;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.realm.AuthorizingRealm;

/**
 * @author Ken-Chy129
 * @date 2022/8/3 9:51
 */
public abstract class JWTRealm extends AuthorizingRealm {

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof JWTToken;
	}

}
