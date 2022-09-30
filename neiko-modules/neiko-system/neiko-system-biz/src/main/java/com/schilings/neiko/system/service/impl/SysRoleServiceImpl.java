package com.schilings.neiko.system.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.schilings.neiko.common.core.exception.ServiceException;
import com.schilings.neiko.common.event.publisher.EventBus;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.domain.SelectData;
import com.schilings.neiko.common.model.result.BaseResultCode;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.system.mapper.SysRoleMapper;
import com.schilings.neiko.system.model.entity.SysRole;
import com.schilings.neiko.system.model.qo.SysRoleQO;
import com.schilings.neiko.system.model.vo.SysRolePageVO;
import com.schilings.neiko.system.service.SysRoleMenuService;
import com.schilings.neiko.system.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ExtendServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

	private final EventBus eventBus;

	private final SysRoleMenuService sysRoleMenuService;

	/**
	 * 查询系统角色列表
	 * @param pageParam 分页参数
	 * @param qo 查询参数
	 * @return 分页对象
	 */
	@Override
	public PageResult<SysRolePageVO> queryPage(PageParam pageParam, SysRoleQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

	/**
	 * 根据多个roleCode查多个角色列表
	 * @param roleCodes
	 */
	@Override
	public List<SysRole> list(Collection<String> roleCodes) {
		return baseMapper.listByRoleCodes(roleCodes);
	}

	/**
	 * 通过角色ID，删除角色,并清空角色菜单缓存
	 * @param id 角色ID
	 * @return boolean
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeById(Serializable id) {
		SysRole role = getById(id);
		boolean deleteSuccess = sysRoleMenuService.deleteAllByRoleCode(role.getCode());
		Assert.isTrue(deleteSuccess, () -> {
			log.error("[removeById] 删除角色菜单关联关系失败，roleId：{}", id);
			return new ServiceException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "删除角色菜单关联关系失败");
		});
		return SqlHelper.retBool(baseMapper.deleteById(id));
	}

	/**
	 * 角色的选择数据
	 * @return List<SelectData<?>>
	 */
	@Override
	public List<SelectData> listSelectData() {
		return baseMapper.listSelectData();
	}

}
