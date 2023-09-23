package com.schilings.neiko.log.remote;

import com.schilings.neiko.common.core.http.RequestParameterObject;

import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.log.model.qo.LoginLogQO;
import com.schilings.neiko.log.model.vo.LoginLogPageVO;
import org.springframework.http.MediaType;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.io.InputStream;
import java.util.List;

/**
 * 登陆日志
 */

@HttpExchange("/log/login-log")
public interface LoginLogRemote {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param loginLogQO 登陆日志查询对象
	 * @return R 通用返回体
	 */
	@GetExchange("/page")
	R<PageResult<LoginLogPageVO>> getLoginLogPage(PageParam pageParam, @RequestParameterObject LoginLogQO loginLogQO);

	/**
	 * Excel导出
	 * @param loginLogQO 登陆日志查询对象
	 */
	// @GetExchange(url = "/export", accept = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetExchange(url = "/export",
			accept = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8")
	byte[] exportAccessLogList(@RequestParameterObject LoginLogQO loginLogQO);

}
