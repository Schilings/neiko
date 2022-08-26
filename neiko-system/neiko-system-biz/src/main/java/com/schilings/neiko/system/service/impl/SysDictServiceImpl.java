package com.schilings.neiko.system.service.impl;

import com.schilings.neiko.extend.mybatis.plus.service.ExtendService;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.system.mapper.SysDictMapper;
import com.schilings.neiko.system.model.entity.SysDict;
import com.schilings.neiko.system.service.SysDictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysDictServiceImpl extends ExtendServiceImpl<SysDictMapper, SysDict> implements SysDictService {

}
