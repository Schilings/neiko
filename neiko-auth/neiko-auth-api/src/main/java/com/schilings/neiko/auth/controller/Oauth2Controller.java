package com.schilings.neiko.auth.controller;

import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.extend.sa.token.core.StpOAuth2UserUtil;
import com.schilings.neiko.extend.sa.token.holder.RBACAuthorityHolder;
import com.schilings.neiko.extend.sa.token.oauth2.ExtendOAuth2Handler;

import com.schilings.neiko.extend.sa.token.oauth2.OAuth2RequestBody;
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
	public Object request(@RequestBody OAuth2RequestBody requestBody) {
		return ExtendOAuth2Handler.serverRequest();
	}

	@GetMapping("/oauth2/logout")
	@Operation(summary = "Oauth2注销登录", description = "Oauth2注销登录")
	public Object logout() {
		String userId = (String) StpOAuth2UserUtil.getLoginId();
		loginService.logout(userId);
		return R.ok();
	}

}
