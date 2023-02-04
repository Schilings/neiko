package com.schilings.neiko.notify.remote;


import com.schilings.neiko.common.core.http.RequestParameterObject;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.notify.model.dto.AnnouncementDTO;
import com.schilings.neiko.notify.model.entity.Announcement;
import com.schilings.neiko.notify.model.qo.AnnouncementQO;
import com.schilings.neiko.notify.model.vo.AnnouncementPageVO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.*;

import java.util.List;


@HttpExchange("/notify/announcement")
public interface AnnouncementRemote {


	/**
	 * 分页查询
	 *
	 * @param pageParam      分页对象
	 * @param announcementQO 公告信息查询对象
	 * @return R 通用返回体
	 */
	@GetExchange("/page")
	R<PageResult<AnnouncementPageVO>> getAnnouncementPage(PageParam pageParam,
														  @RequestParameterObject AnnouncementQO announcementQO);

	/**
	 * 新增公告信息
	 *
	 * @param announcementDTO 公告信息
	 * @return R 通用返回体
	 */
	@PostExchange
	R<Void> save(@RequestBody AnnouncementDTO announcementDTO);

	/**
	 * 修改公告信息
	 *
	 * @param announcementDTO 公告信息
	 * @return R 通用返回体
	 */
	@PutExchange
	R<Void> updateById(@RequestBody AnnouncementDTO announcementDTO);

	/**
	 * 通过id删除公告信息
	 *
	 * @param id id
	 * @return R 通用返回体
	 */
	@DeleteExchange("/{id}")
	R<Void> removeById(@PathVariable("id") Long id);

	/**
	 * 发布公告信息
	 *
	 * @return R 通用返回体
	 */
	@PatchExchange("/publish/{announcementId}")
	R<Void> enableAnnouncement(@PathVariable("announcementId") Long announcementId);

	/**
	 * 关闭公告信息
	 *
	 * @return R 通用返回体
	 */
	@PatchExchange("/close/{announcementId}")
	R<Void> disableAnnouncement(@PathVariable("announcementId") Long announcementId);


	@PostExchange("/image")
	R<List<String>> uploadImages(@RequestParam("files") List<MultipartFile> files);

	@GetExchange("/user")
	R<List<Announcement>> getUserAnnouncements();

}
