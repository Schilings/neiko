package com.schilings.neiko.system.service;

import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.ExtendService;
import com.schilings.neiko.system.model.entity.SysDictItem;
import com.schilings.neiko.system.model.vo.SysDictItemPageVO;

import java.util.List;

public interface SysDictItemService extends ExtendService<SysDictItem> {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param dictCode 查询参数对象
	 * @return 分页数据
	 */
	PageResult<SysDictItemPageVO> queryPage(PageParam pageParam, String dictCode);

	/**
	 * 根据Code查询对应字典项数据
	 * @param dictCode 字典标识
	 * @return 该字典对应的字典项集合
	 */
	List<SysDictItem> listByDictCode(String dictCode);

	/**
	 * 根据字典标识删除对应字典项
	 * @param dictCode 字典标识
	 * @return 是否删除成功
	 */
	boolean removeByDictCode(String dictCode);

	/**
	 * 根据字典标识判断是否存在对应字典项
	 * @param dictCode 字典标识
	 * @return boolean 存在返回 true
	 */
	boolean exist(String dictCode);

}
