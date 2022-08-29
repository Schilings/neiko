package com.schilings.neiko.notify.converter;

import com.schilings.neiko.notify.model.dto.AnnouncementDTO;
import com.schilings.neiko.notify.model.entity.Announcement;
import com.schilings.neiko.notify.model.vo.AnnouncementPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface AnnouncementConverter {

	AnnouncementConverter INSTANCE = Mappers.getMapper(AnnouncementConverter.class);

	/**
	 * PO 转 PageVO
	 * @param announcement 公告表
	 * @return AnnouncementPageVO 公告表PageVO
	 */
	AnnouncementPageVO poToPageVo(Announcement announcement);

	/**
	 * AnnouncementDTO 转 Announcement实体
	 * @param dto AnnouncementDTO
	 * @return Announcement
	 */
	@Mapping(target = "updateTime", ignore = true)
	@Mapping(target = "createTime", ignore = true)
	@Mapping(target = "createBy", ignore = true)
	Announcement dtoToPo(AnnouncementDTO dto);

}
