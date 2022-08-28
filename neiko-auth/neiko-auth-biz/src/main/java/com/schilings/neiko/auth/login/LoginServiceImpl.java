package com.schilings.neiko.auth.login;

import com.schilings.neiko.auth.check.PasswordChecker;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.extend.sa.token.holder.RBACAuthorityHolder;
import com.schilings.neiko.extend.sa.token.oauth2.component.UserDetailsService;
import com.schilings.neiko.extend.sa.token.oauth2.event.authority.RoleAuthorityChangedEvent;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.UserDetails;
import com.schilings.neiko.common.util.spring.SpringUtils;
import com.schilings.neiko.extend.sa.token.core.StpOauth2UserUtil;
import com.schilings.neiko.extend.sa.token.oauth2.component.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

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

	private final UserDetailsService userDetailsService;

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
			if (!userDetails.isEnabled()) {
				return R.ok("账号不可用");
			}
			// 账号密码匹配
			if (username.equals(userDetails.getUsername())
					&& passwordChecker.check(password, userDetails.getPassword(), userDetails.getSalt())) {
				StpOauth2UserUtil.login(userDetails.getUserId());
				RBACAuthorityHolder.setUserDetails(userDetails.getUserId(), userDetails);
				return R.ok();
			}
		}
		return R.ok("账号名或密码错误");
	}

	/**
	 * 注销登录
	 * @param loginId
	 */
	@Override
	public void logoutInternal(String loginId) {
		StpOauth2UserUtil.logout(loginId);
		// 删除缓存
		SpringUtils.publishEvent(new RoleAuthorityChangedEvent(Arrays.asList(loginId)));
	}

}
