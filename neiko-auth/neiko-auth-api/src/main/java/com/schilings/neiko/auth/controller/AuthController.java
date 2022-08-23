package com.schilings.neiko.auth.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Util;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.schilings.neiko.common.security.SoMap;
import com.schilings.neiko.extend.sa.token.oauth2.annotation.Oauth2CheckLogin;
import com.schilings.neiko.extend.sa.token.oauth2.annotation.Oauth2CheckRole;
import com.schilings.neiko.extend.sa.token.oauth2.annotation.Oauth2CheckScope;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "用户认证模块")
public class AuthController {
    

    @GetMapping("/userinfo")
    @Oauth2CheckScope({"userinfo"})
    public SaResult userinfo() {
        // 模拟账号信息 （真实环境需要查询数据库获取信息）
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("nickname", "shengzhang_");
        map.put("avatar", "http://xxx.com/1.jpg");
        map.put("age", "18");
        map.put("sex", "男");
        map.put("address", "山东省 青岛市 城阳区");
        return SaResult.data(map);
    }


    @GetMapping("/test")
    @Oauth2CheckLogin
    public SaResult test() {
        return SaResult.ok();
    }

    @GetMapping("/testRole1")
    @Oauth2CheckRole(value = {"ROLE_DEMO1"})
    public SaResult testRole1() {
        return SaResult.ok();
    }


    @GetMapping("/testRole2")
    @Oauth2CheckRole(value = {"ROLE_DEMO3"})
    public SaResult testRole2() {
        return SaResult.ok();
    }



    // 全局异常拦截  
    @ExceptionHandler
    public SaResult handlerException(Exception e) {
        e.printStackTrace();
        return SaResult.error(e.getMessage());
    }
    
}
