package com.schilings.neiko.wechat.biz.service.impl;


import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.wechat.biz.mapper.WechatMpMenuMapper;
import com.schilings.neiko.wechat.biz.service.WechatMpMenuService;
import com.schilings.neiko.wechat.model.dto.WechatMpMenuButton;
import org.springframework.stereotype.Service;

@Service
public class WechatMpMenuServiceImpl extends ExtendServiceImpl<WechatMpMenuMapper, WechatMpMenuButton>
        implements WechatMpMenuService {
}
