package com.schilings.neiko.wechat.api;

import com.schilings.neiko.common.core.validation.group.CreateGroup;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.result.BaseResultCode;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.extend.sa.token.oauth2.annotation.OAuth2CheckPermission;
import com.schilings.neiko.extend.sa.token.oauth2.annotation.OAuth2CheckScope;
import com.schilings.neiko.wechat.service.WechatMpReplyService;
import com.schilings.neiko.wechat.constant.WechatMpConst;
import com.schilings.neiko.wechat.model.dto.WechatMpReplyDTO;
import com.schilings.neiko.wechat.model.entity.WechatMpReply;
import com.schilings.neiko.wechat.model.qo.WechatMpReplyQO;
import com.schilings.neiko.wechat.model.vo.WechatMpReplyPageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;

@OAuth2CheckScope("system")
@RestController
@RequiredArgsConstructor
@RequestMapping("/wechat/reply")
@Tag(name = "微信公众号自定义回复")
public class WechatMpReplyController {

	private final WechatMpReplyService wechatMpReplyService;

	@GetMapping("/page")
	@OAuth2CheckPermission("wechat:mpreply:read")
	@Operation(summary = "分页查询微信公众号自定义回复")
	public R<PageResult<WechatMpReplyPageVO>> getMpReplyPage(@Validated PageParam pageParam, WechatMpReplyQO qo) {
		return R.ok(wechatMpReplyService.queryPage(pageParam, qo));
	}

	/**
	 * 通过id查询消息自动回复
	 * @param id id
	 * @return R
	 */
	@GetMapping("/{id}")
	@OAuth2CheckPermission("wechat:mpreply:read")
	@Operation(summary = "通过id查询微信公众号自定义回复")
	public R<WechatMpReply> get(@PathVariable("id") Long id) {
		return R.ok(wechatMpReplyService.getById(id));
	}

	/**
	 * 新增消息自动回复
	 * @return R
	 */
	@PostMapping
	@OAuth2CheckPermission("wechat:mpreply:add")
	@Operation(summary = "新增微信公众号自定义回复")
	public R addMpReply(
			@Validated({ Default.class, CreateGroup.class }) @RequestBody WechatMpReplyDTO wechatMpReplyDTO) {
		// 关注时回复只能唯一
		if (wechatMpReplyDTO.getType() == WechatMpConst.Reply.SUBSCRIBE.getValue()) {
			WechatMpReply reply = wechatMpReplyService.getByType(wechatMpReplyDTO.getType());
			if (reply != null) {
				return R.fail(BaseResultCode.LOGIC_CHECK_ERROR, "该类型自动回复已存在");
			}
		}
		return wechatMpReplyService.addMpReply(wechatMpReplyDTO) ? R.ok()
				: R.fail(BaseResultCode.UPDATE_DATABASE_ERROR, "新增微信公众号自定义回复失败");
	}

}
