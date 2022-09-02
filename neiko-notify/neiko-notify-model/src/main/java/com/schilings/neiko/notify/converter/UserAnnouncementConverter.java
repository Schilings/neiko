package com.schilings.neiko.notify.converter;

import com.schilings.neiko.notify.model.entity.UserAnnouncement;
import com.schilings.neiko.notify.model.vo.UserAnnouncementPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserAnnouncementConverter {

	UserAnnouncementConverter INSTANCE = Mappers.getMapper(UserAnnouncementConverter.class);

	/**
	 * PO 转 PageVO
	 * @param userAnnouncement 用户公告表
	 * @return UserAnnouncementPageVO 用户公告表PageVO
	 */
	UserAnnouncementPageVO poToPageVo(UserAnnouncement userAnnouncement);

}
