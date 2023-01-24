package com.schilings.neiko.samples.admin;

import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.notify.mapper.AnnouncementMapper;
import com.schilings.neiko.system.mapper.SysRoleMapper;
import com.schilings.neiko.system.mapper.SysUserMapper;
import com.schilings.neiko.system.mapper.SysUserRoleMapper;
import com.schilings.neiko.system.model.entity.SysRole;
import com.schilings.neiko.system.model.entity.SysUser;
import com.schilings.neiko.system.model.qo.RoleBindUserQO;
import com.schilings.neiko.system.model.qo.SysRoleQO;
import com.schilings.neiko.system.model.qo.SysUserQO;
import com.schilings.neiko.system.model.vo.RoleBindUserVO;
import com.schilings.neiko.system.model.vo.SysRolePageVO;
import com.schilings.neiko.system.model.vo.SysUserPageVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class AdminApplicationTest {

	// private SysUserRoleMapper sysUserRoleMapper;
	//
	// private SysUserMapper sysUserMapper;
	//
	// private SysRoleMapper sysRoleMapper;
	//
	// // SysUser
	//
	//
	// public void SysUser() {
	// SysUser_updatePassword();
	// SysUser_listByOrganizationIds();
	// SysUser_listByUserTypes();
	// SysUser_listByUserIds();
	//
	// }
	//
	// public void SysUser_queryPage() {
	// PageResult<SysUserPageVO> pageResult = sysUserMapper.queryPage(new PageParam(), new
	// SysUserQO());
	// pageResult.getData().forEach(System.out::println);
	// }
	//
	// public void SysUser_selectByUsername() {
	// SysUser sysUser = sysUserMapper.selectByUsername("username1");
	// System.out.println(sysUser);
	// }
	//
	// public void SysUser_updateStatusBatch() {
	// boolean update = sysUserMapper.updateStatusBatch(Arrays.asList(1L, 2L), 0);
	// System.out.println(update);
	// }
	//
	// public void SysUser_updatePassword() {
	// boolean update = sysUserMapper.updatePassword(1L, "updatePass1");
	// System.out.println(update);
	// }
	//
	// public void SysUser_listByOrganizationIds() {
	// List<SysUser> sysUsers = sysUserMapper.listByOrganizationIds(Arrays.asList(1L,
	// 2L));
	// sysUsers.forEach(System.out::println);
	// }
	//
	// public void SysUser_listByUserTypes() {
	// List<SysUser> sysUsers = sysUserMapper.listByUserTypes(Arrays.asList(1, 2));
	// sysUsers.forEach(System.out::println);
	// }
	//
	// public void SysUser_listByUserIds() {
	// List<SysUser> sysUsers = sysUserMapper.listByUserIds(Arrays.asList(1L, 2L));
	// sysUsers.forEach(System.out::println);
	// }
	//
	// // SysRole
	//
	// public void testSysRole() {
	// SysRole_queryPage();
	// }
	//
	// public void SysRole_queryPage() {
	// PageResult<SysRolePageVO> pageResult = sysRoleMapper.queryPage(new PageParam(), new
	// SysRoleQO());
	// pageResult.getData().forEach(System.out::println);
	// }
	//
	// // SysUserRole
	//
	//
	// public void testSysUserRole() {
	// SysUserRole_listRolesByUserId();
	// SysUserRole_listUserByRoleCodes();
	// SysUserRole_queryUserPageByRoleCode();
	// }
	//
	// public void SysUserRole_listRolesByUserId() {
	// List<SysRole> sysRoles = sysUserRoleMapper.listRoleByUserId(1L);
	// sysRoles.forEach(System.out::println);
	// }
	//
	// public void SysUserRole_listUserByRoleCodes() {
	// List<SysUser> sysUsers =
	// sysUserRoleMapper.listUserByRoleCodes(Arrays.asList("ADMIN", "NORMAL"));
	// sysUsers.forEach(System.out::println);
	// }
	//
	// public void SysUserRole_queryUserPageByRoleCode() {
	// RoleBindUserQO qo = new RoleBindUserQO();
	// qo.setRoleCode("NORMAL");
	// PageResult<RoleBindUserVO> pageResult =
	// sysUserRoleMapper.queryUserPageByRoleCode(new PageParam(), qo);
	// pageResult.getData().forEach(System.out::println);
	// }
	//
	//
	// private AnnouncementMapper announcementMapper;
	//
	//
	// public void testNotify() {
	// /**
	// * SELECT t.id, t.title, t.content, t.recipient_filter_type,
	// * t.recipient_filter_condition, t.receive_mode, t.status, t.immortal, t.deadline,
	// * t.deleted, t.create_by, t.update_by, t.create_time, t.update_time FROM
	// * notify_announcement t LEFT JOIN notify_user_announcement t1 ON
	// * (t1.announcement_id = t.id) WHERE (t.deleted = 0 AND t.status = 1 AND
	// * t.immortal = 1 AND t1.id IS NOT NULL)
	// */
	// announcementMapper.listUserAnnouncements(1L, true);
	// }

}
