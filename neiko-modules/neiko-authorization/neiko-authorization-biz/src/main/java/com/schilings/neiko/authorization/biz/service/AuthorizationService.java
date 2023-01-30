package com.schilings.neiko.authorization.biz.service;

import com.schilings.neiko.authorization.model.entity.Authorization;
import com.schilings.neiko.authorization.model.qo.AuthorizationQO;
import com.schilings.neiko.authorization.model.vo.AuthorizationPageVO;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.ExtendService;

public interface AuthorizationService extends ExtendService<Authorization> {
    PageResult<AuthorizationPageVO> queryPage(PageParam pageParam, AuthorizationQO qo);

    Authorization getByState(String state);

    Authorization getByAuthorizationCode(String authorizationCodeValue);

    Authorization getByAccessToken(String accessTokenValue);

    Authorization getByRefreshToken(String refreshTokenValue);

    Authorization getIfUnkonwTokenType(String state, String authorizationCodeValue,
                                       String accessTokenValue, String refreshTokenValue);
}
