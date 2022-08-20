package com.schilings.neiko.system.converter;

import com.schilings.neiko.system.model.entity.SysConfig;
import com.schilings.neiko.system.model.vo.SysConfigPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SysConfigConverter {

    SysConfigConverter INSTANCE = Mappers.getMapper(SysConfigConverter.class);

    /**
     * PO 转 PageVO
     * @param sysConfig 基础配置
     * @return SysConfigPageVO 基础配置分页VO
     */
    SysConfigPageVO poToPageVo(SysConfig sysConfig);
    
}
