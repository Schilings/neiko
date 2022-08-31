package com.schilings.neiko.notify.push;

import cn.hutool.core.util.StrUtil;

import com.schilings.neiko.autoconfigure.mail.model.MailDetails;
import com.schilings.neiko.autoconfigure.mail.sender.MailSender;
import com.schilings.neiko.notify.enums.NotifyChannelEnum;
import com.schilings.neiko.notify.model.domain.NotifyInfo;
import com.schilings.neiko.system.model.entity.SysUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.util.List;

/**
 * 通知邮件发布
 */
@Component
@RequiredArgsConstructor
public class MailNotifyPusher implements NotifyPusher {

	private final MailSender mailSender;

	/**
	 * 当前发布者的推送方式
	 * @see NotifyChannelEnum
	 * @return 推送方式
	 */
	@Override
	public Integer notifyChannel() {
		return NotifyChannelEnum.MAIL.getValue();
	}

	@Override
	public void push(NotifyInfo notifyInfo, List<SysUser> userList) {
		String[] emails = userList.stream().map(SysUser::getEmail).filter(StrUtil::isNotBlank).toArray(String[]::new);

		// 密送群发，不展示其他收件人
		MailDetails mailDetails = new MailDetails();
		mailDetails.setShowHtml(true);
		mailDetails.setSubject(notifyInfo.getTitle());
		mailDetails.setContent(notifyInfo.getContent());
		mailDetails.setBcc(emails);
		mailSender.sendMail(mailDetails);
	}

}
