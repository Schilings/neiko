package com.schilings.neiko.auth.controller;

import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.extend.sa.token.core.StpOauth2UserUtil;
import com.schilings.neiko.extend.sa.token.holder.RBACAuthorityHolder;
import com.schilings.neiko.extend.sa.token.oauth2.ExtendOauth2Handler;

import com.schilings.neiko.extend.sa.token.oauth2.Oauth2RequestBody;
import com.schilings.neiko.extend.sa.token.oauth2.component.LoginService;
import com.schilings.neiko.extend.sa.token.oauth2.pojo.UserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Tag(name = "Oauth2User认证模块")
public class Oauth2Controller {

	private final LoginService loginService;

	@PostMapping("/oauth2/*")
	@Operation(summary = "Oauth2认证登录", description = "Oauth2认证登录")
	public Object request(@RequestBody Oauth2RequestBody requestBody) {
		return ExtendOauth2Handler.serverRequest();
	}

	@GetMapping("/oauth2/logout")
	@Operation(summary = "Oauth2注销登录", description = "Oauth2注销登录")
	public Object logout() {
		String userId = (String) StpOauth2UserUtil.getLoginId();
		UserDetails userDetails = RBACAuthorityHolder.getUserDetails(userId);
		System.out.println(userDetails.getUsername());
		loginService.logout(userId);
		return R.ok();
	}

}
