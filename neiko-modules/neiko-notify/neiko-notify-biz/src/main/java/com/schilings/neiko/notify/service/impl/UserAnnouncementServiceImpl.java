package com.schilings.neiko.notify.service.impl;


import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.notify.enums.UserAnnouncementStateEnum;
import com.schilings.neiko.notify.mapper.UserAnnouncementMapper;
import com.schilings.neiko.notify.model.entity.UserAnnouncement;
import com.schilings.neiko.notify.model.qo.UserAnnouncementQO;
import com.schilings.neiko.notify.model.vo.UserAnnouncementPageVO;
import com.schilings.neiko.notify.service.UserAnnouncementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAnnouncementServiceImpl extends ExtendServiceImpl<UserAnnouncementMapper, UserAnnouncement>
		implements UserAnnouncementService {
	

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<UserAnnouncementVO> 分页数据
	 */
	@Override
	public PageResult<UserAnnouncementPageVO> queryPage(PageParam pageParam, UserAnnouncementQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

	/**
	 * 根据用户ID和公告id初始化一个新的用户公告关联对象
	 * @param userId 用户ID
	 * @param announcementId 公告ID
	 * @return UserAnnouncement
	 */
	@Override
	public UserAnnouncement prodUserAnnouncement(Long userId, Long announcementId) {
		UserAnnouncement userAnnouncement = new UserAnnouncement();
		userAnnouncement.setUserId(userId);
		userAnnouncement.setAnnouncementId(announcementId);
		userAnnouncement.setCreateTime(LocalDateTime.now());
		userAnnouncement.setState(UserAnnouncementStateEnum.UNREAD.getValue());
		return userAnnouncement;
	}

	/**
	 * 对用户公告进行已读标记
	 * @param userId 用户id
	 * @param announcementId 公告id
	 */
	@Override
	public void readAnnouncement(Long userId, Long announcementId) {
		baseMapper.updateToReadState(userId, announcementId);
	}

}
