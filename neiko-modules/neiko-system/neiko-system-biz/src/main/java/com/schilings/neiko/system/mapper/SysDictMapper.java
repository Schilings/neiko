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
import com.schilings.neiko.system.model.qo.SysDictQO;
import com.schilings.neiko.system.model.vo.SysDictPageVO;

import java.util.Collection;
import java.util.List;

public interface SysDictMapper extends ExtendMapper<SysDict> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询对象
	 * @return PageResult<SysRoleVO>
	 */
	default PageResult<SysDictPageVO> queryPage(PageParam pageParam, SysDictQO qo) {
		IPage<SysDictPageVO> page = this.prodPage(pageParam);
		NeikoLambdaQueryWrapper<Object> queryWrapper = WrappersX.lambdaQueryJoin().selectAll(SysDict.class)
				.likeIfPresent(SysDict::getCode, qo.getCode()).likeIfPresent(SysDict::getTitle, qo.getTitle());
		IPage<SysDictPageVO> voPage = this.selectJoinPage(page, SysDictPageVO.class, AUTO_RESULT_MAP, queryWrapper);
		return this.prodPage(voPage);
	}

	/**
	 * 根据字典标识查询对应字典
	 * @param dictCode 字典标识
	 * @return SysDict 字典
	 */
	default SysDict getByCode(String dictCode) {
		return this.selectOne(WrappersX.<SysDict>lambdaQueryX().eq(SysDict::getCode, dictCode));
	}

	/**
	 * 根据字典标识数组查询对应字典集合
	 * @param dictCodes 字典标识数组
	 * @return List<SysDict> 字典集合
	 */
	default List<SysDict> listByCodes(Collection<String> dictCodes) {
		return this.selectList(WrappersX.<SysDict>lambdaQueryX().in(SysDict::getCode, dictCodes));
	}

	/**
	 * 更新字典的HashCode
	 * @param dictCode 字典标识
	 * @param hashCode 哈希值
	 * @return boolean 是否更新成功
	 */
	default boolean updateHashCode(String dictCode, String hashCode) {
		int update = this.update(null,
				Wrappers.<SysDict>lambdaUpdate().set(SysDict::getHashCode, hashCode).eq(SysDict::getCode, dictCode));
		return SqlHelper.retBool(update);
	}

}
