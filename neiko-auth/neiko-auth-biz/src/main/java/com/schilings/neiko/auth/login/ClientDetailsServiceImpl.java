package com.schilings.neiko.auth.login;


import com.schilings.neiko.auth.model.dto.AuthClientDetails;
import com.schilings.neiko.auth.service.AuthClientService;
import com.schilings.neiko.common.security.component.ClientDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * 
 * <p>自定义获取登录客户端Service</p>
 * 
 * @author Schilings
*/
@Component
@RequiredArgsConstructor
public class ClientDetailsServiceImpl implements ClientDetailsService<AuthClientDetails> {

    private final AuthClientService authClientService;
    
    @Override
    public AuthClientDetails loadClientByClientId(String clientId) {
        return authClientService.getClientDetails(clientId);
    }
}
