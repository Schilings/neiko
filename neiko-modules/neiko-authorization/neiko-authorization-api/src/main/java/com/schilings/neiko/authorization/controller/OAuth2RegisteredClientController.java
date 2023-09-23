package com.schilings.neiko.authorization.controller;

import com.schilings.neiko.authorization.biz.service.OAuth2RegisteredClientService;
import com.schilings.neiko.authorization.model.dto.OAuth2RegisteredClientDTO;
import com.schilings.neiko.authorization.model.entity.OAuth2RegisteredClient;
import com.schilings.neiko.authorization.model.qo.OAuth2RegisteredClientQO;
import com.schilings.neiko.authorization.model.vo.OAuth2RegisteredClientInfo;
import com.schilings.neiko.authorization.model.vo.OAuth2RegisteredClientPageVO;
import com.schilings.neiko.common.core.validation.group.CreateGroup;
import com.schilings.neiko.common.core.validation.group.UpdateGroup;
import com.schilings.neiko.common.log.operation.annotation.CreateOperationLogging;
import com.schilings.neiko.common.log.operation.annotation.DeleteOperationLogging;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.result.BaseResultCode;
import com.schilings.neiko.common.model.result.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/authorization/registeredClient")
@RequiredArgsConstructor
@Tag(name = "授权服务端客户端基本信息管理模块")
public class OAuth2RegisteredClientController {

	private final OAuth2RegisteredClientService registeredClientService;

	/**
	 * 分页查询
	 * @param pageParam 参数集
	 * @return 用户集合
	 */
	@GetMapping("/registeredClientPage")
	@PreAuthorize("@per.hasPermission('authorization:registeredClient:read')")
	@Operation(summary = "分页查询客户端")
	public R<PageResult<OAuth2RegisteredClientPageVO>> getRegisteredClientPage(@Validated PageParam pageParam,
			OAuth2RegisteredClientQO qo) {
		return R.ok(registeredClientService.queryPage(pageParam, qo));
	}

	/**
	 * 获取指定的客户端基本信息
	 */
	@GetMapping("/{id}")
	@Operation(summary = "获取指定的客户端基本信息")
	@PreAuthorize("@per.hasPermission('authorization:registeredClient:read')")
	public R<OAuth2RegisteredClientInfo> getClientInfo(@PathVariable("id") Long id) {
		OAuth2RegisteredClientInfo info = registeredClientService.getClientInfoById(id);
		if (info == null) {
			return R.ok();
		}
		return R.ok(info);
	}

	/**
	 * 新增客户端基本信息
	 * @return success/false
	 */
	@PostMapping
	@CreateOperationLogging(msg = "新增客户端基本信息")
	@PreAuthorize("@per.hasPermission('authorization:registeredClient:add')")
	@Operation(summary = "新增客户端基本信息", description = "新增客户端基本信息")
	public R<Void> addClient(
			@Validated({ Default.class, CreateGroup.class }) @RequestBody OAuth2RegisteredClientDTO dto) {
		OAuth2RegisteredClient client = registeredClientService.getByClientId(dto.getClientId());
		if (client != null) {
			return R.fail(BaseResultCode.LOGIC_CHECK_ERROR, "clientId已存在");
		}
		return registeredClientService.saveRegisteredClient(dto) ? R.ok()
				: R.fail(BaseResultCode.UPDATE_DATABASE_ERROR, "新增客户端基本信息失败");

	}

	/**
	 * 新增客户端基本信息
	 * @return success/false
	 */
	@PutMapping
	@CreateOperationLogging(msg = "更新客户端基本信息")
	@PreAuthorize("@per.hasPermission('authorization:registeredClient:edit')")
	@Operation(summary = "更新客户端基本信息", description = "更新客户端基本信息")
	public R<Void> updateClient(
			@Validated({ Default.class, UpdateGroup.class }) @RequestBody OAuth2RegisteredClientDTO dto) {
		OAuth2RegisteredClient client = registeredClientService.getByClientId(dto.getClientId());
		// clientId已存在
		if (client != null && !client.getClientId().equals(dto.getClientId())) {
			return R.fail(BaseResultCode.LOGIC_CHECK_ERROR, "clientId已存在");
		}
		return registeredClientService.updateRegisteredClient(dto) ? R.ok()
				: R.fail(BaseResultCode.UPDATE_DATABASE_ERROR, "更新客户端失败");

	}

	/**
	 * 删除指定的客户端基本信息
	 */
	@DeleteMapping("/{id}")
	@DeleteOperationLogging(msg = "通过id删除指定的客户端基本信息")
	@PreAuthorize("@per.hasPermission('authorization:registeredClient:del')")
	@Operation(summary = "通过id删除指定的客户端基本信息户", description = "通过id删除指定的客户端基本信息")
	public R<Void> deleteClientInfo(@PathVariable("id") Long id) {
		return registeredClientService.removeById(id) ? R.ok()
				: R.fail(BaseResultCode.UPDATE_DATABASE_ERROR, "删除客户端基本信息失败");
	}

}
