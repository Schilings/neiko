package com.schilings.neiko.system.controller;

import com.schilings.neiko.common.log.operation.annotation.CreateOperationLogging;
import com.schilings.neiko.common.log.operation.annotation.DeleteOperationLogging;
import com.schilings.neiko.common.log.operation.annotation.UpdateOperationLogging;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.extend.sa.token.oauth2.annotation.Oauth2CheckPermission;
import com.schilings.neiko.extend.sa.token.oauth2.annotation.Oauth2CheckScope;
import com.schilings.neiko.system.model.entity.SysConfig;
import com.schilings.neiko.system.model.qo.SysConfigQO;
import com.schilings.neiko.system.model.vo.SysConfigPageVO;
import com.schilings.neiko.system.service.SysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 *
 * <p>
 * 系统配置
 * </p>
 *
 * @author Schilings
 */
@Oauth2CheckScope("system")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/config")
@Tag(name = "系统配置")
public class SysConfigController {

	private final SysConfigService sysConfigService;

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param sysConfigQO 系统配置
	 * @return R<PageResult<SysConfigVO>>
	 */
	@GetMapping("/page")
	@Oauth2CheckPermission("system:config:read")
	@Operation(summary = "分页查询", description = "分页查询")
	public R<PageResult<SysConfigPageVO>> getSysConfigPage(@Validated PageParam pageParam, SysConfigQO sysConfigQO) {
		return R.ok(sysConfigService.queryPage(pageParam, sysConfigQO));
	}

	/**
	 * 新增系统配置
	 * @param sysConfig 系统配置
	 * @return R
	 */
	@CreateOperationLogging(msg = "新增系统配置")
	@PostMapping
	@Oauth2CheckPermission("system:config:add")
	@Operation(summary = "新增系统配置", description = "新增系统配置")
	public R<Boolean> save(@RequestBody SysConfig sysConfig) {
		return R.ok(sysConfigService.save(sysConfig));
	}

	/**
	 * 修改系统配置
	 * @param sysConfig 系统配置
	 * @return R
	 */
	@UpdateOperationLogging(msg = "修改系统配置")
	@PutMapping
	@Oauth2CheckPermission("system:config:edit")
	@Operation(summary = "修改系统配置")
	public R<Boolean> updateById(@RequestBody SysConfig sysConfig) {
		return R.ok(sysConfigService.updateByKey(sysConfig));
	}

	/**
	 * 删除系统配置
	 * @param confKey confKey
	 * @return R
	 */
	@DeleteOperationLogging(msg = "删除系统配置")
	@DeleteMapping
	@Oauth2CheckPermission("system:config:del")
	@Operation(summary = "删除系统配置")
	public R<Boolean> removeById(@RequestParam("confKey") String confKey) {
		return R.ok(sysConfigService.removeByKey(confKey));
	}

}
