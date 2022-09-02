package com.schilings.neiko.notify.push;

import com.schilings.neiko.notify.model.domain.NotifyInfo;
import com.schilings.neiko.system.model.entity.SysUser;

import java.util.List;

/**
 * 通知发布者
 */
public interface NotifyPusher {

	/**
	 * 当前发布者对应的推送渠道
	 * @see NotifyChannelEnum
	 * @return 推送方式对应的标识
	 */
	Integer notifyChannel();

	/**
	 * 推送通知
	 * @param notifyInfo 通知信息
	 * @param userList 用户列表
	 */
	void push(NotifyInfo notifyInfo, List<SysUser> userList);

}
