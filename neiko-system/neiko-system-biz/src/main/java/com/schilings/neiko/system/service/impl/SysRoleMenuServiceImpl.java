package com.schilings.neiko.system.service.impl;


import com.schilings.neiko.common.event.publisher.EventBus;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.system.mapper.SysRoleMenuMapper;
import com.schilings.neiko.system.model.entity.SysRole;
import com.schilings.neiko.system.model.entity.SysRoleMenu;
import com.schilings.neiko.system.service.SysRoleMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleMenuServiceImpl extends ExtendServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

    private final EventBus eventBus;


}
