package com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.client;

import cn.hutool.core.util.BooleanUtil;
import com.schilings.neiko.security.oauth2.authorization.server.customizer.token.federated.OAuth2FederatedIdentityConstant;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * <p>
 * 为了让我们需要的信息能传递到AuthenticationSuccessHandler
 * </p>
 * <p>
 * 让OAuth2LoginAuthenticationFilter避免直接remove掉，修改remove逻辑
 * </p>
 *
 * @author Schilings
 */
public class OAuth2FederatedIdentityOAuth2AuthorizationRequestRepository
		implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

	private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository;

	public OAuth2FederatedIdentityOAuth2AuthorizationRequestRepository() {
		this.authorizationRequestRepository = new HttpSessionOAuth2AuthorizationRequestRepository();
	}

	public OAuth2AuthorizationRequest removeAuthorizationRequestInTheEnd(HttpServletRequest request,
			HttpServletResponse response) {
		return this.authorizationRequestRepository.removeAuthorizationRequest(request, response);
	}

	/**
	 * 为了让我们需要的信息能传递到AuthenticationSuccessHandler
	 * 让OAuth2LoginAuthenticationFilter避免直接remove掉，修改remove逻辑
	 * @param request
	 * @return
	 */
	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
		OAuth2AuthorizationRequest authorizationRequest = this.loadAuthorizationRequest(request);
		// 如果存放了我们需要的信息则先不删除
		if (isFederatedIdentityAuthorizationRequest(request, authorizationRequest)) {
			return authorizationRequest;
		}
		else {
			return this.authorizationRequestRepository.removeAuthorizationRequest(request);
		}
	}

	private boolean isFederatedIdentityAuthorizationRequest(HttpServletRequest request,
			OAuth2AuthorizationRequest authorizationRequest) {
		if (authorizationRequest == null) {
			return false;
		}
		// 如果是Federated Request Authorization Request
		String isFederatedIdentity = authorizationRequest
				.getAttribute(OAuth2FederatedIdentityConstant.FEDERATED_IDENTITY_REQUEST);
		if (StringUtils.hasText(isFederatedIdentity) && BooleanUtil.toBoolean(isFederatedIdentity)) {
			return true;
		}
		return false;

	}

	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		return this.authorizationRequestRepository.loadAuthorizationRequest(request);
	}

	@Override
	public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
			HttpServletResponse response) {
		this.authorizationRequestRepository.saveAuthorizationRequest(authorizationRequest, request, response);
	}

}
