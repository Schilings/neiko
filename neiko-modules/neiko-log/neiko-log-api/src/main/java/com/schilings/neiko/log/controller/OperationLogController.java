package com.schilings.neiko.log.controller;

import com.schilings.neiko.common.excel.annotation.ResponseExcel;
import com.schilings.neiko.common.excel.annotation.Sheet;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.log.model.qo.OperationLogQO;
import com.schilings.neiko.log.model.vo.OperationLogExcelVO;
import com.schilings.neiko.log.model.vo.OperationLogPageVO;
import com.schilings.neiko.log.service.OperationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * <p>
 * 操作日志
 * </p>
 *
 * @author Schilings
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/log/operation-log")
@Tag(name = "操作日志管理")
public class OperationLogController {

	private final OperationLogService operationLogService;

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param operationLogQO 操作日志
	 * @return R
	 */
	@GetMapping("/page")
	@Operation(summary = "分页查询", description = "分页查询")
	public R<PageResult<OperationLogPageVO>> getOperationLogAdminPage(@Validated PageParam pageParam,
			OperationLogQO operationLogQO) {
		return R.ok(operationLogService.queryPage(pageParam, operationLogQO));
	}

	/**
	 * Excel导出
	 * @param operationLogQO 操作日志查询对象
	 */
	@GetMapping("/export")
	@Operation(summary = "Excel导出", description = "Excel导出")
	@ResponseExcel(name = "操作日志Excel", sheets = { @Sheet(sheetNo = 1, sheetName = "sheetNO1") })
	public List<OperationLogExcelVO> exportOperationLogList(OperationLogQO operationLogQO) {
		return operationLogService.queryExcelList(operationLogQO);
	}

}
