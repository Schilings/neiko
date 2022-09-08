package com.schilings.neiko.auth.login;

import com.schilings.neiko.auth.checker.PasswordChecker;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.common.model.result.SystemResultCode;
import com.schilings.neiko.extend.sa.token.holder.ExtendComponentHolder;
import com.schilings.neiko.extend.sa.token.holder.RBACAuthorityHolder;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.RoleAuthority;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.UserDetails;
import com.schilings.neiko.extend.sa.token.core.StpOAuth2UserUtil;
import com.schilings.neiko.extend.sa.token.oauth2.component.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 *
 * <p>
 * 自定义登录流程Service
 * </p>
 *
 * @author Schilings
 */
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

	private final PasswordChecker passwordChecker;
	

	/**
	 * 密码式（Password）登录
	 * @param username 前端请求进来的username
	 * @param password 前端请求进来的password
	 * @return
	 */
	@Override
	public Object login(String username, String password) {
		UserDetails userDetails = ExtendComponentHolder.userDetailsService.loadUserByUsername(username);
		if (userDetails != null) {
			if (!userDetails.isEnabled()) {
				return R.fail(SystemResultCode.BAD_REQUEST, "账号不可用");
			}
			// 账号密码匹配
			if (username.equals(userDetails.getUsername())
					&& passwordChecker.check(password, userDetails.getPassword(), userDetails.getSalt())) {
				StpOAuth2UserUtil.login(userDetails.getUserId());
				RBACAuthorityHolder.setUserDetails(userDetails.getUserId(), userDetails);
				RBACAuthorityHolder.setRoles(userDetails.getUserId(), new ArrayList<>(userDetails.getRoles()));
				return R.ok();
			}
		}
		return R.fail(SystemResultCode.BAD_REQUEST, "账号名或密码错误");
	}



}
