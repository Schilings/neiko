package com.schilings.neiko.authorization.controller;


import com.schilings.neiko.authorization.biz.service.AuthorizationConsentService;
import com.schilings.neiko.authorization.model.qo.AuthorizationConsentQO;
import com.schilings.neiko.authorization.model.vo.AuthorizationConsentPageVO;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.result.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/authorization/authorizationConsent")
@RequiredArgsConstructor
@Tag(name = "授权服务端授权用户同意信息管理模块")
public class AuthorizationConsentController {

    private final AuthorizationConsentService authorizationConsentService;

    /**
     * 分页查询
     * @param pageParam 参数集
     * @return 用户集合
     */
    @GetMapping("/authorizationConsentPage")
    @PreAuthorize("@per.hasPermission('authorization:authorizationConsent:read')")
    @Operation(summary = "分页查询授权用户同意信息")
    public R<PageResult<AuthorizationConsentPageVO>> getAuthorizationConsentPage(@Validated PageParam pageParam, AuthorizationConsentQO qo) {
        return R.ok(authorizationConsentService.queryPage(pageParam, qo));
    }

    
    
    
    
}
