package com.schilings.neiko.log.service.impl;

import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.log.mapper.AccessLogMapper;
import com.schilings.neiko.log.model.entity.AccessLog;
import com.schilings.neiko.log.model.qo.AccessLogQO;
import com.schilings.neiko.log.model.vo.AccessLogExcelVO;
import com.schilings.neiko.log.model.vo.AccessLogPageVO;
import com.schilings.neiko.log.service.AccessLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AccessLogServiceImpl extends ExtendServiceImpl<AccessLogMapper, AccessLog> implements AccessLogService {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return IPage<LoginLogVO> 分页数据
	 */
	@Override
	public PageResult<AccessLogPageVO> queryPage(PageParam pageParam, AccessLogQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

	/**
	 * 列表查询
	 *
	 * @param qo 查询对象
	 * @return 结果数据 List
	 */
	@Override
	public List<AccessLogExcelVO> queryList(AccessLogQO qo) {
		return baseMapper.queryList(qo);
	}

}
