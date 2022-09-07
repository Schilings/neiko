package com.schilings.neiko.wechat.api;

import com.schilings.neiko.extend.sa.token.oauth2.annotation.OAuth2CheckScope;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@OAuth2CheckScope("system")
@RestController
@RequiredArgsConstructor
@RequestMapping("/wechat/user")
@Tag(name = "微信关联用户")
public class WechatUserController {

}
