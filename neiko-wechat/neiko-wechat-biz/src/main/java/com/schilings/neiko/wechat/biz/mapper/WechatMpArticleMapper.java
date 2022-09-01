package com.schilings.neiko.wechat.biz.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.schilings.neiko.common.model.constants.GlobalConstants;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.mapper.ExtendMapper;
import com.schilings.neiko.extend.mybatis.plus.wrapper.WrappersX;
import com.schilings.neiko.extend.mybatis.plus.wrapper.join.NeikoLambdaQueryWrapper;
import com.schilings.neiko.wechat.model.entity.WechatMpArticle;
import com.schilings.neiko.wechat.model.qo.WechatMpArticleQO;
import com.schilings.neiko.wechat.model.vo.WechatMpArticlePageVO;

public interface WechatMpArticleMapper extends ExtendMapper<WechatMpArticle> {

    /**
     * 分页查询
     * @param pageParam 分页参数
     * @param qo 查询参数
     * @return
     */
    default PageResult<WechatMpArticlePageVO> queryPage(PageParam pageParam, WechatMpArticleQO qo) {
        IPage<WechatMpArticlePageVO> page = this.prodPage(pageParam);
        NeikoLambdaQueryWrapper<Object> queryWrapper = WrappersX.lambdaQueryJoin()
                .selectAll(WechatMpArticle.class)
                .eq(WechatMpArticle::getDeleted, GlobalConstants.NOT_DELETED_FLAG)
                .likeIfPresent(WechatMpArticle::getAuthor, qo.getAuthor())
                .likeIfPresent(WechatMpArticle::getTitle, qo.getTitle())
                .eqIfPresent(WechatMpArticle::getStatus, qo.getStatus())
                .betweenIfPresent(WechatMpArticle::getCreateTime, qo.getStartTime(), qo.getEndTime());
        IPage<WechatMpArticlePageVO> voPage = this.selectJoinPage(page, WechatMpArticlePageVO.class, AUTO_RESULT_MAP, queryWrapper);
        return this.prodPage(voPage);
    }
}
