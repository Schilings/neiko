package com.schilings.neiko.notify.websocket.announcement;

import com.schilings.neiko.notify.websocket.AbstractNotifyInfoHandler;
import com.schilings.neiko.notify.model.domain.AnnouncementNotifyInfo;
import com.schilings.neiko.notify.model.entity.UserAnnouncement;
import com.schilings.neiko.notify.service.UserAnnouncementService;
import com.schilings.neiko.system.model.entity.SysUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 公告通知消息处理器
 */
@Component
@RequiredArgsConstructor
public class AnnouncementNotifyInfoHandler
		extends AbstractNotifyInfoHandler<AnnouncementNotifyInfo, AnnouncementPushMessage> {

	private final UserAnnouncementService userAnnouncementService;

	@Override
	protected void persistMessage(List<SysUser> userList, AnnouncementNotifyInfo announcementNotifyInfo) {
		List<UserAnnouncement> userAnnouncements = new ArrayList<>(userList.size());
		// 向指定用户推送
		for (SysUser sysUser : userList) {
			Long userId = sysUser.getUserId();
			UserAnnouncement userAnnouncement = userAnnouncementService.prodUserAnnouncement(userId,
					announcementNotifyInfo.getId());
			userAnnouncements.add(userAnnouncement);
		}
		userAnnouncementService.saveBatch(userAnnouncements);
	}

	@Override
	protected AnnouncementPushMessage createMessage(AnnouncementNotifyInfo announcementNotifyInfo) {
		AnnouncementPushMessage message = new AnnouncementPushMessage();
		message.setId(announcementNotifyInfo.getId());
		message.setTitle(announcementNotifyInfo.getTitle());
		message.setContent(announcementNotifyInfo.getContent());
		message.setImmortal(announcementNotifyInfo.getImmortal());
		message.setDeadline(announcementNotifyInfo.getDeadline());
		return message;
	}

}
