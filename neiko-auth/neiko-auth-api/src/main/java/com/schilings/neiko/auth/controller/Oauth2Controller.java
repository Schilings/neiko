package com.schilings.neiko.auth.controller;

import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.extend.sa.token.core.StpOauth2UserUtil;
import com.schilings.neiko.extend.sa.token.oauth2.ExtendOauth2Handler;
import com.schilings.neiko.extend.sa.token.oauth2.component.LoginService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Tag(name = "Oauth2认证模块")
public class Oauth2Controller {


    private final LoginService loginService;

    // 处理所有OAuth相关请求 
    @PostMapping("/oauth2/*")
    public Object request() {
        return ExtendOauth2Handler.serverRequest();
    }

    @GetMapping("/oauth2/logout")
    public Object logout() {
        loginService.logout((String) StpOauth2UserUtil.getLoginId());
        return R.ok();
    }
    
}
