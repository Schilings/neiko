package com.schilings.neiko.log.controller;

import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.log.model.qo.AccessLogQO;
import com.schilings.neiko.log.model.vo.AccessLogPageVO;
import com.schilings.neiko.log.service.AccessLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 访问日志
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/log/access-log")
@Tag(name = "访问日志管理")
public class AccessLogController {

	private final AccessLogService accessLogService;

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param accessLogQO 访问日志查询对象
	 * @return R
	 */
	@GetMapping("/page")
	@Operation(summary = "分页查询", description = "分页查询")
	public R<PageResult<AccessLogPageVO>> getAccessLogApiPage(@Validated PageParam pageParam, AccessLogQO accessLogQO) {
		return R.ok(accessLogService.queryPage(pageParam, accessLogQO));
	}

}
