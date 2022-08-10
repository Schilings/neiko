package com.schilings.neiko.samples.shiro.controller;

import com.schilings.neiko.autoconfigure.shiro.token.JWTToken;
import com.schilings.neiko.samples.shiro.jwt.MyJWTRepository;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
    private MyJWTRepository myJWTRepository;
    
    @GetMapping("/login")
    public String login(String username) {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        String token = myJWTRepository.getToken(map);
        Subject subject = SecurityUtils.getSubject();
        subject.login(new JWTToken(token));
        return token;
    }
    
    @GetMapping("test")
    public String test1() {
        return "hh";
    }
    
}
