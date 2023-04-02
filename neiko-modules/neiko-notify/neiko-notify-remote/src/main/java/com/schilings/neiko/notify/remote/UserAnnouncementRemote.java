package com.schilings.neiko.notify.remote;

import com.schilings.neiko.common.core.http.RequestParameterObject;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.notify.model.qo.UserAnnouncementQO;
import com.schilings.neiko.notify.model.vo.UserAnnouncementPageVO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PatchExchange;

@HttpExchange("/notify/user-announcement")
public interface UserAnnouncementRemote {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param userAnnouncementQO 用户公告表查询对象
	 * @return R 通用返回体
	 */
	@GetExchange("/page")
	R<PageResult<UserAnnouncementPageVO>> getUserAnnouncementPage(PageParam pageParam,
			@RequestParameterObject UserAnnouncementQO userAnnouncementQO);

	@PatchExchange("/read/{announcementId}")
	R<Void> readAnnouncement(@PathVariable("announcementId") Long announcementId);

}
