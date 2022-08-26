package com.schilings.neiko.admin.boostrap;

import com.schilings.neiko.auth.mapper.AuthClientMapper;
import com.schilings.neiko.log.mapper.AccessLogMapper;
import com.schilings.neiko.log.mapper.OperationLogMapper;
import com.schilings.neiko.system.mapper.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Order
@Slf4j
public class BoostrapApplicationRunner implements ApplicationRunner {

	private AtomicBoolean isAuthBoostrap = new AtomicBoolean(false);

	// auth
	@Autowired
	private AuthClientMapper authClientMapper;

	// system
	@Autowired
	private SysUserMapper sysUserMapper;

	@Autowired
	private SysRoleMapper sysRoleMapper;

	@Autowired
	private SysMenuMapper sysMenuMapper;

	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;

	@Autowired
	private SysRoleMenuMapper sysRoleMenuMapper;

	@Autowired
	private SysOrganizationMapper sysOrganizationMapper;

	@Autowired
	private SysConfigMapper sysConfigMapper;

	@Autowired
	private SysDictMapper sysDictMapper;

	@Autowired
	private SysDictItemMapper sysDictItemMapper;

	// log
	@Autowired
	private AccessLogMapper accessLogMapper;

	@Autowired
	private OperationLogMapper operationLogMapper;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (isAuthBoostrap.compareAndSet(false, true)) {
			log.info("[start-neiko]数据库数据应用初始化 开始。。。");
			// auth
			authClientMapper.insertBatchSomeColumn(BoostrapDataHolder.getAuthClientList());
			// system
			sysUserMapper.insertBatchSomeColumn(BoostrapDataHolder.getSysUserList());
			sysRoleMapper.insertBatchSomeColumn(BoostrapDataHolder.getSysRoleList());
			sysMenuMapper.insertBatchSomeColumn(BoostrapDataHolder.getSysMenuList());
			sysUserRoleMapper.insertBatchSomeColumn(BoostrapDataHolder.getSysUserRoleList());
			sysRoleMenuMapper.insertBatchSomeColumn(BoostrapDataHolder.getSysRoleMenuList());
			sysOrganizationMapper.insertBatchSomeColumn(BoostrapDataHolder.getSysOrganizationList());
			log.info("[finish-neiko]数据库数据应用初始化 结束。。。");
		}
	}

}
