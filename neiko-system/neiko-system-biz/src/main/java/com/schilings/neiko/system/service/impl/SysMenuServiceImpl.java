package com.schilings.neiko.system.service.impl;

import com.schilings.neiko.common.event.publisher.EventBus;

import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.system.mapper.SysMenuMapper;
import com.schilings.neiko.system.model.entity.SysMenu;
import com.schilings.neiko.system.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ExtendServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private final EventBus eventBus;
}
