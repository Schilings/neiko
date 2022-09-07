package com.schilings.neiko.samples.admin.controller;


import com.schilings.neiko.extend.sa.token.oauth2.annotation.OAuth2CheckPermission;
import com.schilings.neiko.extend.sa.token.oauth2.annotation.OAuth2CheckScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@OAuth2CheckScope({"system"})
@RestController
@RequestMapping("/demo")
public class DemoController {
    

    @GetMapping("/test1")
    public String test1() {
        return "test1";
    }

    @OAuth2CheckPermission("system:user:read")
    @GetMapping("/test2")
    public String test2() {
        return "test2";
    }
    @OAuth2CheckPermission("system:user:123")
    @GetMapping("/test3")
    public String test3() {
        return "test3";
    }
}
