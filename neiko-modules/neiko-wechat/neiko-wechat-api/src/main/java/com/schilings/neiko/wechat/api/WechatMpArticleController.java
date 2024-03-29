package com.schilings.neiko.wechat.api;

import com.schilings.neiko.wechat.service.WechatMpArticleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wechat/article")
@Tag(name = "微信公众号文章")
public class WechatMpArticleController {

	private final WechatMpArticleService wechatMpArticleService;

}
