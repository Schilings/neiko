package com.schilings.neiko.log.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.mapper.ExtendMapper;
import com.schilings.neiko.extend.mybatis.plus.wrapper.WrappersX;
import com.schilings.neiko.extend.mybatis.plus.wrapper.join.NeikoLambdaQueryWrapper;
import com.schilings.neiko.log.model.entity.LoginLog;
import com.schilings.neiko.log.model.qo.LoginLogQO;
import com.schilings.neiko.log.model.vo.LoginLogExcelVO;
import com.schilings.neiko.log.model.vo.LoginLogPageVO;

import java.util.List;

public interface LoginLogMapper extends ExtendMapper<LoginLog> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询对象
	 * @return 分页结果数据 PageResult
	 */
	default PageResult<LoginLogPageVO> queryPage(PageParam pageParam, LoginLogQO qo) {
		IPage<LoginLog> page = this.prodPage(pageParam);
		NeikoLambdaQueryWrapper<LoginLogPageVO> queryWrapper = WrappersX.<LoginLogPageVO>lambdaQueryJoin()
				.selectAll(LoginLog.class).eqIfPresent(LoginLog::getUsername, qo.getUsername())
				.eqIfPresent(LoginLog::getTraceId, qo.getTraceId()).eqIfPresent(LoginLog::getIp, qo.getIp())
				.eqIfPresent(LoginLog::getEventType, qo.getEventType()).eqIfPresent(LoginLog::getStatus, qo.getStatus())
				.gtIfPresent(LoginLog::getLoginTime, qo.getStartTime())
				.ltIfPresent(LoginLog::getLoginTime, qo.getEndTime());

		IPage<LoginLogPageVO> voPage = this.selectJoinPage(page, LoginLogPageVO.class, AUTO_RESULT_MAP, queryWrapper);
		return this.prodPage(voPage);
	}

	/**
	 * 列表查询
	 * @param qo 查询对象
	 * @return 结果数据 List
	 */
	default List<LoginLogExcelVO> queryExcelList(LoginLogQO qo) {
		NeikoLambdaQueryWrapper<LoginLog> queryWrapper = WrappersX.<LoginLog>lambdaQueryJoin().selectAll(LoginLog.class)
				.eqIfPresent(LoginLog::getUsername, qo.getUsername()).eqIfPresent(LoginLog::getTraceId, qo.getTraceId())
				.eqIfPresent(LoginLog::getIp, qo.getIp()).eqIfPresent(LoginLog::getEventType, qo.getEventType())
				.eqIfPresent(LoginLog::getStatus, qo.getStatus()).gtIfPresent(LoginLog::getLoginTime, qo.getStartTime())
				.ltIfPresent(LoginLog::getLoginTime, qo.getEndTime());
		return this.selectJoinList(LoginLogExcelVO.class, AUTO_RESULT_MAP, queryWrapper);
	}

}
