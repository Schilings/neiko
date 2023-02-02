package com.schilings.neiko.authorization.controller;

import com.schilings.neiko.authorization.biz.service.AuthorizationService;
import com.schilings.neiko.authorization.model.qo.AuthorizationQO;
import com.schilings.neiko.authorization.model.vo.AuthorizationPageVO;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.result.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/authorization/authorization")
@RequiredArgsConstructor
@Tag(name = "授权服务端授权信息管理模块")
public class AuthorizationController {

	private final AuthorizationService authorizationService;

	/**
	 * 分页查询
	 * @param pageParam 参数集
	 * @return 用户集合
	 */
	@GetMapping("/authorizationPage")
	@PreAuthorize("@per.hasPermission('authorization:authorization:read')")
	@Operation(summary = "分页查询授权信息")
	public R<PageResult<AuthorizationPageVO>> getAuthorizationConsentPage(@Validated PageParam pageParam,
			AuthorizationQO qo) {
		return R.ok(authorizationService.queryPage(pageParam, qo));
	}

}
