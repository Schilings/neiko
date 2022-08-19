package com.schilings.neiko.system.service.impl;


import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.system.mapper.SysOrganizationMapper;
import com.schilings.neiko.system.model.entity.SysOrganization;
import com.schilings.neiko.system.service.SysOrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysOrganizationServiceImpl extends ExtendServiceImpl<SysOrganizationMapper,SysOrganization> implements SysOrganizationService {
}
