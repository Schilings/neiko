package com.schilings.neiko.system.converter;

import com.schilings.neiko.system.model.entity.SysDictItem;
import com.schilings.neiko.system.model.vo.DictItemVO;
import com.schilings.neiko.system.model.vo.SysDictItemPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SysDictItemConverter {

	SysDictItemConverter INSTANCE = Mappers.getMapper(SysDictItemConverter.class);

	/**
	 * PO 转 分页VO
	 * @param sysDictItem 字典项
	 * @return SysDictItemPageVO 字典项分页VO
	 */
	SysDictItemPageVO poToPageVo(SysDictItem sysDictItem);

	/**
	 * 字典项实体 转 VO
	 * @param sysDictItem 字典项
	 * @return 字典项VO
	 */
	DictItemVO poToItemVo(SysDictItem sysDictItem);

}
