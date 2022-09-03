package com.schilings.neiko.log.service.impl;

import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.log.mapper.LoginLogMapper;
import com.schilings.neiko.log.model.entity.LoginLog;
import com.schilings.neiko.log.model.qo.LoginLogQO;
import com.schilings.neiko.log.model.vo.LoginLogExcelVO;
import com.schilings.neiko.log.model.vo.LoginLogPageVO;
import com.schilings.neiko.log.service.LoginLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginLogServiceImpl extends ExtendServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return PageResult<LoginLogVO> 分页数据
	 */
	@Override
	public PageResult<LoginLogPageVO> queryPage(PageParam pageParam, LoginLogQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

	/**
	 * 列表查询
	 *
	 * @param qo 查询对象
	 * @return 结果数据 List
	 */
	@Override
	public List<LoginLogExcelVO> queryList(LoginLogQO qo) {
		return baseMapper.queryList(qo);
	}

}
