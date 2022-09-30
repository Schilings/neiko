package com.schilings.neiko.auth.client;

import com.schilings.neiko.auth.model.entity.AuthClient;
import com.schilings.neiko.auth.service.AuthClientService;
import com.schilings.neiko.extend.sa.token.oauth2.component.ClientDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * <p>
 * 自定义获取登录客户端Service
 * </p>
 *
 * @author Schilings
 */
@Service
@RequiredArgsConstructor
public class ClientDetailsServiceImpl implements ClientDetailsService<AuthClientDetails> {

	private final AuthClientService authClientService;

	@Override
	public AuthClientDetails loadClientByClientId(String clientId) {
		return poToDetails(authClientService.getClientDetails(clientId));
	}

	public AuthClientDetails poToDetails(AuthClient authClient) {
		if (authClient == null) {
			return null;
		}
		AuthClientDetails authClientDetails = new AuthClientDetails();
		authClientDetails.setClientId(authClient.getClientId());
		authClientDetails.setClientSecret(authClient.getClientSecret());
		authClientDetails.setScope(authClient.getScope());
		authClientDetails.setAccessTokenTimeout(authClient.getAccessTokenTimeout());
		authClientDetails.setAuthorizedGrantTypes(authClient.getAuthorizedGrantTypes());
		authClientDetails.setUrls(authClient.getAllowUrl());
		authClientDetails.setRefreshTokenTimeout(authClient.getRefreshTokenTimeout());
		return authClientDetails;
	}

}
