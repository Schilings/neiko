package com.schilings.neiko.log.remote;


import com.schilings.neiko.common.core.http.RequestParameterObject;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.log.model.qo.OperationLogQO;
import com.schilings.neiko.log.model.vo.OperationLogPageVO;
import org.springframework.http.MediaType;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;


/**
 *
 * <p>
 * 操作日志
 * </p>
 *
 * @author Schilings
 */

@HttpExchange("/log/operation-log")
public interface OperationLogRemote {

	/**
	 * 分页查询
	 *
	 * @param pageParam      分页参数
	 * @param operationLogQO 操作日志
	 * @return R
	 */
	@GetExchange("/page")
	R<PageResult<OperationLogPageVO>> getOperationLogAdminPage(PageParam pageParam,
															   @RequestParameterObject OperationLogQO operationLogQO);

	/**
	 * Excel导出
	 *
	 * @param operationLogQO 操作日志查询对象
	 */
	@GetExchange(url = "/export", accept = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	byte[] exportOperationLogList(@RequestParameterObject OperationLogQO operationLogQO);
	
}
