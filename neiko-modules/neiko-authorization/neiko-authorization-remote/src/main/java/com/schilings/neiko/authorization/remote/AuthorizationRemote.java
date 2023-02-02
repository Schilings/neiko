package com.schilings.neiko.authorization.remote;


import com.schilings.neiko.authorization.model.qo.AuthorizationQO;
import com.schilings.neiko.authorization.model.vo.AuthorizationPageVO;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.result.R;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url = "/authorization/authorization")
public interface AuthorizationRemote {


	/**
	 * 分页查询
	 */
	@GetExchange("/authorizationPage")
	R<PageResult<AuthorizationPageVO>> getAuthorizationConsentPage(PageParam pageParam, AuthorizationQO qo);

}
