package com.schilings.neiko.system.remote;

import com.schilings.neiko.common.core.http.RequestParameterObject;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.system.model.dto.SysOrganizationDTO;
import com.schilings.neiko.system.model.qo.SysOrganizationQO;
import com.schilings.neiko.system.model.vo.SysOrganizationTree;
import com.schilings.neiko.system.model.vo.SysOrganizationVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;

import java.util.List;

@HttpExchange("/system/organization")
public interface SysOrganizationRemote {

	/**
	 * 组织架构列表查询
	 * @return R 通用返回体
	 */
	@GetExchange("/list")
	R<List<SysOrganizationVO>> listOrganization();

	/**
	 * 组织架构树查询
	 * @param qo 组织机构查询条件
	 * @return R 通用返回体
	 */
	@GetExchange("/tree")
	R<List<SysOrganizationTree>> getOrganizationTree(@RequestParameterObject SysOrganizationQO qo);

	/**
	 * 新增组织架构
	 * @param sysOrganizationDTO 组织机构DTO
	 * @return R 通用返回体
	 */
	@PostExchange
	R<Void> save(@RequestBody SysOrganizationDTO sysOrganizationDTO);

	/**
	 * 修改组织架构
	 * @param sysOrganizationDTO 组织机构DTO
	 * @return R 通用返回体
	 */
	@PutExchange
	R<Void> updateById(@RequestBody SysOrganizationDTO sysOrganizationDTO);

	/**
	 * 通过id删除组织架构
	 * @param id id
	 * @return R 通用返回体
	 */
	@DeleteExchange("/{id}")
	R<Void> removeById(@PathVariable("id") Long id);

	/**
	 * 校正组织机构层级和深度
	 * @return R 通用返回体
	 */
	@PatchExchange("/revised")
	R<Void> revisedHierarchyAndPath();

}
