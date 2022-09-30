package com.schilings.neiko.wechat.service.impl;

import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.wechat.mapper.WechatMpArticleMapper;
import com.schilings.neiko.wechat.service.WechatMpArticleService;
import com.schilings.neiko.wechat.model.entity.WechatMpArticle;
import org.springframework.stereotype.Service;

@Service
public class WechatMpArticleServiceImpl extends ExtendServiceImpl<WechatMpArticleMapper, WechatMpArticle>
		implements WechatMpArticleService {

}
