package com.schilings.neiko.wechat.converter;

import com.schilings.neiko.wechat.model.dto.WechatMpReplyDTO;
import com.schilings.neiko.wechat.model.entity.WechatMpReply;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WechatMpReplyConverter {

	WechatMpReplyConverter INSTANCE = Mappers.getMapper(WechatMpReplyConverter.class);

	WechatMpReply dtoToPo(WechatMpReplyDTO wechatMpReplyDTO);

}
