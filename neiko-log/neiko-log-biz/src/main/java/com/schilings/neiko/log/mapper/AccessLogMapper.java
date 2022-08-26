package com.schilings.neiko.log.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.mapper.ExtendMapper;
import com.schilings.neiko.extend.mybatis.plus.wrapper.WrappersX;
import com.schilings.neiko.extend.mybatis.plus.wrapper.join.NeikoLambdaQueryWrapper;
import com.schilings.neiko.log.model.entity.AccessLog;
import com.schilings.neiko.log.model.qo.AccessLogQO;
import com.schilings.neiko.log.model.vo.AccessLogPageVO;

public interface AccessLogMapper extends ExtendMapper<AccessLog> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询对象
	 * @return 分页结果数据 PageResult
	 */
	default PageResult<AccessLogPageVO> queryPage(PageParam pageParam, AccessLogQO qo) {
		IPage<AccessLog> page = this.prodPage(pageParam);
		NeikoLambdaQueryWrapper<AccessLog> queryWrapper = WrappersX.<AccessLog>lambdaQueryJoin()
				.selectAll(AccessLog.class).eqIfPresent(AccessLog::getUserId, qo.getUserId())
				.eqIfPresent(AccessLog::getTraceId, qo.getTraceId())
				.eqIfPresent(AccessLog::getMatchingPattern, qo.getMatchingPattern())
				.eqIfPresent(AccessLog::getUri, qo.getUri()).eqIfPresent(AccessLog::getHttpStatus, qo.getHttpStatus())
				.eqIfPresent(AccessLog::getIp, qo.getIp()).gtIfPresent(AccessLog::getCreateTime, qo.getStartTime())
				.ltIfPresent(AccessLog::getCreateTime, qo.getEndTime());
		IPage<AccessLogPageVO> voPage = this.selectJoinPage(page, AccessLogPageVO.class, AUTO_RESULT_MAP, queryWrapper);
		return this.prodPage(voPage);
	}

}
