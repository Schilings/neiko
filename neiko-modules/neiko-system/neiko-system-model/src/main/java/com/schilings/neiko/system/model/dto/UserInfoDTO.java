package com.schilings.neiko.system.model.dto;

import com.schilings.neiko.system.model.entity.SysMenu;
import com.schilings.neiko.system.model.entity.SysRole;
import com.schilings.neiko.system.model.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Collection;

@Data
@Schema(title = "用户信息")
public class UserInfoDTO {

	/**
	 * 用户基本信息
	 */
	@Schema(title = "用户基本信息")
	private SysUser sysUser;

	/**
	 * 权限标识集合
	 */
	@Schema(title = "权限标识集合")
	private Collection<String> permissions;

	/**
	 * 角色标识集合
	 */
	@Schema(title = "角色标识集合")
	private Collection<String> roleCodes;

	/**
	 * 菜单对象集合
	 */
	@Schema(title = "菜单对象集合")
	private Collection<SysMenu> menus;

	/**
	 * 角色对象集合
	 */
	@Schema(title = "角色对象集合")
	private Collection<SysRole> roles;

}
