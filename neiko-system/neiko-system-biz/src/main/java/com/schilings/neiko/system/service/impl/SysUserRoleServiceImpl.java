package com.schilings.neiko.system.service.impl;


import com.schilings.neiko.common.event.publisher.EventBus;

import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.system.mapper.SysUserRoleMapper;
import com.schilings.neiko.system.model.entity.SysUserRole;
import com.schilings.neiko.system.service.SysUserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserRoleServiceImpl extends ExtendServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    private final EventBus EventBus;


    
}
