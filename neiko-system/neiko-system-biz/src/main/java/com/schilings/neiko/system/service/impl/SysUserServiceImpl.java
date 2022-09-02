package com.schilings.neiko.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.schilings.neiko.common.core.exception.ServiceException;
import com.schilings.neiko.common.event.publisher.EventBus;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.domain.SelectData;
import com.schilings.neiko.common.model.result.BaseResultCode;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.file.service.FileService;
import com.schilings.neiko.system.checker.AdminstratorChecker;
import com.schilings.neiko.system.constant.SysUserConst;
import com.schilings.neiko.system.converter.SysUserConverter;
import com.schilings.neiko.system.event.UserCreatedEvent;
import com.schilings.neiko.system.event.UserOrganizationChangeEvent;
import com.schilings.neiko.system.mapper.*;
import com.schilings.neiko.system.model.dto.SysUserDTO;
import com.schilings.neiko.system.model.dto.SysUserScope;
import com.schilings.neiko.system.model.dto.UserInfoDTO;
import com.schilings.neiko.system.model.entity.SysMenu;
import com.schilings.neiko.system.model.entity.SysRole;
import com.schilings.neiko.system.model.entity.SysUser;
import com.schilings.neiko.system.model.qo.SysUserQO;
import com.schilings.neiko.system.model.vo.SysUserPageVO;
import com.schilings.neiko.system.security.PasswordHelper;
import com.schilings.neiko.system.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ExtendServiceImpl<SysUserMapper, SysUser> implements SysUserService {

	private final EventBus eventBus;

	private final ApplicationEventPublisher publisher;

	private final FileService fileService;

	private final AdminstratorChecker adminstratorChecker;

	private final PasswordHelper passwordHelper;

	private final SysMenuService sysMenuService;

	private final SysRoleService sysRoleService;

	private final SysUserRoleService sysUserRoleService;

	private final SysRoleMenuService sysRoleMenuService;

	/**
	 * 根据QueryObject查询系统用户列表
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<SysUserVO> 分页数据
	 */
	@Override
	public PageResult<SysUserPageVO> queryPage(PageParam pageParam, SysUserQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

	/**
	 * 根据用户名查询用户
	 * @param username 用户名
	 * @return 系统用户
	 */
	@Override
	public SysUser getByUsername(String username) {
		return baseMapper.selectByUsername(username);
	}

	/**
	 * 获取用户详情信息
	 * @param sysUser
	 * @return UserInfoDTO
	 */
	@Override
	public UserInfoDTO findUserInfo(SysUser sysUser) {
		UserInfoDTO userInfoDTO = new UserInfoDTO();
		userInfoDTO.setSysUser(sysUser);

		// 超级管理员拥有所有角色
		List<SysRole> roleList;
		if (adminstratorChecker.isAdminstrator(sysUser)) {
			roleList = sysRoleService.list();
		}
		else {
			roleList = sysUserRoleService.listRoles(sysUser.getUserId());
		}
		// 设置角色标识
		Set<String> roleCodes = new HashSet<>();
		for (SysRole role : roleList) {
			roleCodes.add(role.getCode());
		}
		userInfoDTO.setRoles(new HashSet<>(roleList));
		userInfoDTO.setRoleCodes(roleCodes);

		// 设置权限列表（permission）
		Set<String> permissions = new HashSet<>();
		Set<SysMenu> menus = new HashSet<>();
		for (String roleCode : roleCodes) {
			List<SysMenu> sysMenuList = sysRoleMenuService.listMenus(roleCode);
			menus.addAll(sysMenuList);
			List<String> permissionList = sysMenuList.stream().map(SysMenu::getPermission).filter(StrUtil::isNotEmpty)
					.collect(Collectors.toList());
			permissions.addAll(permissionList);
		}
		userInfoDTO.setMenus(menus);
		userInfoDTO.setPermissions(permissions);

		return userInfoDTO;
	}

	/**
	 * 新增系统用户
	 * @param sysUserDto SysUserDTO
	 * @return boolean
	 */
	@Override
	public boolean addSysUser(SysUserDTO sysUserDto) {
		SysUser sysUser = SysUserConverter.INSTANCE.dtoToPo(sysUserDto);
		sysUser.setType(SysUserConst.Type.SYSTEM.getValue());
		// 对密码进行加密
		String rawPassword = sysUserDto.getPass();
		String encodedPassword = passwordHelper.encode(rawPassword, null);
		sysUser.setPassword(encodedPassword);

		// 保存用户
		boolean insertSuccess = SqlHelper.retBool(baseMapper.insert(sysUser));
		Assert.isTrue(insertSuccess, () -> {
			log.error("[addSysUser] 数据插入系统用户表失败，user：{}", sysUserDto);
			return new ServiceException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "数据插入系统用户表失败");
		});

		// 新增用户角色关联
		List<String> roleCodes = sysUserDto.getRoleCodes();
		if (CollectionUtil.isNotEmpty(roleCodes)) {
			boolean addUserRoleSuccess = sysUserRoleService.addUserRoles(sysUser.getUserId(), roleCodes);
			Assert.isTrue(addUserRoleSuccess, () -> {
				log.error("[addSysUser] 更新用户角色信息失败，user：{}， roleCodes: {}", sysUserDto, roleCodes);
				return new ServiceException(BaseResultCode.UPDATE_DATABASE_ERROR.getCode(), "更新用户角色信息失败");
			});
		}

		// 发布用户创建事件
		publisher.publishEvent(new UserCreatedEvent(sysUser, sysUserDto.getRoleCodes()));

		return true;
	}

	/**
	 * 更新系统用户信息
	 * @param sysUserDTO 系统用户DTO
	 * @return 更新成功 true: 更新失败 false
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateSysUser(SysUserDTO sysUserDTO) {
		SysUser entity = SysUserConverter.INSTANCE.dtoToPo(sysUserDTO);
		Assert.isTrue(adminstratorChecker.hasModifyPermission(entity), "当前用户不允许修改!");

		// 如果不更新组织，直接执行
		Long currentOrganizationId = entity.getOrganizationId();
		if (currentOrganizationId == null) {
			return SqlHelper.retBool(baseMapper.updateById(entity));
		}

		// 查询出当前库中用户
		Long userId = entity.getUserId();
		SysUser oldUser = baseMapper.selectById(userId);
		Assert.notNull(oldUser, "修改用户失败，当前用户不存在：{}", userId);

		// 是否修改了组织
		Long originOrganizationId = oldUser.getOrganizationId();
		boolean organizationIdModified = !currentOrganizationId.equals(originOrganizationId);
		// 是否更改成功
		boolean isUpdateSuccess = SqlHelper.retBool(baseMapper.updateById(entity));
		// 如果修改了组织且修改成功，则发送用户组织更新事件
		if (isUpdateSuccess && organizationIdModified) {
			publisher
					.publishEvent(new UserOrganizationChangeEvent(userId, originOrganizationId, currentOrganizationId));
		}

		return isUpdateSuccess;
	}

	/**
	 * 更新用户权限信息
	 * @param userId 用户Id
	 * @param sysUserScope 系统用户权限范围
	 * @return 更新成功：true
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateUserScope(Long userId, SysUserScope sysUserScope) {
		// 更新用户角色关联关系
		return sysUserRoleService.updateUserRoles(userId, sysUserScope.getRoleCodes());
	}

	/**
	 * 根据userId删除 用户
	 * @param userId 用户ID
	 * @return 删除成功：true
	 */
	@Override
	public boolean deleteByUserId(Long userId) {
		Assert.isFalse(adminstratorChecker.isAdminstrator(getById(userId)), "管理员不允许删除!");
		return SqlHelper.retBool(baseMapper.deleteById(userId));
	}

	/**
	 * 修改用户密码
	 * @param userId 用户ID
	 * @param rawPassword 明文密码
	 * @return 更新成功：true
	 */
	@Override
	public boolean updatePassword(Long userId, String rawPassword) {
		Assert.isTrue(adminstratorChecker.hasModifyPermission(getById(userId)), "当前用户不允许修改!");
		// 密码加密加密
		String encodedPassword = passwordHelper.encode(rawPassword, null);
		return baseMapper.updatePassword(userId, encodedPassword);
	}

	/**
	 * 批量修改用户状态
	 * @param userIds 用户ID集合
	 * @return 更新成功：true
	 */
	@Override
	public boolean updateUserStatusBatch(Collection<Long> userIds, Integer status) {

		List<SysUser> userList = baseMapper.listByUserIds(userIds);
		Assert.notEmpty(userList, "更新用户状态失败，待更新用户列表为空");

		// 移除无权限更改的用户id
		Map<Long, SysUser> userMap = userList.stream()
				.collect(Collectors.toMap(SysUser::getUserId, Function.identity()));
		userIds.removeIf(id -> !adminstratorChecker.hasModifyPermission(userMap.get(id)));
		Assert.notEmpty(userIds, "更新用户状态失败，无权限更新用户");

		return baseMapper.updateStatusBatch(userIds, status);
	}

	/**
	 * 修改系统用户头像
	 * @param file 头像文件
	 * @param userId 用户ID
	 * @return 文件相对路径
	 * @throws IOException IO异常
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String updateAvatar(MultipartFile file, Long userId) throws IOException {
		Assert.isTrue(adminstratorChecker.hasModifyPermission(getById(userId)), "当前用户不允许修改!");
		// 获取系统用户头像的文件名
		String objectName = "sysuser/" + userId + "/avatar/" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
				+ StrUtil.SLASH + IdUtil.fastSimpleUUID() + StrUtil.DOT + FileUtil.extName(file.getOriginalFilename());
		objectName = fileService.upload(file.getInputStream(), objectName, file.getSize());
		SysUser sysUser = new SysUser();
		sysUser.setUserId(userId);
		sysUser.setAvatar(objectName);
		baseMapper.updateById(sysUser);

		return objectName;
	}

	/**
	 * 根据组织机构ID查询用户
	 * @param organizationIds 组织机构id集合
	 * @return 用户集合
	 */
	public List<SysUser> listByOrganizationIds(Collection<Long> organizationIds) {
		return baseMapper.listByOrganizationIds(organizationIds);
	}

	/**
	 * 根据用户类型查询用户
	 * @param userTypes 用户类型集合
	 * @return 用户集合
	 */
	public List<SysUser> listByUserTypes(Collection<Integer> userTypes) {
		return baseMapper.listByUserTypes(userTypes);
	}

	/**
	 * 根据用户Id集合查询用户
	 * @param userIds 用户Id集合
	 * @return 用户集合
	 */
	public List<SysUser> listByUserIds(Collection<Long> userIds) {
		return baseMapper.listByUserIds(userIds);
	}

	/**
	 * 是否存在指定组织的用户
	 * @param organizationId 组织 id
	 * @return boolean 存在返回 true
	 */
	public boolean existsForOrganization(Long organizationId) {
		return baseMapper.existsForOrganization(organizationId);
	}

	/**
	 * 返回用户的select数据 name=> username value => userId
	 * @return List<SelectData>
	 * @param userTypes 用户类型
	 */
	@Override
	public List<SelectData> listSelectData(Collection<Integer> userTypes) {
		return baseMapper.listSelectData(userTypes);
	}

}
