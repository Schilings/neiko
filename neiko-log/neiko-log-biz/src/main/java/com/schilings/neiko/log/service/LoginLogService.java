package com.schilings.neiko.log.service;

import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.ExtendService;
import com.schilings.neiko.extend.mybatis.plus.wrapper.WrappersX;
import com.schilings.neiko.extend.mybatis.plus.wrapper.join.NeikoLambdaQueryWrapper;
import com.schilings.neiko.log.model.entity.LoginLog;
import com.schilings.neiko.log.model.qo.LoginLogQO;
import com.schilings.neiko.log.model.vo.LoginLogExcelVO;
import com.schilings.neiko.log.model.vo.LoginLogPageVO;

import java.util.List;

public interface LoginLogService extends ExtendService<LoginLog> {

	/**
	 * 根据QueryObject查询分页数据
	 * @param page 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<LoginLogVO> 分页数据
	 */
	PageResult<LoginLogPageVO> queryPage(PageParam page, LoginLogQO qo);

	/**
	 * 列表查询
	 *
	 * @param qo 查询对象
	 * @return 结果数据 List
	 */
	List<LoginLogExcelVO> queryExcelList(LoginLogQO qo);
}
