package com.schilings.neiko.system.service;

import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.ExtendService;
import com.schilings.neiko.system.model.entity.SysDict;
import com.schilings.neiko.system.model.qo.SysDictQO;
import com.schilings.neiko.system.model.vo.SysDictPageVO;

import java.util.Collection;
import java.util.List;

public interface SysDictService extends ExtendService<SysDict> {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<SysDictVO> 分页数据
	 */
	PageResult<SysDictPageVO> queryPage(PageParam pageParam, SysDictQO qo);

	/**
	 * 根据字典标识查询
	 * @param dictCode 字典标识
	 * @return 字典数据
	 */
	SysDict getByCode(String dictCode);

	/**
	 * 根据字典标识数组查询对应字典集合
	 * @param dictCodes 字典标识数组
	 * @return List<SysDict> 字典集合
	 */
	List<SysDict> listByCodes(Collection<String> dictCodes);

	/**
	 * 更新字典HashCode
	 * @param dictCode 字典标识
	 * @return 更新状态 成功（true） or 失败 (false)
	 */
	boolean updateHashCode(String dictCode);

}
