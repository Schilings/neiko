package com.schilings.neiko.notify.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.mapper.ExtendMapper;
import com.schilings.neiko.extend.mybatis.plus.wrapper.WrappersX;
import com.schilings.neiko.extend.mybatis.plus.wrapper.join.NeikoLambdaQueryWrapper;
import com.schilings.neiko.notify.enums.UserAnnouncementStateEnum;
import com.schilings.neiko.notify.model.entity.UserAnnouncement;
import com.schilings.neiko.notify.model.qo.UserAnnouncementQO;
import com.schilings.neiko.notify.model.vo.UserAnnouncementPageVO;

import java.time.LocalDateTime;

/**
 * 用户公告表
 */
public interface UserAnnouncementMapper extends ExtendMapper<UserAnnouncement> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询对象
	 * @return 分页结果数据 PageResult
	 */
	default PageResult<UserAnnouncementPageVO> queryPage(PageParam pageParam, UserAnnouncementQO qo) {
		IPage<UserAnnouncementPageVO> page = this.prodPage(pageParam);

		NeikoLambdaQueryWrapper<Object> queryWrapper = WrappersX.lambdaQueryJoin().selectAll(UserAnnouncement.class)
				.eqIfPresent(UserAnnouncement::getId, qo.getId());
		IPage<UserAnnouncementPageVO> voPage = this.selectJoinPage(page, UserAnnouncementPageVO.class, AUTO_RESULT_MAP,
				queryWrapper);
		return this.prodPage(voPage);
	}

	/**
	 * 更新用户公共信息至已读状态
	 * @param userId 用户ID
	 * @param announcementId 公告ID
	 */
	default void updateToReadState(Long userId, Long announcementId) {
		LambdaUpdateWrapper<UserAnnouncement> wrapper = Wrappers.<UserAnnouncement>lambdaUpdate()
				.set(UserAnnouncement::getState, UserAnnouncementStateEnum.READ.getValue())
				.set(UserAnnouncement::getReadTime, LocalDateTime.now())
				.eq(UserAnnouncement::getAnnouncementId, announcementId).eq(UserAnnouncement::getUserId, userId);
		this.update(null, wrapper);
	}

}
