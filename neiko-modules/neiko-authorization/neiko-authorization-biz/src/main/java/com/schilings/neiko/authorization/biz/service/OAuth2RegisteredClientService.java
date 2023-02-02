package com.schilings.neiko.authorization.biz.service;

import com.schilings.neiko.authorization.model.dto.OAuth2ClientSettingsDTO;
import com.schilings.neiko.authorization.model.dto.OAuth2RegisteredClientDTO;
import com.schilings.neiko.authorization.model.dto.OAuth2TokenSettingsDTO;
import com.schilings.neiko.authorization.model.entity.OAuth2RegisteredClient;
import com.schilings.neiko.authorization.model.qo.OAuth2RegisteredClientQO;
import com.schilings.neiko.authorization.model.vo.OAuth2RegisteredClientInfo;
import com.schilings.neiko.authorization.model.vo.OAuth2RegisteredClientPageVO;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.ExtendService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;

public interface OAuth2RegisteredClientService extends ExtendService<OAuth2RegisteredClient> {

	PageResult<OAuth2RegisteredClientPageVO> queryPage(PageParam pageParam, OAuth2RegisteredClientQO qo);

	OAuth2RegisteredClient getByClientId(String clienId);

	OAuth2RegisteredClientInfo getClientInfoById(Long id);

	OAuth2RegisteredClientInfo getClientInfoByClientId(String clientId);

	OAuth2RegisteredClientInfo getClientInfo(OAuth2RegisteredClientQO qo);

	boolean saveRegisteredClient(OAuth2RegisteredClientDTO dto);

	boolean updateRegisteredClient(OAuth2RegisteredClientDTO dto);

}
