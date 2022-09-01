package com.schilings.neiko.wechat.biz.service.impl;


import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.wechat.biz.mapper.WechatMpTemplateMapper;
import com.schilings.neiko.wechat.biz.service.WechatMpTemplateService;
import com.schilings.neiko.wechat.model.entity.WechatMpTemplate;
import org.springframework.stereotype.Service;

@Service
public class WechatMpTemplateServiceImpl extends ExtendServiceImpl<WechatMpTemplateMapper, WechatMpTemplate>
        implements WechatMpTemplateService {
}
