package com.schilings.neiko.wechat.biz.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.mapper.ExtendMapper;
import com.schilings.neiko.extend.mybatis.plus.wrapper.WrappersX;
import com.schilings.neiko.extend.mybatis.plus.wrapper.join.NeikoLambdaQueryWrapper;
import com.schilings.neiko.wechat.model.entity.WechatMpReply;
import com.schilings.neiko.wechat.model.qo.WechatMpReplyQO;
import com.schilings.neiko.wechat.model.vo.WechatMpReplyPageVO;

public interface WechatMpReplyMapper extends ExtendMapper<WechatMpReply> {


    default PageResult<WechatMpReplyPageVO> queryPage(PageParam pageParam, WechatMpReplyQO qo) {
        IPage<WechatMpReplyPageVO> page = this.prodPage(pageParam);
        NeikoLambdaQueryWrapper<Object> queryWrapper = WrappersX.lambdaQueryJoin()
                .selectAll(WechatMpReply.class)
                .eqIfPresent(WechatMpReply::getType, qo.getType())
                .eqIfPresent(WechatMpReply::getRepType, qo.getRepType())
                .eqIfPresent(WechatMpReply::getReqType, qo.getReqKey())
                .likeIfPresent(WechatMpReply::getReqKey, qo.getReqKey());
        IPage<WechatMpReplyPageVO> voPage = this.selectJoinPage(page, WechatMpReplyPageVO.class, AUTO_RESULT_MAP, queryWrapper);
        return this.prodPage(voPage);

    }

    /**
     * 根据关键字查询
     * @param replyKy
     * @return
     */
    default WechatMpReply getByKey(String replyKy) {
        return this.selectOne(WrappersX.lambdaQueryX(WechatMpReply.class).eq(WechatMpReply::getReqKey, replyKy));
    }
    
    
    default boolean delete(Long id) {
        return SqlHelper.retBool(this.deleteById(id));
    }

    default WechatMpReply getByType(Integer type) {
        return this.selectOne(WrappersX.lambdaQueryX(WechatMpReply.class).eq(WechatMpReply::getType, type));
    }
}
