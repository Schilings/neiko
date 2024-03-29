package com.schilings.neiko.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.domain.SelectData;
import com.schilings.neiko.extend.mybatis.plus.mapper.ExtendMapper;
import com.schilings.neiko.extend.mybatis.plus.wrapper.WrappersX;
import com.schilings.neiko.extend.mybatis.plus.wrapper.join.NeikoLambdaQueryWrapper;
import com.schilings.neiko.system.model.entity.SysRole;
import com.schilings.neiko.system.model.qo.SysRoleQO;
import com.schilings.neiko.system.model.vo.SysRolePageVO;

import java.util.Collection;
import java.util.List;

import static com.schilings.neiko.common.model.constants.GlobalConstants.NOT_DELETED_FLAG;

/**
 *
 * <p>
 * 系统角色表
 * </p>
 *
 * @author Schilings
 */
public interface SysRoleMapper extends ExtendMapper<SysRole> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询对象
	 * @return PageResult<SysRoleVO>
	 */
	default PageResult<SysRolePageVO> queryPage(PageParam pageParam, SysRoleQO qo) {
		IPage<SysRolePageVO> page = this.prodPage(pageParam);
		NeikoLambdaQueryWrapper<Object> queryWrapper = WrappersX.lambdaQueryJoin().selectAll(SysRole.class)
				.eq(SysRole::getDeleted, NOT_DELETED_FLAG).likeIfPresent(SysRole::getName, qo.getName())
				.likeIfPresent(SysRole::getCode, qo.getCode())
				.betweenIfPresent(SysRole::getCreateTime, qo.getStartTime(), qo.getEndTime());
		IPage<SysRolePageVO> iPage = this.selectJoinPage(page, SysRolePageVO.class, AUTO_RESULT_MAP, queryWrapper);
		return this.prodPage(iPage);
	}

	/**
	 * 获取角色下拉框数据
	 * @return 下拉选择框数据集合
	 */
	default List<SelectData> listSelectData() {
		NeikoLambdaQueryWrapper<SelectData<Void>> queryWrapper = WrappersX.<SelectData<Void>>lambdaQueryJoin()
				.selectAs(SysRole::getName, SelectData<Void>::getName)
				.selectAs(SysRole::getCode, SelectData<Void>::getValue).eq(SysRole::getDeleted, NOT_DELETED_FLAG);
		return this.selectJoinList(SelectData.class, AUTO_RESULT_MAP, queryWrapper);
	}

	/**
	 * 根据多个roleCode查多个角色列表
	 * @param roleCodes
	 */
	default List<SysRole> listByRoleCodes(Collection<String> roleCodes) {
		return this.selectList(WrappersX.lambdaQueryX(SysRole.class).eq(SysRole::getDeleted, NOT_DELETED_FLAG)
				.in(SysRole::getCode, roleCodes));
	}

}
