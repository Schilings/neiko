package com.schilings.neiko.authorization.biz.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.schilings.neiko.authorization.biz.mapper.AuthorizationConsentMapper;
import com.schilings.neiko.authorization.biz.service.AuthorizationConsentService;
import com.schilings.neiko.authorization.converter.AuthorizationConsentConverter;
import com.schilings.neiko.authorization.model.dto.AuthorizationConsentDTO;
import com.schilings.neiko.authorization.model.entity.AuthorizationConsent;
import com.schilings.neiko.authorization.model.qo.AuthorizationConsentQO;
import com.schilings.neiko.authorization.model.vo.AuthorizationConsentPageVO;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.extend.mybatis.plus.wrapper.WrappersX;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorizationConsentServiceImpl extends ExtendServiceImpl<AuthorizationConsentMapper, AuthorizationConsent>
		implements AuthorizationConsentService {

	@Override
	public PageResult<AuthorizationConsentPageVO> queryPage(PageParam pageParam, AuthorizationConsentQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

	@Override
	public boolean saveOrUpdateAuthorizationConsent(AuthorizationConsentDTO dto) {
		AuthorizationConsent po = AuthorizationConsentConverter.INSTANCE.dtoToPo(dto);
		AuthorizationConsent exist = getByRegisteredClientIdAndPrincipalName(dto.getRegisteredClientId(),
				dto.getPrincipalName());
		if (exist != null) {
			po.setId(exist.getId());
			return updateById(po);
		}
		else {
			return save(po);
		}
	}

	@Override
	public boolean deleteByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName) {
		int delete = baseMapper.delete(WrappersX.<AuthorizationConsent>lambdaQueryX()
				.eq(AuthorizationConsent::getRegisteredClientId, registeredClientId)
				.eq(AuthorizationConsent::getPrincipalName, principalName));
		return SqlHelper.retBool(delete);
	}

	@Override
	public AuthorizationConsent getByRegisteredClientIdAndPrincipalName(String registeredClientId,
			String principalName) {
		List<AuthorizationConsent> result = baseMapper.selectList(WrappersX.<AuthorizationConsent>lambdaQueryX()
				.eq(AuthorizationConsent::getRegisteredClientId, registeredClientId)
				.eq(AuthorizationConsent::getPrincipalName, principalName));
		return !result.isEmpty() ? result.get(0) : null;
	}

}
