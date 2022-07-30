package com.schilings.neiko.samples.starters.web.controller;

import com.schilings.neiko.autoconfigure.web.api.annotation.ApiVersion;
import com.schilings.neiko.samples.starters.web.dto.UserUpdateDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/version")
public class VersionController {

	// @GetMapping("/test")
	@ApiVersion("1.13.47")
	public UserUpdateDTO test0() {
		return new UserUpdateDTO("!!admin", "13333333333", "1146830743@qq.com");
	}

	// @GetMapping("/test")
	@ApiVersion("2.12.45")
	public UserUpdateDTO test1() {
		return new UserUpdateDTO("admin", "13333333333", "1146830743@qq.com");
	}

	// @GetMapping("/test")
	@ApiVersion("1.12.46")
	public UserUpdateDTO test2() {
		return new UserUpdateDTO("!admin", "13333333333", "1146830743@qq.com");
	}

}
