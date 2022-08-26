package com.schilings.neiko.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.mapper.ExtendMapper;
import com.schilings.neiko.extend.mybatis.plus.wrapper.WrappersX;
import com.schilings.neiko.extend.mybatis.plus.wrapper.join.NeikoLambdaQueryWrapper;
import com.schilings.neiko.system.model.entity.SysDict;
import com.schilings.neiko.system.model.entity.SysDictItem;
import com.schilings.neiko.system.model.vo.SysDictItemPageVO;

import java.util.List;

public interface SysDictItemMapper extends ExtendMapper<SysDictItem> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param dictCode 字典标识
	 * @return PageResult<SysRoleVO>
	 */
	default PageResult<SysDictItemPageVO> queryPage(PageParam pageParam, String dictCode) {
		IPage<SysDictItemPageVO> page = this.prodPage(pageParam);
		NeikoLambdaQueryWrapper<Object> queryWrapper = WrappersX.lambdaQueryJoin().selectAll(SysDictItem.class)
				.eq(SysDictItem::getDictCode, dictCode);
		IPage<SysDictItemPageVO> voPage = this.selectJoinPage(page, SysDictItemPageVO.class, AUTO_RESULT_MAP,
				queryWrapper);
		return this.prodPage(voPage);
	}

	/**
	 * 根据字典标识查询对应字典项集合
	 * @param dictCode 字典标识
	 * @return List<SysDictItem> 字典项集合
	 */
	default List<SysDictItem> listByDictCode(String dictCode) {
		return this.selectList(Wrappers.<SysDictItem>lambdaQuery().eq(SysDictItem::getDictCode, dictCode));
	}

	/**
	 * 根据字典标识删除对应字典项
	 * @param dictCode 字典标识
	 * @return 是否删除成功
	 */
	default boolean deleteByDictCode(String dictCode) {
		int delete = this.delete(Wrappers.<SysDictItem>lambdaUpdate().eq(SysDictItem::getDictCode, dictCode));
		return SqlHelper.retBool(delete);
	}

	/**
	 * 判断是否存在指定字典标识的字典项
	 * @param dictCode 字典标识
	 * @return boolean 存在：true
	 */
	default boolean existsDictItem(String dictCode) {
		return this.exists(Wrappers.lambdaQuery(SysDictItem.class).eq(SysDictItem::getDictCode, dictCode));
	}

}
