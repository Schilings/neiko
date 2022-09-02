package com.schilings.neiko.system.service;

import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.domain.SelectData;
import com.schilings.neiko.extend.mybatis.plus.service.ExtendService;
import com.schilings.neiko.system.model.entity.SysRole;
import com.schilings.neiko.system.model.qo.SysRoleQO;
import com.schilings.neiko.system.model.vo.SysRolePageVO;

import java.util.List;

public interface SysRoleService extends ExtendService<SysRole> {

	/**
	 * 查询系统角色列表
	 * @param pageParam 分页参数
	 * @param qo 查询参数
	 * @return 分页对象
	 */
	PageResult<SysRolePageVO> queryPage(PageParam pageParam, SysRoleQO qo);

	/**
	 * 角色的选择数据
	 * @return 角色下拉列表数据集合
	 */
	List<SelectData> listSelectData();

}
