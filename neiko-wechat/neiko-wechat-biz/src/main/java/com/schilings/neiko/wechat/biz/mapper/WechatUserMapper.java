package com.schilings.neiko.wechat.biz.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.schilings.neiko.common.model.constants.GlobalConstants;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.mapper.ExtendMapper;
import com.schilings.neiko.extend.mybatis.plus.wrapper.WrappersX;
import com.schilings.neiko.extend.mybatis.plus.wrapper.join.NeikoLambdaQueryWrapper;
import com.schilings.neiko.wechat.model.entity.WechatUser;
import com.schilings.neiko.wechat.model.qo.WechatUserQO;
import com.schilings.neiko.wechat.model.vo.WechatUserPageVO;

public interface WechatUserMapper extends ExtendMapper<WechatUser> {

    default PageResult<WechatUserPageVO> queryPage(PageParam pageParam, WechatUserQO qo) {
        IPage<WechatUserPageVO> page = this.prodPage(pageParam);
        NeikoLambdaQueryWrapper<Object> queryWrapper = WrappersX.lambdaQueryJoin()
                .selectAll(WechatUser.class)
                .eq(WechatUser::getDeleted, GlobalConstants.NOT_DELETED_FLAG)
                .likeIfPresent(WechatUser::getNickName, qo.getNickName())
                .eqIfPresent(WechatUser::getAppType, qo.getAppType())
                .eqIfPresent(WechatUser::getPhone, qo.getPhone())
                .eqIfPresent(WechatUser::getSubscribe, qo.getSubscribe())
                .eqIfPresent(WechatUser::getSubscribeScene, qo.getSubscribeScene())
                .eqIfPresent(WechatUser::getSex, qo.getSex());
        IPage<WechatUserPageVO> voPage = this.selectJoinPage(page, WechatUserPageVO.class, AUTO_RESULT_MAP, queryWrapper);
        return this.prodPage(voPage);
    }

    default WechatUser getById(Long id) {
        return this.selectById(id);
    }

    default WechatUser getByOpenId(String openId) {
        return this.selectOne(WrappersX.lambdaQueryX(WechatUser.class).eq(WechatUser::getOpenId, openId));
    }
    
    
}
