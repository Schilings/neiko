package com.schilings.neiko.system.converter;

import com.schilings.neiko.system.model.entity.SysUser;
import com.schilings.neiko.system.model.vo.SysUserPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SysUserConverter {

    SysUserConverter INSTANCE = Mappers.getMapper(SysUserConverter.class);
    
    /**
     * PO 转 PageVO
     * @param sysUser 系统用户
     * @return SysUserPageVO 系统用户PageVO
     */
    SysUserPageVO poToPageVo(SysUser sysUser);

}
