package com.schilings.neiko.notify.recipient;

import com.schilings.neiko.notify.enums.NotifyRecipientFilterTypeEnum;
import com.schilings.neiko.system.model.entity.SysUser;
import com.schilings.neiko.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SpecifyOrganizationRecipientFilter implements RecipientFilter {

	private final SysUserService sysUserService;

	/**
	 * 当前筛选器对应的筛选类型
	 * @return 筛选类型对应的标识
	 * @see NotifyRecipientFilterTypeEnum
	 */
	@Override
	public Integer filterType() {
		return NotifyRecipientFilterTypeEnum.SPECIFY_ORGANIZATION.getValue();
	}

	/**
	 * 接收者筛选
	 * @param filterCondition 筛选条件
	 * @return 接收者集合
	 */
	@Override
	public List<SysUser> filter(List<Object> filterCondition) {
		List<Long> organizationIds = filterCondition.stream().map(Long.class::cast).collect(Collectors.toList());
		return sysUserService.listByOrganizationIds(organizationIds);
	}

	/**
	 * 获取当前用户的过滤属性
	 * @param sysUser 系统用户
	 * @return 该用户所对应筛选条件的属性
	 */
	@Override
	public Object getFilterAttr(SysUser sysUser) {
		return sysUser.getOrganizationId();
	}

	/**
	 * 是否匹配当前用户
	 * @param filterAttr 筛选属性
	 * @param filterCondition 筛选条件
	 * @return boolean true: 是否匹配
	 */
	@Override
	public boolean match(Object filterAttr, List<Object> filterCondition) {
		Long organizationId = (Long) filterAttr;
		return filterCondition.stream().map(Long.class::cast).anyMatch(x -> x.equals(organizationId));
	}

}
