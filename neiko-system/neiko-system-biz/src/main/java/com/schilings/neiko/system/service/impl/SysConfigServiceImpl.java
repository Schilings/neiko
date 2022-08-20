package com.schilings.neiko.system.service.impl;


import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.system.mapper.SysConfigMapper;
import com.schilings.neiko.system.model.entity.SysConfig;
import com.schilings.neiko.system.model.qo.SysConfigQO;
import com.schilings.neiko.system.model.vo.SysConfigPageVO;
import com.schilings.neiko.system.service.SysConfigService;
import org.springframework.stereotype.Service;

@Service
public class SysConfigServiceImpl extends ExtendServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {


    /**
     * 根据QueryObject查询分页数据
     * @param pageParam 分页参数
     * @param sysConfigQO 查询参数对象
     * @return 分页数据
     */
    @Override
    public PageResult<SysConfigPageVO> queryPage(PageParam pageParam, SysConfigQO sysConfigQO) {
        return baseMapper.queryPage(pageParam, sysConfigQO);
    }
}
