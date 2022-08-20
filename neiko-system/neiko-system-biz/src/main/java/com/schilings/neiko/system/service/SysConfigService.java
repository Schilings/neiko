package com.schilings.neiko.system.service;

import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.ExtendService;
import com.schilings.neiko.system.model.entity.SysConfig;
import com.schilings.neiko.system.model.qo.SysConfigQO;
import com.schilings.neiko.system.model.vo.SysConfigPageVO;

public interface SysConfigService extends ExtendService<SysConfig> {

    /**
     * 根据QueryObject查询分页数据
     * @param pageParam 分页参数
     * @param sysConfigQO 查询参数对象
     * @return 分页数据
     */
    PageResult<SysConfigPageVO> queryPage(PageParam pageParam, SysConfigQO sysConfigQO);

}
