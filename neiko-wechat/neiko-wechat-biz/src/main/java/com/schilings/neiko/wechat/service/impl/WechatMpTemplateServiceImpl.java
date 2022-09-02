package com.schilings.neiko.wechat.service.impl;


import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.wechat.mapper.WechatMpTemplateMapper;
import com.schilings.neiko.wechat.service.WechatMpTemplateService;
import com.schilings.neiko.wechat.model.entity.WechatMpTemplate;
import org.springframework.stereotype.Service;

@Service
public class WechatMpTemplateServiceImpl extends ExtendServiceImpl<WechatMpTemplateMapper, WechatMpTemplate>
        implements WechatMpTemplateService {
}
