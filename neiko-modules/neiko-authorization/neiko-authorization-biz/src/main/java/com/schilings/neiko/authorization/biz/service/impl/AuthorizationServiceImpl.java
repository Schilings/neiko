package com.schilings.neiko.authorization.biz.service.impl;


import com.schilings.neiko.authorization.biz.mapper.AuthorizationMapper;
import com.schilings.neiko.authorization.biz.service.AuthorizationService;
import com.schilings.neiko.authorization.model.entity.Authorization;
import com.schilings.neiko.authorization.model.qo.AuthorizationQO;
import com.schilings.neiko.authorization.model.vo.AuthorizationPageVO;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.extend.mybatis.plus.wrapper.WrappersX;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorizationServiceImpl extends ExtendServiceImpl<AuthorizationMapper, Authorization> 
        implements AuthorizationService {

    @Override
    public PageResult<AuthorizationPageVO> queryPage(PageParam pageParam, AuthorizationQO qo) {
        return baseMapper.queryPage(pageParam, qo);
    }
    
    @Override
    public Authorization getByState(String state) {
        List<Authorization> authorizations = baseMapper.selectList(WrappersX.<Authorization>lambdaQueryX()
                .eq(Authorization::getState, state));
        return !authorizations.isEmpty() ? authorizations.get(0) : null;
    }

    @Override
    public Authorization getByAuthorizationCode(String authorizationCodeValue) {
        List<Authorization> authorizations = baseMapper.selectList(WrappersX.<Authorization>lambdaQueryX()
                .eq(Authorization::getAuthorizationCodeValue, authorizationCodeValue));
        return !authorizations.isEmpty() ? authorizations.get(0) : null;
    }

    @Override
    public Authorization getByAccessToken(String accessTokenValue) {
        List<Authorization> authorizations = baseMapper.selectList(WrappersX.<Authorization>lambdaQueryX()
                .eq(Authorization::getAccessTokenValue, accessTokenValue));
        return !authorizations.isEmpty() ? authorizations.get(0) : null;
    }

    @Override
    public Authorization getByRefreshToken(String refreshTokenValue) {
        List<Authorization> authorizations = baseMapper.selectList(WrappersX.<Authorization>lambdaQueryX()
                .eq(Authorization::getRefreshTokenValue, refreshTokenValue));
        return !authorizations.isEmpty() ? authorizations.get(0) : null;
    }

    @Override
    public Authorization getIfUnkonwTokenType(String state, String authorizationCodeValue,
                                               String accessTokenValue,String refreshTokenValue) {
        List<Authorization> authorizations = baseMapper.selectList(WrappersX.<Authorization>lambdaQueryX()
                .eq(Authorization::getState, state).or()
                .eq(Authorization::getAuthorizationCodeValue, authorizationCodeValue).or()
                .eq(Authorization::getAccessTokenValue, accessTokenValue).or()
                .eq(Authorization::getRefreshTokenValue, refreshTokenValue)
        );
        return !authorizations.isEmpty() ? authorizations.get(0) : null;
    }
    
}
