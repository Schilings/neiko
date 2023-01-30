package com.schilings.neiko.authorization.biz.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.schilings.neiko.authorization.converter.AuthorizationConsentConverter;
import com.schilings.neiko.authorization.model.entity.AuthorizationConsent;
import com.schilings.neiko.authorization.model.entity.OAuth2RegisteredClient;
import com.schilings.neiko.authorization.model.qo.AuthorizationConsentQO;
import com.schilings.neiko.authorization.model.vo.AuthorizationConsentPageVO;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.mapper.ExtendMapper;
import com.schilings.neiko.extend.mybatis.plus.wrapper.WrappersX;
import com.schilings.neiko.extend.mybatis.plus.wrapper.join.NeikoLambdaQueryWrapper;
import com.schilings.neiko.extend.mybatis.plus.wrapper.query.LambdaQueryWrapperX;

public interface AuthorizationConsentMapper extends ExtendMapper<AuthorizationConsent> {


    default PageResult<AuthorizationConsentPageVO> queryPage(PageParam pageParam, AuthorizationConsentQO qo) {
        IPage<AuthorizationConsent> page = this.prodPage(pageParam);
        LambdaQueryWrapperX<AuthorizationConsent> queryWrapper = WrappersX.lambdaQueryX(AuthorizationConsent.class)
                .eqIfPresent(AuthorizationConsent::getRegisteredClientId, qo.getRegisteredClientId())
                .likeIfPresent(AuthorizationConsent::getPrincipalName, qo.getPrincipalName())
                .betweenIfPresent(AuthorizationConsent::getCreateTime, qo.getStartTime(), qo.getEndTime());
        IPage<AuthorizationConsentPageVO> iPage = this.selectPage(page, queryWrapper).convert(AuthorizationConsentConverter.INSTANCE::poToPageVo);
        return this.prodPage(iPage);
    }
}
