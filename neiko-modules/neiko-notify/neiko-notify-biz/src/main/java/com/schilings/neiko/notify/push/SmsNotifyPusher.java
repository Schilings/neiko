package com.schilings.neiko.notify.push;

import cn.hutool.core.util.StrUtil;

import com.schilings.neiko.common.util.HtmlUtils;
import com.schilings.neiko.notify.enums.NotifyChannelEnum;
import com.schilings.neiko.notify.model.domain.NotifyInfo;
import com.schilings.neiko.system.model.entity.SysUser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 短信通知发布
 */
@Component
public class SmsNotifyPusher implements NotifyPusher {

	/**
	 * 当前发布者对应的接收方式
	 * @see NotifyChannelEnum
	 * @return 推送方式
	 */
	@Override
	public Integer notifyChannel() {
		return NotifyChannelEnum.SMS.getValue();
	}

	@Override
	public void push(NotifyInfo notifyInfo, List<SysUser> userList) {
		List<String> phoneList = userList.stream().map(SysUser::getPhone).filter(StrUtil::isNotBlank)
				.collect(Collectors.toList());
		// 短信文本去除 html 标签
		String content = HtmlUtils.toText(notifyInfo.getContent());
		// TODO 对接短信发送平台
		System.out.println("短信推送");
	}

}
