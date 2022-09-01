package com.schilings.neiko.wechat.biz.service.impl;


import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.schilings.neiko.common.core.exception.ServiceException;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.result.BaseResultCode;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.wechat.biz.mapper.WechatMpReplyMapper;
import com.schilings.neiko.wechat.biz.service.WechatMpReplyService;
import com.schilings.neiko.wechat.converter.WechatMpReplyConverter;
import com.schilings.neiko.wechat.model.dto.WechatMpReplyDTO;
import com.schilings.neiko.wechat.model.entity.WechatMpReply;
import com.schilings.neiko.wechat.model.qo.WechatMpReplyQO;
import com.schilings.neiko.wechat.model.vo.WechatMpReplyPageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WechatMpReplyServiceImpl extends ExtendServiceImpl<WechatMpReplyMapper, WechatMpReply>
        implements WechatMpReplyService {

    @Override
    public PageResult<WechatMpReplyPageVO> queryPage(PageParam pageParam, WechatMpReplyQO qo) {
        return baseMapper.queryPage(pageParam, qo);
    }

    /**
     * 根据关键字查询
     * @param key
     * @return
     */
    public WechatMpReply getByKey(String key) {
        return baseMapper.getByKey(key);
    }

    @Override
    public WechatMpReply getByType(Integer type) {
        return baseMapper.getByType(type);
    }

    @Override
    public boolean addMpReply(WechatMpReplyDTO wechatMpReplyDTO) {
        WechatMpReply wechatMpReply = WechatMpReplyConverter.INSTANCE.dtoToPo(wechatMpReplyDTO);
        // 保存用户
        boolean insertSuccess = SqlHelper.retBool(baseMapper.insert(wechatMpReply));
        Assert.isTrue(insertSuccess, () -> {
            log.error("[addMpReply] 数据插入微信公众号回复表失败，wechatMpReplyDTO：{}", wechatMpReplyDTO);
            return new ServiceException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "数据插入微信公众号回复表失败");
        });
        return insertSuccess;
    }
}
