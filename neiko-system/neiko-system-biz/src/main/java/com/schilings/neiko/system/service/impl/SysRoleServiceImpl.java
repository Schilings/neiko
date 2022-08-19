package com.schilings.neiko.system.service.impl;


import com.schilings.neiko.common.event.publisher.EventBus;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.system.mapper.SysRoleMapper;
import com.schilings.neiko.system.model.entity.SysRole;
import com.schilings.neiko.system.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ExtendServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final EventBus eventBus;


}
