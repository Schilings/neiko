package com.schilings.neiko.authorization.remote;

import com.schilings.neiko.authorization.model.qo.AuthorizationQO;
import com.schilings.neiko.authorization.model.vo.AuthorizationPageVO;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.core.http.RequestParameterObject;
import com.schilings.neiko.common.model.result.R;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Map;

@HttpExchange(url = "/authorization/authorization")
public interface AuthorizationRemote {

	/**
	 * 分页查询
	 */
	@GetExchange("/authorizationPage")
	R<PageResult<AuthorizationPageVO>> getAuthorizationConsentPage(@RequestParam(name = "map") Map<String, Object> map);

	/**
	 * 分页查询
	 */
	@GetExchange("/authorizationPage")
	R<PageResult<AuthorizationPageVO>> getAuthorizationConsentPage(PageParam pageParam,
			@RequestParameterObject AuthorizationQO qo);

}
