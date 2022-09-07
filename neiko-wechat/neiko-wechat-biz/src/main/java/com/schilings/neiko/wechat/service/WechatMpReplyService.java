package com.schilings.neiko.wechat.service;

import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.ExtendService;
import com.schilings.neiko.wechat.model.dto.WechatMpReplyDTO;
import com.schilings.neiko.wechat.model.entity.WechatMpReply;
import com.schilings.neiko.wechat.model.qo.WechatMpReplyQO;
import com.schilings.neiko.wechat.model.vo.WechatMpReplyPageVO;

public interface WechatMpReplyService extends ExtendService<WechatMpReply> {

	PageResult<WechatMpReplyPageVO> queryPage(PageParam pageParam, WechatMpReplyQO qo);

	/**
	 * 根据关键字查询
	 * @param key
	 * @return
	 */
	WechatMpReply getByKey(String key);

	WechatMpReply getByType(Integer type);

	boolean addMpReply(WechatMpReplyDTO wechatMpReplyDTO);

}
