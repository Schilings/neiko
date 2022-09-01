package com.schilings.neiko.wechat.biz.service.impl;


import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.wechat.biz.mapper.WechatMpArticleMapper;
import com.schilings.neiko.wechat.biz.service.WechatMpArticleService;
import com.schilings.neiko.wechat.model.entity.WechatMpArticle;
import org.springframework.stereotype.Service;

@Service
public class WechatMpArticleServiceImpl extends ExtendServiceImpl<WechatMpArticleMapper, WechatMpArticle>
        implements WechatMpArticleService {
}
