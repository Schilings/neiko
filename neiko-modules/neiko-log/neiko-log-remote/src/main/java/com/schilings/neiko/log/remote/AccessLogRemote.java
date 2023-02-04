package com.schilings.neiko.log.remote;


import com.schilings.neiko.common.core.http.RequestParameterObject;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.log.model.qo.AccessLogQO;
import com.schilings.neiko.log.model.vo.AccessLogPageVO;
import org.springframework.http.MediaType;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;



/**
 * 访问日志
 */

@HttpExchange("/log/access-log")
public interface AccessLogRemote {
	

	/**
	 * 分页查询
	 *
	 * @param pageParam   分页参数
	 * @param accessLogQO 访问日志查询对象
	 * @return R
	 */
	@GetExchange("/page")
	R<PageResult<AccessLogPageVO>> getAccessLogPage(PageParam pageParam,@RequestParameterObject AccessLogQO accessLogQO);

	/**
	 * Excel导出
	 *
	 * @param accessLogQO 访问日志查询对象
	 */
	@GetExchange(url = "/export", accept = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	byte[] exportLoginLogList(@RequestParameterObject AccessLogQO accessLogQO); 
	
}
