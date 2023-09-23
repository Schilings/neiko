package com.schilings.neiko.authorization.biz.service;

import com.schilings.neiko.authorization.model.dto.AuthorizationConsentDTO;
import com.schilings.neiko.authorization.model.entity.AuthorizationConsent;
import com.schilings.neiko.authorization.model.qo.AuthorizationConsentQO;
import com.schilings.neiko.authorization.model.vo.AuthorizationConsentPageVO;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.ExtendService;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public interface AuthorizationConsentService extends ExtendService<AuthorizationConsent> {

	PageResult<AuthorizationConsentPageVO> queryPage(PageParam pageParam, AuthorizationConsentQO qo);

	boolean saveOrUpdateAuthorizationConsent(AuthorizationConsentDTO dto);

	boolean deleteByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);

	AuthorizationConsent getByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);

}
