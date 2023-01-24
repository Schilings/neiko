package com.schilings.neiko.wechat.api;

import com.schilings.neiko.wechat.service.WechatMpTemplateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wechat/template")
@Tag(name = "微信公众号模板消息")
public class WechatMpTemplateController {

	private final WechatMpTemplateService wechatMpTemplateService;

}
