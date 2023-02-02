package com.schilings.neiko.sample.resource.server.web;


import com.schilings.neiko.authorization.model.dto.OAuth2RegisteredClientDTO;
import com.schilings.neiko.authorization.model.qo.OAuth2RegisteredClientQO;
import com.schilings.neiko.authorization.model.vo.OAuth2RegisteredClientInfo;
import com.schilings.neiko.authorization.model.vo.OAuth2RegisteredClientPageVO;
import com.schilings.neiko.authorization.remote.AuthorizationConsentRemote;
import com.schilings.neiko.authorization.remote.AuthorizationRemote;
import com.schilings.neiko.authorization.remote.OAuth2RegisteredClientRemote;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.sample.resource.server.http.token.TokenHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/http")
@RequiredArgsConstructor
public class TestHttpController {

    private final OAuth2RegisteredClientRemote registeredClientRemote;

    private final AuthorizationRemote authorizationRemote;

    private final AuthorizationConsentRemote authorizationConsentRemote;

    private final TokenHolder tokenHolder;
    
    @GetMapping("/test")
    public Object test() {

        R<PageResult<OAuth2RegisteredClientPageVO>> pageResultR = registeredClientRemote.getRegisteredClientPage(Collections.emptyMap());

        PageParam pageParam = new PageParam();
        OAuth2RegisteredClientQO registeredClientQO = new OAuth2RegisteredClientQO();
        registeredClientQO.setClientName("TEST");
        R<PageResult<OAuth2RegisteredClientPageVO>> pageResultR1 = registeredClientRemote
                .getRegisteredClientPage(pageParam,registeredClientQO);

        R<OAuth2RegisteredClientInfo> client = registeredClientRemote.getClientInfo(333333334444444L);


        Long id = System.nanoTime();
        OAuth2RegisteredClientDTO dto = new OAuth2RegisteredClientDTO();
        dto.setId(id);
        dto.setClientId("aaaaaaaaaaaa");
        dto.setClientSecret("aaaaaaaaaaaa");
        dto.setClientName("aaaaaaaaaaaa");
        dto.setClientAuthenticationMethods(Collections.singleton(ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue()));
        dto.setAuthorizationGrantTypes(Collections.singleton(AuthorizationGrantType.AUTHORIZATION_CODE.getValue()));

        R<Void> addResult = registeredClientRemote.addClient(dto);

        dto.setScopes(Collections.singleton("user_info"));
        R<Void> updateResult = registeredClientRemote.updateClient(dto);

        R<OAuth2RegisteredClientInfo> clientInfo = registeredClientRemote.getClientInfo(id);

        return clientInfo;
    }
  
}
