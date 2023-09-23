package com.schilings.neiko.authorization.biz.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.schilings.neiko.authorization.converter.AuthorizationConverter;
import com.schilings.neiko.authorization.model.entity.Authorization;
import com.schilings.neiko.authorization.model.qo.AuthorizationQO;
import com.schilings.neiko.authorization.model.vo.AuthorizationPageVO;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.mapper.ExtendMapper;
import com.schilings.neiko.extend.mybatis.plus.wrapper.WrappersX;
import com.schilings.neiko.extend.mybatis.plus.wrapper.join.NeikoLambdaQueryWrapper;
import com.schilings.neiko.extend.mybatis.plus.wrapper.query.LambdaQueryWrapperX;

public interface AuthorizationMapper extends ExtendMapper<Authorization> {

	default PageResult<AuthorizationPageVO> queryPage(PageParam pageParam, AuthorizationQO qo) {
		IPage<Authorization> page = this.prodPage(pageParam);
		LambdaQueryWrapperX<Authorization> queryWrapper = WrappersX.lambdaQueryX(Authorization.class)
				.eqIfPresent(Authorization::getRegisteredClientId, qo.getRegisteredClientId())
				.likeIfPresent(Authorization::getPrincipalName, qo.getPrincipalName())
				.eqIfPresent(Authorization::getAuthorizationGrantType, qo.getAuthorizationGrantType())
				.eqIfPresent(Authorization::getState, qo.getState())
				.eqIfPresent(Authorization::getAuthorizationCodeValue, qo.getAuthorizationCodeValue())
				.eqIfPresent(Authorization::getAccessTokenValue, qo.getAccessTokenValue())
				.likeIfPresent(Authorization::getAccessTokenScopes, qo.getAccessTokenScopes())
				.eqIfPresent(Authorization::getRefreshTokenValue, qo.getRefreshTokenValue())
				.betweenIfPresent(Authorization::getCreateTime, qo.getStartTime(), qo.getEndTime());

		IPage<AuthorizationPageVO> iPage = this.selectPage(page, queryWrapper)
				.convert(AuthorizationConverter.INSTANCE::poToPageVo);
		return this.prodPage(iPage);
	}

}
