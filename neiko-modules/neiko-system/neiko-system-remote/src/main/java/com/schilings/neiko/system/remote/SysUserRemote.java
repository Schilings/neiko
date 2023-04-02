package com.schilings.neiko.system.remote;

import com.schilings.neiko.common.core.http.RequestParameterObject;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.domain.SelectData;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.system.model.dto.SysUserDTO;
import com.schilings.neiko.system.model.dto.SysUserPassDTO;
import com.schilings.neiko.system.model.dto.SysUserScope;
import com.schilings.neiko.system.model.qo.SysUserQO;
import com.schilings.neiko.system.model.vo.SysUserInfo;
import com.schilings.neiko.system.model.vo.SysUserPageVO;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.WritableResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.*;

import java.io.File;
import java.io.InputStream;
import java.util.List;

@HttpExchange("/system/user")
public interface SysUserRemote {

	/**
	 * 分页查询用户
	 * @param pageParam 参数集
	 * @return 用户集合
	 */
	@GetExchange("/page")
	R<PageResult<SysUserPageVO>> getUserPage(PageParam pageParam, @RequestParameterObject SysUserQO sysUserQO);

	/**
	 * 获取指定用户的基本信息
	 * @param userId 用户ID
	 * @return SysUserInfo
	 */
	@GetExchange("/{userId}")
	R<SysUserInfo> getSysUserInfo(@PathVariable("userId") Long userId);

	/**
	 * 获取用户 所拥有的角色ID
	 * @param userId userId
	 */
	@GetExchange("/scope/{userId}")
	R<SysUserScope> getUserRoleIds(@PathVariable("userId") Long userId);

	/**
	 * 新增用户
	 * @param sysUserDTO userInfo
	 * @return success/false
	 */
	@PostExchange
	R<Void> addSysUser(@RequestBody SysUserDTO sysUserDTO);

	/**
	 * 修改用户个人信息
	 * @param sysUserDto userInfo
	 * @return success/false
	 */
	@PutExchange
	R<Void> updateUserInfo(@RequestBody SysUserDTO sysUserDto);

	/**
	 * 删除用户信息
	 */
	@DeleteExchange("/{userId}")
	R<Void> deleteByUserId(@PathVariable("userId") Long userId);

	/**
	 * 修改用户权限信息 比如角色 数据权限等
	 * @param sysUserScope sysUserScope
	 * @return success/false
	 */
	@PutExchange("/scope/{userId}")
	R<Void> updateUserScope(@PathVariable("userId") Long userId, @RequestBody SysUserScope sysUserScope);

	/**
	 * 修改用户密码
	 */
	@PutExchange("/pass/{userId}")
	R<Void> updateUserPass(@PathVariable("userId") Long userId, @RequestBody SysUserPassDTO sysUserPassDTO);

	/**
	 * 批量修改用户状态
	 */
	@PutExchange("/status")
	R<Void> updateUserStatus(@RequestBody List<Long> userIds, @RequestParam("status") Integer status);

	/**
	 * 注意：进行上传下载最好限制web服务器的文件大小 上传头像，@RequestPart与@RequestParam不要同时使用，不然只会解析@RequestParam
	 * @param file
	 * @param userId
	 * @return
	 */
	@PostExchange(url = "/avatar", contentType = MediaType.MULTIPART_FORM_DATA_VALUE)
	R<String> updateAvatar(@RequestPart("file") WritableResource file, @RequestPart("userId") Long userId);

	/**
	 * 获取用户Select
	 * @return 用户SelectData
	 */
	@GetExchange("/select")
	R<List<SelectData>> listSelectData(@RequestParam(value = "userTypes", required = false) List<Integer> userTypes);

}
