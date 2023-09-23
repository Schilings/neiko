package com.schilings.neiko.authorization.biz.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.schilings.neiko.authorization.converter.OAuth2RegisteredClientConverter;
import com.schilings.neiko.authorization.model.entity.OAuth2RegisteredClient;
import com.schilings.neiko.authorization.model.qo.OAuth2RegisteredClientQO;
import com.schilings.neiko.authorization.model.vo.OAuth2RegisteredClientInfo;
import com.schilings.neiko.authorization.model.vo.OAuth2RegisteredClientPageVO;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.mapper.ExtendMapper;
import com.schilings.neiko.extend.mybatis.plus.wrapper.WrappersX;
import com.schilings.neiko.extend.mybatis.plus.wrapper.query.LambdaQueryWrapperX;

import java.time.LocalDateTime;

public interface OAuth2RegisteredClientMapper extends ExtendMapper<OAuth2RegisteredClient> {

	default PageResult<OAuth2RegisteredClientPageVO> queryPage(PageParam pageParam, OAuth2RegisteredClientQO qo) {
		IPage<OAuth2RegisteredClient> page = this.prodPage(pageParam);
		LambdaQueryWrapperX<OAuth2RegisteredClient> queryWrapper = WrappersX.<OAuth2RegisteredClient>lambdaQueryX()
				.eqIfPresent(OAuth2RegisteredClient::getClientId, qo.getClientId())
				.likeIfPresent(OAuth2RegisteredClient::getClientName, qo.getClientName())
				.likeIfPresent(OAuth2RegisteredClient::getClientAuthenticationMethods,
						qo.getClientAuthenticationMethod())
				.likeIfPresent(OAuth2RegisteredClient::getAuthorizationGrantTypes, qo.getAuthorizationGrantType())
				.likeIfPresent(OAuth2RegisteredClient::getScopes, qo.getScope())
				.betweenIfPresent(OAuth2RegisteredClient::getCreateTime, qo.getStartTime(), qo.getEndTime());
		IPage<OAuth2RegisteredClientPageVO> iPage = this.selectPage(page, queryWrapper)
				.convert(OAuth2RegisteredClientConverter.INSTANCE::poToPageVo);
		return this.prodPage(iPage);
	}

	default OAuth2RegisteredClient queryOne(OAuth2RegisteredClientQO qo) {
		LambdaQueryWrapperX<OAuth2RegisteredClient> queryWrapper = WrappersX.<OAuth2RegisteredClient>lambdaQueryX()
				.eqIfPresent(OAuth2RegisteredClient::getClientId, qo.getClientId())
				.likeIfPresent(OAuth2RegisteredClient::getClientName, qo.getClientName())
				.likeIfPresent(OAuth2RegisteredClient::getClientAuthenticationMethods,
						qo.getClientAuthenticationMethod())
				.likeIfPresent(OAuth2RegisteredClient::getAuthorizationGrantTypes, qo.getAuthorizationGrantType())
				.likeIfPresent(OAuth2RegisteredClient::getScopes, qo.getScope());
		return this.selectOne(queryWrapper);
	}

}
