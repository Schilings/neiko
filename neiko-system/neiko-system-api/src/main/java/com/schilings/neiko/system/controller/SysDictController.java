package com.schilings.neiko.system.controller;

import com.schilings.neiko.extend.sa.token.oauth2.annotation.Oauth2CheckScope;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Oauth2CheckScope("system")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/dict")
@Tag(name = "字典表管理")
public class SysDictController {

}
