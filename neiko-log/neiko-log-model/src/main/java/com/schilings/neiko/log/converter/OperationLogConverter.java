package com.schilings.neiko.log.converter;

import com.schilings.neiko.log.model.entity.OperationLog;
import com.schilings.neiko.log.model.vo.OperationLogPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OperationLogConverter {
    
    OperationLogConverter INSTANCE = Mappers.getMapper(OperationLogConverter.class);

    /**
     * PO 转 PageVO
     * @param operationLog 操作日志
     * @return AdminOperationLogPageVO 操作日志PageVO
     */
    OperationLogPageVO poToPageVo(OperationLog operationLog);
}
