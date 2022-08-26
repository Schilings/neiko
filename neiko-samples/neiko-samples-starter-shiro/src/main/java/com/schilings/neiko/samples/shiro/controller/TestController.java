package com.schilings.neiko.samples.shiro.controller;

import com.schilings.neiko.autoconfigure.shiro.token.JWTRepository;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * <p></p>
 * </pre>
 *
 * @author <a href="https://github.com/kil1ua">Ken-Chy129</a>
 * @date 2022/8/10 14:25
 */
@RestController
@RequestMapping("shiro")
public class TestController {

	@Autowired
	private JWTRepository jWTRepository;

	@GetMapping("/login")
	public String login(String username) {
		Map<String, String> map = new HashMap<>();
		map.put("username", username);
		return jWTRepository.getToken(map);
	}

	@GetMapping("test")
	public String test1() {
		return "hh";
	}

	@RequiresRoles("admin")
	@GetMapping("test2")
	public String test2() {
		return "xx";
	}

	@RequiresRoles("ll")
	@GetMapping("test3")
	public String test3() {
		return "xx";
	}

}
