package com.schilings.neiko.system.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.schilings.neiko.common.log.operation.annotation.CreateOperationLogging;
import com.schilings.neiko.common.log.operation.annotation.DeleteOperationLogging;
import com.schilings.neiko.common.log.operation.annotation.UpdateOperationLogging;
import com.schilings.neiko.common.model.result.BaseResultCode;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.extend.sa.token.oauth2.annotation.Oauth2CheckPermission;
import com.schilings.neiko.extend.sa.token.oauth2.annotation.Oauth2CheckScope;
import com.schilings.neiko.system.converter.SysOrganizationConverter;
import com.schilings.neiko.system.model.dto.SysOrganizationDTO;
import com.schilings.neiko.system.model.entity.SysOrganization;
import com.schilings.neiko.system.model.qo.SysOrganizationQO;
import com.schilings.neiko.system.model.vo.SysOrganizationTree;
import com.schilings.neiko.system.model.vo.SysOrganizationVO;
import com.schilings.neiko.system.service.SysOrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Oauth2CheckScope("system")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/organization")
@Tag(name = "组织架构管理")
public class SysOrganizationController {

	private final SysOrganizationService sysOrganizationService;

	/**
	 * 组织架构列表查询
	 * @return R 通用返回体
	 */
	@GetMapping("/list")
	@Oauth2CheckPermission("system:organization:read")
	@Operation(summary = "组织架构列表查询")
	public R<List<SysOrganizationVO>> listOrganization() {
		List<SysOrganization> list = sysOrganizationService.list();
		if (CollectionUtil.isEmpty(list)) {
			return R.ok(new ArrayList<>());
		}
		List<SysOrganizationVO> voList = list.stream().map(SysOrganizationConverter.INSTANCE::poToVo)
				.collect(Collectors.toList());
		return R.ok(voList);
	}

	/**
	 * 组织架构树查询
	 * @param qo 组织机构查询条件
	 * @return R 通用返回体
	 */
	@GetMapping("/tree")
	@Oauth2CheckPermission("system:organization:read")
	@Operation(summary = "组织架构树查询")
	public R<List<SysOrganizationTree>> getOrganizationTree(SysOrganizationQO qo) {
		return R.ok(sysOrganizationService.listTree(qo));
	}

	/**
	 * 新增组织架构
	 * @param sysOrganizationDTO 组织机构DTO
	 * @return R 通用返回体
	 */
	@CreateOperationLogging(msg = "新增组织架构")
	@PostMapping
	@Oauth2CheckPermission("system:organization:add")
	@Operation(summary = "新增组织架构")
	public R<Void> save(@RequestBody SysOrganizationDTO sysOrganizationDTO) {
		return sysOrganizationService.create(sysOrganizationDTO) ? R.ok()
				: R.fail(BaseResultCode.UPDATE_DATABASE_ERROR, "新增组织架构失败");
	}

	/**
	 * 修改组织架构
	 * @param sysOrganizationDTO 组织机构DTO
	 * @return R 通用返回体
	 */
	@UpdateOperationLogging(msg = "修改组织架构")
	@PutMapping
	@Oauth2CheckPermission("system:organization:edit")
	@Operation(summary = "修改组织架构")
	public R<Void> updateById(@RequestBody SysOrganizationDTO sysOrganizationDTO) {
		return sysOrganizationService.update(sysOrganizationDTO) ? R.ok()
				: R.fail(BaseResultCode.UPDATE_DATABASE_ERROR, "修改组织架构失败");
	}

	/**
	 * 通过id删除组织架构
	 * @param id id
	 * @return R 通用返回体
	 */
	@DeleteOperationLogging(msg = "通过id删除组织架构")
	@DeleteMapping("/{id}")
	@Oauth2CheckPermission("system:organization:del")
	@Operation(summary = "通过id删除组织架构")
	public R<Void> removeById(@PathVariable("id") Long id) {
		return sysOrganizationService.removeById(id) ? R.ok()
				: R.fail(BaseResultCode.UPDATE_DATABASE_ERROR, "通过id删除组织架构失败");
	}

	/**
	 * 校正组织机构层级和深度
	 * @return R 通用返回体
	 */
	@UpdateOperationLogging(msg = "校正组织机构层级和深度")
	@PatchMapping("/revised")
	@Oauth2CheckPermission("system:organization:revised")
	@Operation(summary = "校正组织机构层级和深度")
	public R<Void> revisedHierarchyAndPath() {
		return sysOrganizationService.revisedHierarchyAndPath() ? R.ok()
				: R.fail(BaseResultCode.UPDATE_DATABASE_ERROR, "校正组织机构层级和深度失败");
	}

}
