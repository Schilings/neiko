package com.schilings.neiko.log.service;

import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.ExtendService;
import com.schilings.neiko.log.model.entity.OperationLog;
import com.schilings.neiko.log.model.qo.OperationLogQO;
import com.schilings.neiko.log.model.vo.OperationLogPageVO;
import org.springframework.scheduling.annotation.Async;

public interface OperationLogService extends ExtendService<OperationLog> {

    /**
     * 根据QueryObject查询分页数据
     * @param pageParam 分页参数
     * @param qo 查询参数对象
     * @return PageResult<LoginLogVO> 分页数据
     */
    PageResult<OperationLogPageVO> queryPage(PageParam pageParam, OperationLogQO qo);


    /**
     * 异步保存操作日志
     * @param operationLog 操作日志
     */
    void saveAsync(OperationLog operationLog);



}
