package com.schilings.neiko.samples.admin.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class IndexController {

	/**
	 * 默认登录成功跳转页为 / 防止404状态
	 * @return the map
	 */
	@GetMapping("/")
	@ResponseBody
	public Map<String, Object> index(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient oAuth2AuthorizedClient) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> map = new HashMap<>(2);

		// OAuth2AuthorizedClient 为敏感信息不应该返回前端
		map.put("oAuth2AuthorizedClient", oAuth2AuthorizedClient);
		map.put("authentication", authentication);
		// todo 处理登录注册的逻辑
		// todo 根据 authentication 生成 token cookie之类的 也可以用 AuthenticationSuccessHandler
		// 配置来替代
		return map;
	}

	@GetMapping("/oauth2Login")
	public String oauth2Login() {
		return "oauth2Login";
	}

	@GetMapping("/authorizeLogin")
	public String authorizeLogin() {
		return "authorizeLogin";
	}

}
