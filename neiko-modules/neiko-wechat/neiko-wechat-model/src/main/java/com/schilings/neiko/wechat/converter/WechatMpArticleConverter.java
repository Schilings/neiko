package com.schilings.neiko.wechat.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WechatMpArticleConverter {

	WechatMpArticleConverter INSTANCE = Mappers.getMapper(WechatMpArticleConverter.class);

}
