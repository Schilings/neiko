package com.schilings.neiko.log.controller;


import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.extend.sa.token.oauth2.annotation.Oauth2CheckPermission;
import com.schilings.neiko.log.model.qo.LoginLogQO;
import com.schilings.neiko.log.model.vo.LoginLogPageVO;
import com.schilings.neiko.log.service.LoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登陆日志
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/log/login-log")
@Tag(name = "登陆日志管理")
public class LoginLogController {


    private final LoginLogService loginLogService;

    /**
     * 分页查询
     * @param pageParam 分页参数
     * @param loginLogQO 登陆日志查询对象
     * @return R 通用返回体
     */
    @GetMapping("/page")
    @Oauth2CheckPermission("log:login-log:read")
    @Operation(summary = "分页查询", description = "分页查询")
    public R<PageResult<LoginLogPageVO>> getLoginLogPage(@Validated PageParam pageParam, LoginLogQO loginLogQO) {
        return R.ok(loginLogService.queryPage(pageParam, loginLogQO));
    }
}
