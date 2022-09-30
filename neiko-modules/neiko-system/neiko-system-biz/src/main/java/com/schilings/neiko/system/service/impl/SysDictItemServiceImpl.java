package com.schilings.neiko.system.service.impl;

import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.system.mapper.SysDictItemMapper;
import com.schilings.neiko.system.model.entity.SysDictItem;
import com.schilings.neiko.system.model.vo.SysDictItemPageVO;
import com.schilings.neiko.system.service.SysDictItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysDictItemServiceImpl extends ExtendServiceImpl<SysDictItemMapper, SysDictItem>
		implements SysDictItemService {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param dictCode 字典标识
	 * @return 分页数据
	 */
	@Override
	public PageResult<SysDictItemPageVO> queryPage(PageParam pageParam, String dictCode) {
		return baseMapper.queryPage(pageParam, dictCode);
	}

	/**
	 * 根据Code查询对应字典项数据
	 * @param dictCode 字典标识
	 * @return 字典项集合
	 */
	@Override
	public List<SysDictItem> listByDictCode(String dictCode) {
		return baseMapper.listByDictCode(dictCode);
	}

	/**
	 * 根据字典标识删除对应字典项
	 * @param dictCode 字典标识
	 * @return 是否删除成功
	 */
	@Override
	public boolean removeByDictCode(String dictCode) {
		// 如果存在字典项则进行删除
		if (baseMapper.existsDictItem(dictCode)) {
			return baseMapper.deleteByDictCode(dictCode);
		}
		return true;
	}

	/**
	 * 判断字典项是否存在
	 * @param dictCode 字典标识
	 * @return 存在返回 true
	 */
	@Override
	public boolean exist(String dictCode) {
		return baseMapper.existsDictItem(dictCode);
	}

}
