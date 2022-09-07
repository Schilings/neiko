package com.schilings.neiko.log.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.mapper.ExtendMapper;
import com.schilings.neiko.extend.mybatis.plus.wrapper.WrappersX;
import com.schilings.neiko.extend.mybatis.plus.wrapper.join.NeikoLambdaQueryWrapper;
import com.schilings.neiko.log.model.entity.OperationLog;
import com.schilings.neiko.log.model.qo.OperationLogQO;
import com.schilings.neiko.log.model.vo.OperationLogExcelVO;
import com.schilings.neiko.log.model.vo.OperationLogPageVO;

import java.util.List;

public interface OperationLogMapper extends ExtendMapper<OperationLog> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询对象
	 * @return 分页结果数据 PageResult
	 */
	default PageResult<OperationLogPageVO> queryPage(PageParam pageParam, OperationLogQO qo) {
		IPage<OperationLog> page = this.prodPage(pageParam);
		NeikoLambdaQueryWrapper<Object> queryWrapper = WrappersX.lambdaQueryJoin().selectAll(OperationLog.class)
				.eqIfPresent(OperationLog::getOperator, qo.getUserId())
				.eqIfPresent(OperationLog::getTraceId, qo.getTraceId()).eqIfPresent(OperationLog::getUri, qo.getUri())
				.eqIfPresent(OperationLog::getIp, qo.getIp()).eqIfPresent(OperationLog::getStatus, qo.getStatus())
				.eqIfPresent(OperationLog::getType, qo.getType()).likeIfPresent(OperationLog::getMsg, qo.getMsg())
				.gtIfPresent(OperationLog::getCreateTime, qo.getStartTime())
				.ltIfPresent(OperationLog::getCreateTime, qo.getEndTime());
		IPage<OperationLogPageVO> voPage = this.selectJoinPage(page, OperationLogPageVO.class, AUTO_RESULT_MAP,
				queryWrapper);
		return this.prodPage(voPage);
	}

	/**
	 * 列表查询
	 * @param qo 查询对象
	 * @return 结果数据 List
	 */
	default List<OperationLogExcelVO> queryExcelList(OperationLogQO qo) {
		NeikoLambdaQueryWrapper<OperationLog> queryWrapper = WrappersX.<OperationLog>lambdaQueryJoin()
				.selectAll(OperationLog.class).eqIfPresent(OperationLog::getOperator, qo.getUserId())
				.eqIfPresent(OperationLog::getTraceId, qo.getTraceId()).eqIfPresent(OperationLog::getUri, qo.getUri())
				.eqIfPresent(OperationLog::getIp, qo.getIp()).eqIfPresent(OperationLog::getStatus, qo.getStatus())
				.eqIfPresent(OperationLog::getType, qo.getType()).likeIfPresent(OperationLog::getMsg, qo.getMsg())
				.gtIfPresent(OperationLog::getCreateTime, qo.getStartTime())
				.ltIfPresent(OperationLog::getCreateTime, qo.getEndTime());
		return this.selectJoinList(OperationLogExcelVO.class, AUTO_RESULT_MAP, queryWrapper);
	}

}
