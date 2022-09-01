package com.schilings.neiko.wechat.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WechatMpTemplateConverter {

    WechatMpTemplateConverter INSTANCE = Mappers.getMapper(WechatMpTemplateConverter.class);
}
