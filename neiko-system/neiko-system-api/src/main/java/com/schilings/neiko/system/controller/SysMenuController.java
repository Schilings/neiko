package com.schilings.neiko.system.controller;


import com.schilings.neiko.system.service.SysMenuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/system/menu")
@Tag(name = "菜单权限管理")
public class SysMenuController {


    private final SysMenuService sysMenuService;
    
}
