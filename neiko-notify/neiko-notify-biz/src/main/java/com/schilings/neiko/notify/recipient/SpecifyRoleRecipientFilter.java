package com.schilings.neiko.notify.recipient;

import cn.hutool.core.collection.CollectionUtil;

import com.schilings.neiko.notify.enums.NotifyRecipientFilterTypeEnum;
import com.schilings.neiko.system.model.entity.SysUser;
import com.schilings.neiko.system.service.SysUserRoleService;
import com.schilings.neiko.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class SpecifyRoleRecipientFilter implements RecipientFilter {
	

	private final SysUserRoleService sysUserRoleService;

	/**
	 * 当前筛选器对应的筛选类型
	 * @return 筛选类型对应的标识
	 * @see NotifyRecipientFilterTypeEnum
	 */
	@Override
	public Integer filterType() {
		return NotifyRecipientFilterTypeEnum.SPECIFY_ROLE.getValue();
	}

	/**
	 * 接收者筛选
	 * @param filterCondition 筛选条件
	 * @return 接收者集合
	 */
	@Override
	public List<SysUser> filter(List<Object> filterCondition) {
		List<String> roleCodes = filterCondition.stream().map(String.class::cast).collect(Collectors.toList());
		return sysUserRoleService.listUsers(roleCodes);
	}

	/**
	 * 获取当前用户的过滤属性
	 * @param sysUser 系统用户
	 * @return 该用户所对应筛选条件的属性
	 */
	@Override
	public Object getFilterAttr(SysUser sysUser) {
		return sysUserRoleService.listRoleCodes(sysUser.getUserId());
	}

	/**
	 * 是否匹配当前用户
	 * @param filterAttr 筛选属性
	 * @param filterCondition 筛选条件
	 * @return boolean true: 是否匹配
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean match(Object filterAttr, List<Object> filterCondition) {
		if (!(filterAttr instanceof List)) {
			return false;
		}
		List<String> roleCodes = (List<String>) filterAttr;
		if (CollectionUtil.isEmpty(roleCodes)) {
			return false;
		}
		for (Object roleCode : roleCodes) {
			boolean matched = filterCondition.stream().map(String.class::cast).anyMatch(x -> x.equals(roleCode));
			if (matched) {
				return true;
			}
		}
		return false;
	}

}
