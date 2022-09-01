package com.schilings.neiko.admin.boostrap;

import com.schilings.neiko.auth.model.entity.AuthClient;
import com.schilings.neiko.common.util.json.JsonUtils;
import com.schilings.neiko.common.util.json.TypeReference;
import com.schilings.neiko.system.model.entity.*;
import io.swagger.v3.core.util.Json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoostrapDataHolder {

	// auth
	private static List<AuthClient> authClientList;

	// system
	private static List<SysUser> sysUserList;

	private static List<SysRole> sysRoleList;

	private static List<SysMenu> sysMenuList;

	private static List<SysUserRole> sysUserRoleList;

	private static List<SysRoleMenu> sysRoleMenuList;

	private static List<SysOrganization> sysOrganizationList;

	private static List<SysConfig> sysConfigList;

	private static List<SysDict> sysDictList;

	private static List<SysDictItem> sysDictItemList;

	static {
		// auth
		if (authClientList == null) {
			authClientList = new ArrayList<>();
			authClientList.add(new AuthClient(1L, "1001", "99109f1b0cf6ae9b4572d99fc8bcc0df", "server,userinfo,system",
					"*", "authorization_code,refresh_token,password,client_credentials,implicit", null, null, null,
					null));
		}

		// system
		if (sysUserList == null) {
			sysUserList = new ArrayList<>();
			sysUserList.add(new SysUser(1L, "admin", "超管牛逼", "51f5c2f36d9803ab55d4ba5cee9a3e48", "",
					"sysuser/1/avatar/20200226/ab6bd5221afe4238ae4987f278758113.jpg", 1, "chengbohua@foxmail.com",
					"15800000000", 1, 1, 6L));
			sysUserList.add(new SysUser(2L, "test", "测试用户213", "51f5c2f36d9803ab55d4ba5cee9a3e48", "",
					"sysuser/10/avatar/20201204/002875d468db41239ee02ad99ab14490.jpg", 2, "magic.xiaohua@gmail.com",
					"12345678520", 0, 1, 6L));
		}
		if (sysRoleList == null) {
			sysRoleList = new ArrayList<>();
			sysRoleList.add(new SysRole(1L, "管理员", "ROLE_ADMIN", 1, 0, null, "管理员"));
			sysRoleList.add(new SysRole(2L, "测试工程师", "ROLE_TEST", 2, 1, null, "测试工程师"));
			sysRoleList.add(new SysRole(14L, "销售主管", "ROLE_SALES_EXECUTIVE", 2, 1, null, "销售主管"));
			sysRoleList.add(new SysRole(15L, "销售专员", "ROLE_SALESMAN", 2, 1, null, "销售专员"));
		}
		if (sysMenuList == null) {
			sysMenuList = new ArrayList<>(); 
			sysMenuList.add(new SysMenu(10028L, 0L, "个人页", "user", null, "account", 1, "account/settings/Index", 0, 0, 1, 1, null));
			sysMenuList.add(new SysMenu(10030L, 10028L, "个人设置", null, null, "settings", 1, null, 1, 0, 1, 0, null));
			sysMenuList.add(new SysMenu(10031L, 10030L, "基本设置", null, null, "base", 1, "account/settings/BaseSetting", 1, 0, 0, 1, null));
			sysMenuList.add(new SysMenu(10032L, 10030L, "安全设置", null, null, "security", 1, "account/settings/Security", 2, 0, 0, 1, null));
			sysMenuList.add(new SysMenu(10034L, 10030L, "账户绑定", null, null, "binding", 1, "account/settings/Binding", 4, 0, 0, 1, null));
			sysMenuList.add(new SysMenu(10035L, 10030L, "新消息通知", null, null, "notification", 1, "account/settings/Notification", 5, 0, 0, 1, null));
			sysMenuList.add(new SysMenu(100000L, 0L, "系统管理", "setting", null, "system", 1, null, 1, 0, 0, 0, null));
			sysMenuList.add(new SysMenu(100100L, 100000L, "系统用户", null, null, "user", 1, "system/user/SysUserPage", 1, 0, 0, 1, null));
			sysMenuList.add(new SysMenu(100101L, 100100L, "系统用户查询", null, "system:user:read", null, 1, null, 1, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100102L, 100100L, "系统用户新增", null, "system:user:add", null, 1, null, 1, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100103L, 100100L, "系统用户修改", null, "system:user:edit", null, 1, null, 2, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100104L, 100100L, "系统用户删除", null, "system:user:del", null, 1, null, 3, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100105L, 100100L, "系统用户授权", null, "system:user:grant", null, 1, null, 3, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100106L, 100100L, "系统用户改密", null, "system:user:pass", null, 1, null, 4, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100200L, 100000L, "角色管理", null, null, "role", 1, "system/role/SysRolePage", 2, 0, 0, 1, null));
			sysMenuList.add(new SysMenu(100201L, 100200L, "系统角色查询", null, "system:role:read", null, 1, null, 0, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100202L, 100200L, "系统角色新增", null, "system:role:add", null, 1, null, 1, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100203L, 100200L, "系统角色修改", null, "system:role:edit", null, 1, null, 2, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100204L, 100200L, "系统角色删除", null, "system:role:del", null, 1, null, 3, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100205L, 100200L, "系统角色授权", null, "system:role:grant", null, 1, null, 3, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100400L, 100000L, "配置信息", null, null, "config", 1, "system/config/SysConfigPage", 6, 0, 0, 1, null));
			sysMenuList.add(new SysMenu(100401L, 100400L, "配置查询", null, "system:config:read", null, 1, null, 0, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100402L, 100400L, "配置新增", null, "system:config:add", null, 1, null, 1, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100403L, 100400L, "配置修改", null, "system:config:edit", null, 1, null, 2, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100404L, 100400L, "配置删除", null, "system:config:del", null, 1, null, 3, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100500L, 100000L, "字典管理", null, null, "dict", 1, "system/dict/SysDictPage", 5, 0, 0, 1, null));
			sysMenuList.add(new SysMenu(100501L, 100500L, "字典查询", null, "system:dict:read", null, 1, null, 0, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100502L, 100500L, "字典新增", null, "system:dict:add", null, 1, null, 1, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100503L, 100500L, "字典修改", null, "system:dict:edit", null, 1, null, 2, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100504L, 100500L, "字典删除", null, "system:dict:del", null, 1, null, 3, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100700L, 100000L, "组织架构", null, null, "organization", 1, "system/organization/SysOrganizationPage", 4, 0, 0, 1, null));
			sysMenuList.add(new SysMenu(100701L, 100700L, "组织架构查询", null, "system:organization:read", null, 1, null, 0, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100702L, 100700L, "组织架构新增", null, "system:organization:add", null, 1, null, 1, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100703L, 100700L, "组织架构修改", null, "system:organization:edit", null, 1, null, 2, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100704L, 100700L, "组织架构删除", null, "system:organization:del", null, 1, null, 3, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100705L, 100700L, "组织机构校正", null, "system:organization:revised", null, 1, "", 5, 0, 0, 2, "校正组织机构层级和深度"));
			sysMenuList.add(new SysMenu(100800L, 100000L, "菜单权限", null, null, "menu", 1, "system/menu/SysMenuPage", 3, 0, 0, 1, null));
			sysMenuList.add(new SysMenu(100801L, 100800L, "菜单权限查询", null, "system:menu:read", null, 1, null, 0, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100802L, 100800L, "菜单权限新增", null, "system:menu:add", null, 1, null, 1, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100803L, 100800L, "菜单权限修改", null, "system:menu:edit", null, 1, null, 2, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100804L, 100800L, "菜单权限删除", null, "system:menu:del", null, 1, null, 3, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(110000L, 0L, "日志管理", "file-search", null, "log", 1, null, 2, 0, 0, 0, null));
			sysMenuList.add(new SysMenu(110100L, 110000L, "操作日志", null, null, "operation-log", 1, "log/operation-log/OperationLogPage", 2, 0, 0, 1, null));
			sysMenuList.add(new SysMenu(110101L, 110100L, "操作日志查询", null, "log:operation-log:read", null, 1, null, 0, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(110200L, 110000L, "登陆日志", null, null, "login-log", 1, "log/login-log/LoginLogPage", 1, 0, 0, 1, null));
			sysMenuList.add(new SysMenu(110201L, 110200L, "登陆日志查询", null, "log:login-log:read", null, 1, null, 0, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(110300L, 110000L, "访问日志", null, null, "access-log", 1, "log/access-log/AccessLogPage", 3, 0, 0, 1, null));
			sysMenuList.add(new SysMenu(110301L, 110300L, "访问日志查询", null, "log:access-log:read", null, 1, null, 0, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(120000L, 0L, "消息通知", "message", null, "notify", 1, null, 3, 0, 0, 0, null));
			sysMenuList.add(new SysMenu(120100L, 120000L, "公告信息", null, null, "announcement", 1, "notify/announcement/AnnouncementPage", 1, 0, 0, 1, null));
			sysMenuList.add(new SysMenu(120101L, 120100L, "公告信息查询", null, "notify:announcement:read", null, 1, null, 0, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(120102L, 120100L, "公告信息新增", null, "notify:announcement:add", null, 1, null, 1, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(120103L, 120100L, "公告信息修改", null, "notify:announcement:edit", null, 1, null, 2, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(120104L, 120100L, "公告信息删除", null, "notify:announcement:del", null, 1, null, 3, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(120200L, 120000L, "用户公告", null, null, "userannouncement", 1, "notify/userannouncement/UserAnnouncementPage", 1, 0, 1, 1, null));
			sysMenuList.add(new SysMenu(120201L, 120200L, "用户公告表查询", null, "notify:userannouncement:read", null, 1, null,0, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(660000L, 0L, "Ballcat 官网", "crown", null, "ballcat", 2, "http://www.ballcat.cn", 0, 1, 0, 1, null));
			sysMenuList.add(new SysMenu(990000L, 0L, "开发平台", "desktop", "", "develop", 1, null, 99, 0, 0, 0, null));
			sysMenuList.add(new SysMenu(990100L, 990000L, "接口文档", "file", "", "swagger", 3, "http://ballcat-admin:8080/swagger-ui/index.html", 1, 0, 0, 1, null));
			sysMenuList.add(new SysMenu(990200L, 990000L, "文档增强", "file-text", "", "doc", 3, "http://ballcat-admin:8080/doc.html", 2, 0, 0, 1, null));
			sysMenuList.add(new SysMenu(990300L, 990000L, "调度中心", "rocket", "", "xxl-job", 3, "http://ballcat-job:8888/xxl-job-admin", 3, 0, 0, 1, null));
			sysMenuList.add(new SysMenu(990400L, 990000L, "服务监控", "alert", "", "monitor", 3, "http://ballcat-monitor:9999", 4, 0, 0, 1, null));
			sysMenuList.add(new SysMenu(990500L, 990000L, "代码生成", "printer", "", "codegen", 3, "http://localhost:7777", 5, 0, 0, 1, null));
		}
		if (sysUserRoleList == null) {
			sysUserRoleList = new ArrayList<>();
			sysUserRoleList.add(new SysUserRole(1L, 1L, "ROLE_ADMIN"));
			sysUserRoleList.add(new SysUserRole(6L, 14L, "ROLE_SALES_EXECUTIVE"));
			sysUserRoleList.add(new SysUserRole(4L, 1L, "ROLE_TEST"));
		}
		if (sysRoleMenuList == null) {
			sysRoleMenuList = new ArrayList<>();
			sysRoleMenuList.add(new SysRoleMenu(572L, "ROLE_ADMIN", 10028L));
			sysRoleMenuList.add(new SysRoleMenu(571L, "ROLE_ADMIN", 10030L));
			sysRoleMenuList.add(new SysRoleMenu(570L, "ROLE_ADMIN", 10031L));
			sysRoleMenuList.add(new SysRoleMenu(573L, "ROLE_ADMIN", 10032L));
			sysRoleMenuList.add(new SysRoleMenu(574L, "ROLE_ADMIN", 10034L));
			sysRoleMenuList.add(new SysRoleMenu(575L, "ROLE_ADMIN", 10035L));
			sysRoleMenuList.add(new SysRoleMenu(578L, "ROLE_ADMIN", 100000L));
			sysRoleMenuList.add(new SysRoleMenu(577L, "ROLE_ADMIN", 100100L));
			sysRoleMenuList.add(new SysRoleMenu(576L, "ROLE_ADMIN", 100101L));
			sysRoleMenuList.add(new SysRoleMenu(579L, "ROLE_ADMIN", 100102L));
			sysRoleMenuList.add(new SysRoleMenu(580L, "ROLE_ADMIN", 100103L));
			sysRoleMenuList.add(new SysRoleMenu(581L, "ROLE_ADMIN", 100104L));
			sysRoleMenuList.add(new SysRoleMenu(582L, "ROLE_ADMIN", 100105L));
			sysRoleMenuList.add(new SysRoleMenu(583L, "ROLE_ADMIN", 100106L));
			sysRoleMenuList.add(new SysRoleMenu(585L, "ROLE_ADMIN", 100200L));
			sysRoleMenuList.add(new SysRoleMenu(584L, "ROLE_ADMIN", 100201L));
			sysRoleMenuList.add(new SysRoleMenu(586L, "ROLE_ADMIN", 100202L));
			sysRoleMenuList.add(new SysRoleMenu(587L, "ROLE_ADMIN", 100203L));
			sysRoleMenuList.add(new SysRoleMenu(588L, "ROLE_ADMIN", 100204L));
			sysRoleMenuList.add(new SysRoleMenu(589L, "ROLE_ADMIN", 100205L));
			sysRoleMenuList.add(new SysRoleMenu(591L, "ROLE_ADMIN", 100400L));
			sysRoleMenuList.add(new SysRoleMenu(590L, "ROLE_ADMIN", 100401L));
			sysRoleMenuList.add(new SysRoleMenu(592L, "ROLE_ADMIN", 100402L));
			sysRoleMenuList.add(new SysRoleMenu(593L, "ROLE_ADMIN", 100403L));
			sysRoleMenuList.add(new SysRoleMenu(594L, "ROLE_ADMIN", 100404L));
			sysRoleMenuList.add(new SysRoleMenu(596L, "ROLE_ADMIN", 100500L));
			sysRoleMenuList.add(new SysRoleMenu(595L, "ROLE_ADMIN", 100501L));
			sysRoleMenuList.add(new SysRoleMenu(597L, "ROLE_ADMIN", 100502L));
			sysRoleMenuList.add(new SysRoleMenu(598L, "ROLE_ADMIN", 100503L));
			sysRoleMenuList.add(new SysRoleMenu(599L, "ROLE_ADMIN", 100504L));
			sysRoleMenuList.add(new SysRoleMenu(601L, "ROLE_ADMIN", 100700L));
			sysRoleMenuList.add(new SysRoleMenu(600L, "ROLE_ADMIN", 100701L));
			sysRoleMenuList.add(new SysRoleMenu(602L, "ROLE_ADMIN", 100702L));
			sysRoleMenuList.add(new SysRoleMenu(603L, "ROLE_ADMIN", 100703L));
			sysRoleMenuList.add(new SysRoleMenu(604L, "ROLE_ADMIN", 100704L));
			sysRoleMenuList.add(new SysRoleMenu(605L, "ROLE_ADMIN", 100705L));
			sysRoleMenuList.add(new SysRoleMenu(607L, "ROLE_ADMIN", 100800L));
			sysRoleMenuList.add(new SysRoleMenu(606L, "ROLE_ADMIN", 100801L));
			sysRoleMenuList.add(new SysRoleMenu(608L, "ROLE_ADMIN", 100802L));
			sysRoleMenuList.add(new SysRoleMenu(609L, "ROLE_ADMIN", 100803L));
			sysRoleMenuList.add(new SysRoleMenu(610L, "ROLE_ADMIN", 100804L));
			sysRoleMenuList.add(new SysRoleMenu(613L, "ROLE_ADMIN", 110000L));
			sysRoleMenuList.add(new SysRoleMenu(612L, "ROLE_ADMIN", 110100L));
			sysRoleMenuList.add(new SysRoleMenu(611L, "ROLE_ADMIN", 110101L));
			sysRoleMenuList.add(new SysRoleMenu(615L, "ROLE_ADMIN", 110200L));
			sysRoleMenuList.add(new SysRoleMenu(614L, "ROLE_ADMIN", 110201L));
			sysRoleMenuList.add(new SysRoleMenu(617L, "ROLE_ADMIN", 110300L));
			sysRoleMenuList.add(new SysRoleMenu(616L, "ROLE_ADMIN", 110301L));
			sysRoleMenuList.add(new SysRoleMenu(620L, "ROLE_ADMIN", 120000L));
			sysRoleMenuList.add(new SysRoleMenu(619L, "ROLE_ADMIN", 120100L));
			sysRoleMenuList.add(new SysRoleMenu(618L, "ROLE_ADMIN", 120101L));
			sysRoleMenuList.add(new SysRoleMenu(621L, "ROLE_ADMIN", 120102L));
			sysRoleMenuList.add(new SysRoleMenu(622L, "ROLE_ADMIN", 120103L));
			sysRoleMenuList.add(new SysRoleMenu(623L, "ROLE_ADMIN", 120104L));
			sysRoleMenuList.add(new SysRoleMenu(625L, "ROLE_ADMIN", 120200L));
			sysRoleMenuList.add(new SysRoleMenu(624L, "ROLE_ADMIN", 120201L));
			sysRoleMenuList.add(new SysRoleMenu(632L, "ROLE_ADMIN", 660000L));
			sysRoleMenuList.add(new SysRoleMenu(627L, "ROLE_ADMIN", 990000L));
			sysRoleMenuList.add(new SysRoleMenu(626L, "ROLE_ADMIN", 990100L));
			sysRoleMenuList.add(new SysRoleMenu(628L, "ROLE_ADMIN", 990200L));
			sysRoleMenuList.add(new SysRoleMenu(629L, "ROLE_ADMIN", 990300L));
			sysRoleMenuList.add(new SysRoleMenu(630L, "ROLE_ADMIN", 990400L));
			sysRoleMenuList.add(new SysRoleMenu(631L, "ROLE_ADMIN", 990500L));
			sysRoleMenuList.add(new SysRoleMenu(279L, "ROLE_SALES_EXECUTIVE", 10L));
			sysRoleMenuList.add(new SysRoleMenu(280L, "ROLE_SALES_EXECUTIVE", 10L));
			sysRoleMenuList.add(new SysRoleMenu(281L, "ROLE_SALES_EXECUTIVE", 10L));
			sysRoleMenuList.add(new SysRoleMenu(282L, "ROLE_SALES_EXECUTIVE", 10L));
			sysRoleMenuList.add(new SysRoleMenu(283L, "ROLE_SALES_EXECUTIVE", 10L));
			sysRoleMenuList.add(new SysRoleMenu(284L, "ROLE_SALES_EXECUTIVE", 10L));
			sysRoleMenuList.add(new SysRoleMenu(285L, "ROLE_SALES_EXECUTIVE", 78L));
			sysRoleMenuList.add(new SysRoleMenu(286L, "ROLE_SALES_EXECUTIVE", 79L));
			sysRoleMenuList.add(new SysRoleMenu(635L, "ROLE_TEST", 10028L));
			sysRoleMenuList.add(new SysRoleMenu(634L, "ROLE_TEST", 10030L));
			sysRoleMenuList.add(new SysRoleMenu(633L, "ROLE_TEST", 10031L));
			sysRoleMenuList.add(new SysRoleMenu(636L, "ROLE_TEST", 10032L));
			sysRoleMenuList.add(new SysRoleMenu(673L, "ROLE_TEST", 10034L));
			sysRoleMenuList.add(new SysRoleMenu(674L, "ROLE_TEST", 10035L));
			sysRoleMenuList.add(new SysRoleMenu(638L, "ROLE_TEST", 100000L));
			sysRoleMenuList.add(new SysRoleMenu(639L, "ROLE_TEST", 100100L));
			sysRoleMenuList.add(new SysRoleMenu(640L, "ROLE_TEST", 100101L));
			sysRoleMenuList.add(new SysRoleMenu(641L, "ROLE_TEST", 100102L));
			sysRoleMenuList.add(new SysRoleMenu(642L, "ROLE_TEST", 100103L));
			sysRoleMenuList.add(new SysRoleMenu(643L, "ROLE_TEST", 100104L));
			sysRoleMenuList.add(new SysRoleMenu(644L, "ROLE_TEST", 100105L));
			sysRoleMenuList.add(new SysRoleMenu(645L, "ROLE_TEST", 100106L));
			sysRoleMenuList.add(new SysRoleMenu(646L, "ROLE_TEST", 100200L));
			sysRoleMenuList.add(new SysRoleMenu(647L, "ROLE_TEST", 100201L));
			sysRoleMenuList.add(new SysRoleMenu(648L, "ROLE_TEST", 100202L));
			sysRoleMenuList.add(new SysRoleMenu(649L, "ROLE_TEST", 100203L));
			sysRoleMenuList.add(new SysRoleMenu(650L, "ROLE_TEST", 100204L));
			sysRoleMenuList.add(new SysRoleMenu(651L, "ROLE_TEST", 100205L));
			sysRoleMenuList.add(new SysRoleMenu(652L, "ROLE_TEST", 100400L));
			sysRoleMenuList.add(new SysRoleMenu(653L, "ROLE_TEST", 100401L));
			sysRoleMenuList.add(new SysRoleMenu(654L, "ROLE_TEST", 100402L));
			sysRoleMenuList.add(new SysRoleMenu(655L, "ROLE_TEST", 100403L));
			sysRoleMenuList.add(new SysRoleMenu(656L, "ROLE_TEST", 100404L));
			sysRoleMenuList.add(new SysRoleMenu(657L, "ROLE_TEST", 100500L));
			sysRoleMenuList.add(new SysRoleMenu(658L, "ROLE_TEST", 100501L));
			sysRoleMenuList.add(new SysRoleMenu(659L, "ROLE_TEST", 100502L));
			sysRoleMenuList.add(new SysRoleMenu(660L, "ROLE_TEST", 100503L));
			sysRoleMenuList.add(new SysRoleMenu(661L, "ROLE_TEST", 100504L));
			sysRoleMenuList.add(new SysRoleMenu(662L, "ROLE_TEST", 100700L));
			sysRoleMenuList.add(new SysRoleMenu(663L, "ROLE_TEST", 100701L));
			sysRoleMenuList.add(new SysRoleMenu(664L, "ROLE_TEST", 100702L));
			sysRoleMenuList.add(new SysRoleMenu(665L, "ROLE_TEST", 100703L));
			sysRoleMenuList.add(new SysRoleMenu(666L, "ROLE_TEST", 100704L));
			sysRoleMenuList.add(new SysRoleMenu(667L, "ROLE_TEST", 100705L));
			sysRoleMenuList.add(new SysRoleMenu(668L, "ROLE_TEST", 100800L));
			sysRoleMenuList.add(new SysRoleMenu(669L, "ROLE_TEST", 100801L));
			sysRoleMenuList.add(new SysRoleMenu(670L, "ROLE_TEST", 100802L));
			sysRoleMenuList.add(new SysRoleMenu(671L, "ROLE_TEST", 100803L));
			sysRoleMenuList.add(new SysRoleMenu(672L, "ROLE_TEST", 100804L));
			sysRoleMenuList.add(new SysRoleMenu(637L, "ROLE_TEST", 660000L));
			sysRoleMenuList.add(new SysRoleMenu(675L, "ROLE_TEST", 990000L));
			sysRoleMenuList.add(new SysRoleMenu(676L, "ROLE_TEST", 990100L));
			sysRoleMenuList.add(new SysRoleMenu(677L, "ROLE_TEST", 990200L));
			sysRoleMenuList.add(new SysRoleMenu(678L, "ROLE_TEST", 990300L));
			sysRoleMenuList.add(new SysRoleMenu(679L, "ROLE_TEST", 990400L));
			sysRoleMenuList.add(new SysRoleMenu(680L, "ROLE_TEST", 990500L));

		}
		if (sysOrganizationList == null) {
			sysOrganizationList = new ArrayList<>();
			sysOrganizationList.add(new SysOrganization(6L, "高大上公司", 0L, "0", 1, 1, "一个神秘的组织"));
			sysOrganizationList.add(new SysOrganization(7L, "产品研发部", 0L, "0", 1, 1, "一个🐂皮的部门"));
			sysOrganizationList.add(new SysOrganization(8L, "java开发一组", 7L, "0-7", 2, 1, null));
			sysOrganizationList.add(new SysOrganization(9L, "Java开发二组", 7L, "0-7", 2, 2, null));
			sysOrganizationList.add(new SysOrganization(10L, "谷歌", 6L, "0-6", 2, 1, null));
			sysOrganizationList.add(new SysOrganization(11L, "不会Ollie", 10L, "0-6-10", 3, 1, null));
			sysOrganizationList.add(new SysOrganization(12L, "treflip高手", 10L, "0-6-10", 3, 2, null));
			sysOrganizationList.add(new SysOrganization(13L, "impossible", 10L, "0-6-10", 3, 2, null));
			sysOrganizationList.add(new SysOrganization(14L, "测试", 12L, "0-6-10-12", 4, 1, null));
		}
		if (sysConfigList == null) {
			sysConfigList = new ArrayList<>();
			sysConfigList.add(new SysConfig(1L, "网站弹窗开关", "site_popup", "1233", "group", "宣传网站是否弹出框的控制开关。 1：开启 0：关闭"));
		}
		if (sysDictList == null) {
			sysDictList = new ArrayList<>();
			sysDictList.add(new SysDict(2L, "log_status", "日志状态", 1, "b3b0d5919e4a46ffa6dd17d9d7799c58", "正常、异常"));
			sysDictList.add(new SysDict(3L, "gender", "性别", 1, "ae18a6a3e6744f10bd35dc71867edf8d", "用户性别"));
			sysDictList.add(new SysDict(4L, "grant_types", "授权类型", 1, "e5316daadb490e9ca7e1ac5c5607a4", "OAuth授权类型"));
			sysDictList.add(new SysDict(5L, "operation_type", "操作类型", 1, "6b375bba88f649caa38e95d8e5c5c5c9", "操作日志的操作类型"));
			sysDictList.add(new SysDict(6L, "role_type", "角色类型", 1, "90875dc38a154b9d810e8556f8972d9b", "角色类型，系统保留角色不允许删除"));
			sysDictList.add(new SysDict(7L, "dict_value_type", "字典数据类型", 1, "886c8965bdaa4c1e91ffcd5fb20ea84f", "Number、String、Boolean"));
			sysDictList.add(new SysDict(8L, "login_event_type", "登陆事件类型", 1, "56f191fa64ad42b5948e9dcb331cded9", "1：登陆  2：登出"));
			sysDictList.add(new SysDict(9L, "yes_or_no", "是否", 1, "aa22893ca4d243cb8eb198e0f7d66824", null));
			sysDictList.add(new SysDict(13L, "user_type", "用户类型", 1, "a2730f33f24045578ebe7786282e1955", "用户类型"));
			sysDictList.add(new SysDict(14L, "recipient_filter_type", "消息接收人筛选方式", 1, "54f95affca9e4c53aa679bca2855351f", "接收人筛选方式"));
			sysDictList.add(new SysDict(16L, "menu_type", "菜单类型", 1, "88b77280a299482a8e58fbc5fcc065a3", "系统菜单的类型"));
			sysDictList.add(new SysDict(15L, "notify_channel", "通知渠道", 1, "b1d33b2d410b4214920f67c01af80f2f", "通知渠道"));
			sysDictList.add(new SysDict(17L, "user_status", "用户状态", 1, "9527", null));
		}
		if (sysDictItemList == null) {
			sysDictItemList = new ArrayList<>();
			sysDictItemList.add(new SysDictItem(3L, "log_status", "1", "正常", 1, jsonToMap("{\"tagColor\": \"\", \"languages\": {\"en-US\": \"Normal \", \"zh-CN\": \"正常\"}, \"textColor\": \"#34890A\"}"), 0, ""));
			sysDictItemList.add(new SysDictItem(4L, "log_status", "0", "异常", 1, jsonToMap("{\"tagColor\": \"\", \"languages\": {\"en-US\": \"Error\", \"zh-CN\": \"异常\"}, \"textColor\": \"#FF0000\"}"), 1, ""));
			sysDictItemList.add(new SysDictItem(5L, "gender", "1", "男", 1, jsonToMap("{\"tagColor\": \"\", \"languages\": {\"en-US\": \"Male\", \"zh-CN\": \"男\"}, \"textColor\": \"\"}"), 0, ""));
			sysDictItemList.add(new SysDictItem(6L, "gender", "2", "女", 1, jsonToMap("{\"tagColor\": \"\", \"languages\": {\"en-US\": \"Female\", \"zh-CN\": \"女\"}, \"textColor\": \"\"}"), 1, ""));
			sysDictItemList.add(new SysDictItem(7L, "gender", "3", "未知", 1, jsonToMap("{\"tagColor\": \"\", \"languages\": {\"en-US\": \"Unknown\", \"zh-CN\": \"未知\"}, \"textColor\": \"\"}"), 2, ""));
			sysDictItemList.add(new SysDictItem(8L, "grant_types", "password", "密码模式", 1, jsonToMap("{}"), 0, null));
			sysDictItemList.add(new SysDictItem(9L, "grant_types", "authorization_code", "授权码模式", 1, jsonToMap("{}"), 1, null));
			sysDictItemList.add(new SysDictItem(10L, "grant_types", "client_credentials", "客户端模式", 1, jsonToMap("{}"), 2, null));
			sysDictItemList.add(new SysDictItem(11L, "grant_types", "refresh_token", "刷新模式", 1, jsonToMap("{}"), 3, null));
			sysDictItemList.add(new SysDictItem(12L, "grant_types", "implicit", "简化模式", 1, jsonToMap("{}"), 4, null));
			sysDictItemList.add(new SysDictItem(13L, "login_event_type", "1", "登陆", 1, jsonToMap("{\"tagColor\": \"cyan\", \"languages\": {\"en-US\": \"Login\", \"zh-CN\": \"登陆\"}, \"textColor\": \"\"}"), 0, ""));
			sysDictItemList.add(new SysDictItem(14L, "login_event_type", "2", "登出", 1, jsonToMap("{\"tagColor\": \"pink\", \"languages\": {\"en-US\": \"Logout\", \"zh-CN\": \"登出\"}, \"textColor\": \"\"}"), 1, ""));
			sysDictItemList.add(new SysDictItem(15L, "operation_type", "3", "查看", 1, jsonToMap("{\"tagColor\": \"purple\", \"languages\": {\"en-US\": \"Read\", \"zh-CN\": \"查看\"}, \"textColor\": \"\"}"), 3, ""));
			sysDictItemList.add(new SysDictItem(16L, "operation_type", "4", "新建", 1, jsonToMap("{\"tagColor\": \"cyan\", \"languages\": {\"en-US\": \"Create\", \"zh-CN\": \"新建\"}, \"textColor\": \"\"}"), 4, ""));
			sysDictItemList.add(new SysDictItem(17L, "operation_type", "5", "修改", 1, jsonToMap("{\"tagColor\": \"orange\", \"languages\": {\"en-US\": \"Update\", \"zh-CN\": \"修改\"}, \"textColor\": \"\"}"), 5, ""));
			sysDictItemList.add(new SysDictItem(18L, "operation_type", "6", "删除", 1, jsonToMap("{\"tagColor\": \"pink\", \"languages\": {\"en-US\": \"Delete\", \"zh-CN\": \"删除\"}, \"textColor\": \"\"}"), 6, ""));
			sysDictItemList.add(new SysDictItem(19L, "role_type", "1", "系统", 1, jsonToMap("{\"tagColor\": \"orange\", \"languages\": {\"en-US\": \"System\", \"zh-CN\": \"系统\"}, \"textColor\": \"\"}"), 1, "系统角色不能删除"));
			sysDictItemList.add(new SysDictItem(20L, "role_type", "2", "自定义", 1, jsonToMap("{\"tagColor\": \"green\", \"languages\": {\"en-US\": \"Custom\", \"zh-CN\": \"自定义\"}, \"textColor\": \"\"}"), 2, "自定义角色可以删除"));
			sysDictItemList.add(new SysDictItem(21L, "dict_type", "1", "Number", 1, jsonToMap("{}"), 1, null));
			sysDictItemList.add(new SysDictItem(22L, "dict_type", "2", "String", 1, jsonToMap("{}"), 1, null));
			sysDictItemList.add(new SysDictItem(23L, "dict_type", "3", "Boolean", 1, jsonToMap("{}"), 1, null));
			sysDictItemList.add(new SysDictItem(24L, "dict_value_type", "1", "Number", 1, jsonToMap("{}"), 1, null));
			sysDictItemList.add(new SysDictItem(25L, "dict_value_type", "2", "String", 1, jsonToMap("{}"), 1, null));
			sysDictItemList.add(new SysDictItem(26L, "dict_value_type", "3", "Boolean", 1, jsonToMap("{}"), 1, null));
			sysDictItemList.add(new SysDictItem(27L, "yes_or_no", "1", "是", 1, jsonToMap("{\"tagColor\": \"\", \"languages\": {\"en-US\": \"Yes\", \"zh-CN\": \"是\"}, \"textColor\": \"\"}"), 1, null));
			sysDictItemList.add(new SysDictItem(28L, "yes_or_no", "0", "否", 1, jsonToMap("{\"tagColor\": \"\", \"languages\": {\"en-US\": \"No\", \"zh-CN\": \"否\"}, \"textColor\": \"\"}"), 2, null));
			sysDictItemList.add(new SysDictItem(49L, "user_type", "1", "系统用户", 1, jsonToMap("{\"tagColor\": \"\", \"languages\": {\"en-US\": \"System User\", \"zh-CN\": \"系统用户\"}, \"textColor\": \"\"}"), 1, null));
			sysDictItemList.add(new SysDictItem(50L, "recipient_filter_type", "1", "全部", 1, jsonToMap("{\"tagColor\": \"\", \"languages\": {\"en-US\": \"All\", \"zh-CN\": \"全部\"}, \"textColor\": \"\"}"), 1, "不筛选，对全部用户发送"));
			sysDictItemList.add(new SysDictItem(51L, "recipient_filter_type", "2", "指定角色", 1, jsonToMap("{\"tagColor\": \"\", \"languages\": {\"en-US\": \"Specify the role\", \"zh-CN\": \"指定角色\"}, \"textColor\": \"\"}"), 2, "筛选拥有指定角色的用户"));
			sysDictItemList.add(new SysDictItem(52L, "recipient_filter_type", "3", "指定组织", 1, jsonToMap("{\"tagColor\": \"\", \"languages\": {\"en-US\": \"Specify the organization\", \"zh-CN\": \"指定组织\"}, \"textColor\": \"\"}"), 3, "筛选指定组织的用户"));
			sysDictItemList.add(new SysDictItem(53L, "recipient_filter_type", "4", "指定类型", 1, jsonToMap("{\"tagColor\": \"\", \"languages\": {\"en-US\": \"Specify the type\", \"zh-CN\": \"指定类型\"}, \"textColor\": \"\"}"), 4, "筛选指定用户类型的用户"));
			sysDictItemList.add(new SysDictItem(54L, "recipient_filter_type", "5", "指定用户", 1, jsonToMap("{\"tagColor\": \"\", \"languages\": {\"en-US\": \"Specify the user\", \"zh-CN\": \"指定用户\"}, \"textColor\": \"\"}"), 5, "指定用户发送"));
			sysDictItemList.add(new SysDictItem(55L, "notify_channel", "1", "站内", 1, jsonToMap("{\"tagColor\": \"\", \"languages\": {\"en-US\": \"Station\", \"zh-CN\": \"站内\"}, \"textColor\": \"\"}"), 1, null));
			sysDictItemList.add(new SysDictItem(56L, "notify_channel", "2", "短信", 1, jsonToMap("{\"tagColor\": \"\", \"languages\": {\"en-US\": \"SMS\", \"zh-CN\": \"短信\"}, \"textColor\": \"\"}"), 2, null));
			sysDictItemList.add(new SysDictItem(57L, "notify_channel", "3", "邮箱", 1, jsonToMap("{\"tagColor\": \"\", \"languages\": {\"en-US\": \"Email\", \"zh-CN\": \"邮箱\"}, \"textColor\": \"\"}"), 3, null));
			sysDictItemList.add(new SysDictItem(59L, "menu_type", "0", "目录", 1, jsonToMap("{\"tagColor\": \"\", \"languages\": {\"en-US\": \"Catalog\", \"zh-CN\": \"目录\"}, \"textColor\": \"\"}"), 1, null));
			sysDictItemList.add(new SysDictItem(60L, "menu_type", "1", "菜单", 1, jsonToMap("{\"tagColor\": \"\", \"languages\": {\"en-US\": \"Menu\", \"zh-CN\": \"菜单\"}, \"textColor\": \"\"}"), 2, null));
			sysDictItemList.add(new SysDictItem(61L, "menu_type", "2", "按钮", 1, jsonToMap("{\"tagColor\": \"\", \"languages\": {\"en-US\": \"Button\", \"zh-CN\": \"按钮\"}, \"textColor\": \"\"}"), 3, null));
			sysDictItemList.add(new SysDictItem(62L, "operation_type", "0", "其他", 1, jsonToMap("{\"tagColor\": \"\", \"languages\": {\"en-US\": \"Other\", \"zh-CN\": \"其他\"}, \"textColor\": \"\"}"), 0, null));
			sysDictItemList.add(new SysDictItem(63L, "operation_type", "1", "导入", 1, jsonToMap("{\"tagColor\": \"green\", \"languages\": {\"en-US\": \"Import\", \"zh-CN\": \"导入\"}, \"textColor\": \"\"}"), 1, null));
			sysDictItemList.add(new SysDictItem(64L, "operation_type", "2", "导出", 1, jsonToMap("{\"tagColor\": \"blue\", \"languages\": {\"en-US\": \"Export\", \"zh-CN\": \"导出\"}, \"textColor\": \"\"}"), 2, null));
			sysDictItemList.add(new SysDictItem(65L, "user_status", "0", "锁定", 1, jsonToMap("{\"tagColor\": \"#d9d9d9\", \"languages\": {\"en-US\": \"Locked\", \"zh-CN\": \"锁定\"}, \"textColor\": \"#d9d9d9\", \"badgeStatus\": \"default\"}"), 2, null));
			sysDictItemList.add(new SysDictItem(66L, "user_status", "1", "正常", 1, jsonToMap("{\"tagColor\": \"blue\", \"languages\": {\"en-US\": \"Normal\", \"zh-CN\": \"正常\"}, \"textColor\": \"#5b8ff9\", \"badgeStatus\": \"processing\"}"), 1, null));
		}
	}

	public static Map<String, Object> jsonToMap(String json) {
		return JsonUtils.toObj(json, new TypeReference<Map<String, Object>>() {
		});
	}

	public static List<AuthClient> getAuthClientList() {
		return authClientList;
	}

	public static List<SysUser> getSysUserList() {
		return sysUserList;
	}

	public static List<SysRole> getSysRoleList() {
		return sysRoleList;
	}

	public static List<SysMenu> getSysMenuList() {
		return sysMenuList;
	}

	public static List<SysUserRole> getSysUserRoleList() {
		return sysUserRoleList;
	}

	public static List<SysRoleMenu> getSysRoleMenuList() {
		return sysRoleMenuList;
	}

	public static List<SysOrganization> getSysOrganizationList() {
		return sysOrganizationList;
	}

	public static List<SysConfig> getSysConfigList() {
		return sysConfigList;
	}

	public static List<SysDict> getSysDictList() {
		return sysDictList;
	}

	public static List<SysDictItem> getSysDictItemList() {
		return sysDictItemList;
	}

}
