package com.schilings.neiko.authorization.remote;

import com.schilings.neiko.authorization.model.qo.AuthorizationConsentQO;
import com.schilings.neiko.authorization.model.vo.AuthorizationConsentPageVO;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.core.http.RequestParameterObject;
import com.schilings.neiko.common.model.result.R;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Map;

@HttpExchange("/authorization/authorizationConsent")
public interface AuthorizationConsentRemote {

	/**
	 * 分页查询
	 */
	@GetExchange("/authorizationConsentPage")
	R<PageResult<AuthorizationConsentPageVO>> getAuthorizationConsentPage(
			@RequestParam(name = "map") Map<String, Object> map);

	/**
	 * 分页查询
	 */
	@GetExchange("/authorizationConsentPage")
	R<PageResult<AuthorizationConsentPageVO>> getAuthorizationConsentPage(PageParam pageParam,
			@RequestParameterObject AuthorizationConsentQO qo);

}
