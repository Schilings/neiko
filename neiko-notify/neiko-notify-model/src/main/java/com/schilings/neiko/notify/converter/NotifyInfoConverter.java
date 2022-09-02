package com.schilings.neiko.notify.converter;

import com.schilings.neiko.notify.model.domain.AnnouncementNotifyInfo;
import com.schilings.neiko.notify.model.entity.Announcement;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NotifyInfoConverter {

	NotifyInfoConverter INSTANCE = Mappers.getMapper(NotifyInfoConverter.class);

	/**
	 * 公告转通知实体
	 * @param announcement 公告信息
	 * @return 通知信息
	 */
	AnnouncementNotifyInfo fromAnnouncement(Announcement announcement);

}
