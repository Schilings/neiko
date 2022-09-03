package com.schilings.neiko.notify.controller;

import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.extend.sa.token.holder.RBACAuthorityHolder;
import com.schilings.neiko.extend.sa.token.oauth2.annotation.OAuth2CheckPermission;
import com.schilings.neiko.extend.sa.token.oauth2.annotation.OAuth2CheckScope;
import com.schilings.neiko.notify.model.qo.UserAnnouncementQO;
import com.schilings.neiko.notify.model.vo.UserAnnouncementPageVO;
import com.schilings.neiko.notify.service.UserAnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@OAuth2CheckScope("system")
@RestController
@RequiredArgsConstructor
@RequestMapping("/notify/user-announcement")
@Tag(name = "用户公告表管理")
public class UserAnnouncementController {

	private final UserAnnouncementService userAnnouncementService;

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param userAnnouncementQO 用户公告表查询对象
	 * @return R 通用返回体
	 */
	@GetMapping("/page")
	@OAuth2CheckPermission("notify:userannouncement:read")
	@Operation(summary = "分页查询", description = "分页查询")
	public R<PageResult<UserAnnouncementPageVO>> getUserAnnouncementPage(@Validated PageParam pageParam,
			UserAnnouncementQO userAnnouncementQO) {
		return R.ok(userAnnouncementService.queryPage(pageParam, userAnnouncementQO));
	}

	@PatchMapping("/read/{announcementId}")
	@OAuth2CheckPermission("notify:userannouncement:read")
	@Operation(summary = "用户公告已读上报", description = "用户公告已读上报")
	public R<Void> readAnnouncement(@PathVariable("announcementId") Long announcementId) {
		Long userId = Long.valueOf(RBACAuthorityHolder.getUserDetails().getUserId());
		userAnnouncementService.readAnnouncement(userId, announcementId);
		return R.ok();
	}

}
