package com.schilings.neiko.log.converter;

import com.schilings.neiko.log.model.entity.LoginLog;
import com.schilings.neiko.log.model.vo.LoginLogPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LoginLogConverter {

    LoginLogConverter INSTANCE = Mappers.getMapper(LoginLogConverter.class);

    /**
     * PO 转 PageVO
     * @param loginLog 登陆日志
     * @return AdminLoginLogPageVO 登陆日志PageVO
     */
    LoginLogPageVO poToPageVo(LoginLog loginLog);
}
