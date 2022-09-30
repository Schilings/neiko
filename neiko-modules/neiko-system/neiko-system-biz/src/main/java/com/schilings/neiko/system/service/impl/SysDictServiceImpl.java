package com.schilings.neiko.system.service.impl;

import cn.hutool.core.util.IdUtil;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.system.mapper.SysDictMapper;
import com.schilings.neiko.system.model.entity.SysDict;
import com.schilings.neiko.system.model.qo.SysDictQO;
import com.schilings.neiko.system.model.vo.SysDictPageVO;
import com.schilings.neiko.system.service.SysDictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysDictServiceImpl extends ExtendServiceImpl<SysDictMapper, SysDict> implements SysDictService {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<SysDictVO> 分页数据
	 */
	@Override
	public PageResult<SysDictPageVO> queryPage(PageParam pageParam, SysDictQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

	/**
	 * 根据字典标识查询
	 * @param dictCode 字典标识
	 * @return 字典数据
	 */
	@Override
	public SysDict getByCode(String dictCode) {
		return baseMapper.getByCode(dictCode);
	}

	/**
	 * 根据字典标识数组查询对应字典集合
	 * @param dictCodes 字典标识数组
	 * @return List<SysDict> 字典集合
	 */
	@Override
	public List<SysDict> listByCodes(Collection<String> dictCodes) {
		if (dictCodes == null || dictCodes.size() == 0) {
			return new ArrayList<>();
		}
		return baseMapper.listByCodes(dictCodes);
	}

	/**
	 * 更新字典HashCode
	 * @param dictCode 字典标识
	 * @return 更新状态: 成功(true) 失败(false)
	 */
	@Override
	public boolean updateHashCode(String dictCode) {
		return baseMapper.updateHashCode(dictCode, IdUtil.fastSimpleUUID());
	}

}
