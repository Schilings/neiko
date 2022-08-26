package com.schilings.neiko.admin.boostrap;

import com.schilings.neiko.auth.model.entity.AuthClient;
import com.schilings.neiko.system.model.entity.*;

import java.util.ArrayList;
import java.util.List;

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
					"15800000000", 1, 1, 6));
			sysUserList.add(new SysUser(2L, "test", "测试用户213", "51f5c2f36d9803ab55d4ba5cee9a3e48", "",
					"sysuser/10/avatar/20201204/002875d468db41239ee02ad99ab14490.jpg", 2, "magic.xiaohua@gmail.com",
					"12345678520", 0, 1, 6));
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
			sysMenuList.add(new SysMenu(100000L, 0L, "系统管理", "setting", null, "system", 1, null, 1, 0, 0, 0, null));
			sysMenuList.add(new SysMenu(100100L, 100000L, "系统用户", null, null, "user", 1, "system/user/SysUserPage", 1,
					0, 0, 1, null));
			sysMenuList.add(
					new SysMenu(100101L, 100100L, "系统用户查询", null, "system:user:read", null, 1, null, 1, 0, 0, 2, null));
			sysMenuList.add(
					new SysMenu(100102L, 100100L, "系统用户新增", null, "system:user:add", null, 1, null, 1, 0, 0, 2, null));
			sysMenuList.add(
					new SysMenu(100103L, 100100L, "系统用户修改", null, "system:user:edit", null, 1, null, 2, 0, 0, 2, null));
			sysMenuList.add(
					new SysMenu(100104L, 100100L, "系统用户删除", null, "system:user:del", null, 1, null, 3, 0, 0, 2, null));
			sysMenuList.add(new SysMenu(100105L, 100100L, "系统用户授权", null, "system:user:grant", null, 1, null, 3, 0, 0,
					2, null));
			sysMenuList.add(
					new SysMenu(100106L, 100100L, "系统用户改密", null, "system:user:pass", null, 1, null, 4, 0, 0, 2, null));
		}
		if (sysUserRoleList == null) {
			sysUserRoleList = new ArrayList<>();
			sysUserRoleList.add(new SysUserRole(1L, 1L, "ROLE_ADMIN"));
			sysUserRoleList.add(new SysUserRole(6L, 14L, "ROLE_SALES_EXECUTIVE"));
			sysUserRoleList.add(new SysUserRole(4L, 1L, "ROLE_TEST"));
		}
		if (sysRoleMenuList == null) {
			sysRoleMenuList = new ArrayList<>();
			sysRoleMenuList.add(new SysRoleMenu(578L, "ROLE_ADMIN", 100000L));
			sysRoleMenuList.add(new SysRoleMenu(577L, "ROLE_ADMIN", 100100L));
			sysRoleMenuList.add(new SysRoleMenu(576L, "ROLE_ADMIN", 100101L));
			sysRoleMenuList.add(new SysRoleMenu(579L, "ROLE_ADMIN", 100102L));
			sysRoleMenuList.add(new SysRoleMenu(580L, "ROLE_ADMIN", 100103L));
			sysRoleMenuList.add(new SysRoleMenu(581L, "ROLE_ADMIN", 100104L));
			sysRoleMenuList.add(new SysRoleMenu(582L, "ROLE_ADMIN", 100105L));
			sysRoleMenuList.add(new SysRoleMenu(583L, "ROLE_ADMIN", 100106L));
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

}
