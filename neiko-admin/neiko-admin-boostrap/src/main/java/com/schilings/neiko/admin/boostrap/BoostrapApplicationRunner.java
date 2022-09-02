package com.schilings.neiko.admin.boostrap;

import com.schilings.neiko.auth.mapper.AuthClientMapper;
import com.schilings.neiko.auth.service.AuthClientService;
import com.schilings.neiko.log.mapper.AccessLogMapper;
import com.schilings.neiko.log.mapper.OperationLogMapper;
import com.schilings.neiko.log.service.AccessLogService;
import com.schilings.neiko.log.service.OperationLogService;
import com.schilings.neiko.notify.service.AnnouncementService;
import com.schilings.neiko.notify.service.UserAnnouncementService;
import com.schilings.neiko.system.mapper.*;

import com.schilings.neiko.system.service.*;
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
	private AuthClientService authClientService;

	// system
	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysRoleService sysRoleService;

	@Autowired
	private SysMenuService sysMenuService;

	@Autowired
	private SysUserRoleService sysUserRoleService;

	@Autowired
	private SysRoleMenuService sysRoleMenuService;

	@Autowired
	private SysOrganizationService sysOrganizationService;

	@Autowired
	private SysConfigService sysConfigService;

	@Autowired
	private SysDictService sysDictService;

	@Autowired
	private SysDictItemService sysDictItemService;

	// notify
	@Autowired
	private AnnouncementService announcementService;

	@Autowired
	private UserAnnouncementService userAnnouncementService;

	// log
	@Autowired
	private AccessLogService accessLogService;

	@Autowired
	private OperationLogService operationLogService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (isAuthBoostrap.compareAndSet(false, true)) {
			log.info("[start-neiko]数据库数据应用初始化 开始。。。");
			// auth
			authClientService.saveBatch(BoostrapDataHolder.getAuthClientList());
			// system
			sysUserService.saveBatch(BoostrapDataHolder.getSysUserList());
			sysRoleService.saveBatch(BoostrapDataHolder.getSysRoleList());
			sysMenuService.saveBatch(BoostrapDataHolder.getSysMenuList());
			sysUserRoleService.saveBatch(BoostrapDataHolder.getSysUserRoleList());
			sysRoleMenuService.saveBatch(BoostrapDataHolder.getSysRoleMenuList());
			sysOrganizationService.saveBatch(BoostrapDataHolder.getSysOrganizationList());
			sysConfigService.saveBatch(BoostrapDataHolder.getSysConfigList());
			sysDictService.saveBatch(BoostrapDataHolder.getSysDictList());
			sysDictItemService.saveBatch(BoostrapDataHolder.getSysDictItemList());
			log.info("[finish-neiko]数据库数据应用初始化 结束。。。");
		}
	}

}
