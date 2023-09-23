package com.schilings.neiko.system.remote;

import com.schilings.neiko.common.core.http.RequestParameterObject;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.system.model.entity.SysConfig;
import com.schilings.neiko.system.model.qo.SysConfigQO;
import com.schilings.neiko.system.model.vo.SysConfigPageVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;

@HttpExchange("/system/config")
public interface SysConfigRemote {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param sysConfigQO 系统配置
	 * @return R<PageResult < SysConfigVO>>
	 */
	@GetExchange("/page")
	R<PageResult<SysConfigPageVO>> getSysConfigPage(PageParam pageParam,
			@RequestParameterObject SysConfigQO sysConfigQO);

	/**
	 * 新增系统配置
	 * @param sysConfig 系统配置
	 * @return R
	 */
	@PostExchange
	R<Boolean> save(@RequestBody SysConfig sysConfig);

	/**
	 * 修改系统配置
	 * @param sysConfig 系统配置
	 * @return R
	 */
	@PutExchange
	R<Boolean> updateById(@RequestBody SysConfig sysConfig);

	/**
	 * 删除系统配置
	 * @param confKey confKey
	 * @return R
	 */
	@DeleteExchange
	R<Boolean> removeById(@RequestParam("confKey") String confKey);

}
