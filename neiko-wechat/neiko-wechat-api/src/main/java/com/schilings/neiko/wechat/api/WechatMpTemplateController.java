package com.schilings.neiko.wechat.api;

import com.schilings.neiko.extend.sa.token.oauth2.annotation.OAuth2CheckScope;
import com.schilings.neiko.wechat.service.WechatMpTemplateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@OAuth2CheckScope("system")
@RestController
@RequiredArgsConstructor
@RequestMapping("/wechat/template")
@Tag(name = "微信公众号模板消息")
public class WechatMpTemplateController {

    private final WechatMpTemplateService wechatMpTemplateService;
}
