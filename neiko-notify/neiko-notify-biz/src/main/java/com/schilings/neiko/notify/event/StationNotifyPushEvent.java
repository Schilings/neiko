package com.schilings.neiko.notify.event;

import com.schilings.neiko.notify.model.domain.NotifyInfo;
import com.schilings.neiko.system.model.entity.SysUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


/**
 * 
 * <p>站内通知方式，采用Websocket</p>
 * 
 * @author Schilings
*/
@Getter
@RequiredArgsConstructor
public class StationNotifyPushEvent {

	/**
	 * 通知信息
	 */
	private final NotifyInfo notifyInfo;

	/**
	 * 推送用户列表
	 */
	private final List<SysUser> userList;

}
