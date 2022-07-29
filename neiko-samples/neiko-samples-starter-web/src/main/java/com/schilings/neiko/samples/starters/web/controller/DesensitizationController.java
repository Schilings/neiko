package com.schilings.neiko.samples.starters.web.controller;


import com.schilings.neiko.samples.starters.web.dto.UserUpdateDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/desen")
public class DesensitizationController {

    public boolean check(String value) {
        return value.startsWith("admin");
    }

    @GetMapping("/test")
    public UserUpdateDTO test(String username,String phone) {
        return new UserUpdateDTO(username, phone, "1146830743@qq.com");
    }
}
