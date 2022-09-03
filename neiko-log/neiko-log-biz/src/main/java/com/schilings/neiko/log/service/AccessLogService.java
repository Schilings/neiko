package com.schilings.neiko.log.service;

import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.ExtendService;
import com.schilings.neiko.log.model.entity.AccessLog;
import com.schilings.neiko.log.model.qo.AccessLogQO;
import com.schilings.neiko.log.model.vo.AccessLogExcelVO;
import com.schilings.neiko.log.model.vo.AccessLogPageVO;

import java.util.List;

public interface AccessLogService extends ExtendService<AccessLog> {

	/**
	 * 根据QueryObject查询分页数据
	 * @param page 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<LoginLogVO> 分页数据
	 */
	PageResult<AccessLogPageVO> queryPage(PageParam page, AccessLogQO qo);


	/**
	 * 列表查询
	 * @param qo 查询对象
	 * @return 结果数据 List
	 */
	List<AccessLogExcelVO> queryList(AccessLogQO qo); 
}
