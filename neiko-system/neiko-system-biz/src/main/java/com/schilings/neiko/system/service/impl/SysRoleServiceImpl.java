package com.schilings.neiko.system.service.impl;


import com.schilings.neiko.common.event.publisher.EventBus;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.system.mapper.SysRoleMapper;
import com.schilings.neiko.system.model.entity.SysRole;
import com.schilings.neiko.system.model.qo.SysRoleQO;
import com.schilings.neiko.system.model.vo.SysRolePageVO;
import com.schilings.neiko.system.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ExtendServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final EventBus eventBus;

    /**
     * 查询系统角色列表
     * @param pageParam 分页参数
     * @param qo 查询参数
     * @return 分页对象
     */
    @Override
    public PageResult<SysRolePageVO> queryPage(PageParam pageParam, SysRoleQO sysRoleQO) {
        return baseMapper.queryPage(pageParam, sysRoleQO);
    }
}
