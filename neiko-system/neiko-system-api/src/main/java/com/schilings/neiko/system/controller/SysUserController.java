package com.schilings.neiko.system.controller;


import com.schilings.neiko.common.log.operation.annotation.ReadOperationLogging;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.extend.sa.token.oauth2.annotation.Oauth2CheckScope;
import com.schilings.neiko.system.model.qo.SysUserQO;
import com.schilings.neiko.system.model.vo.SysUserPageVO;
import com.schilings.neiko.system.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@Oauth2CheckScope("system")
@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
@Tag(name = "用户管理模块")
public class SysUserController {

    private final SysUserService sysUserService;

    /**
     * 分页查询用户
     *
     * @param pageParam 参数集
     * @return 用户集合
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询系统用户")
    @ReadOperationLogging(msg = "分页查询系统用户")
    public R<PageResult<SysUserPageVO>> getUserPage(@Validated PageParam pageParam, SysUserQO sysUserQO) {
        return R.ok(sysUserService.queryPage(pageParam, sysUserQO));
    }
    
    
    

}
