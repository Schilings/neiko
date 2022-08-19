package com.schilings.neiko.log.service.impl;


import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.log.mapper.OperationLogMapper;
import com.schilings.neiko.log.model.entity.OperationLog;
import com.schilings.neiko.log.model.qo.OperationLogQO;
import com.schilings.neiko.log.model.vo.OperationLogPageVO;
import com.schilings.neiko.log.service.OperationLogService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class OperationLogServiceImpl extends ExtendServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    /**
     * 根据QueryObject查询分页数据
     * @param pageParam 分页参数
     * @param qo 查询参数对象
     * @return PageResult<LoginLogVO> 分页数据
     */
    @Override
    public PageResult<OperationLogPageVO> queryPage(PageParam pageParam, OperationLogQO qo) {
        return baseMapper.queryPage(pageParam, qo);
    }

    /**
     * 异步保存操作日志
     * @param operationLog 操作日志
     */
    @Async
    @Override
    public void saveAsync(OperationLog operationLog) {
        baseMapper.insert(operationLog);
    }
    
}
