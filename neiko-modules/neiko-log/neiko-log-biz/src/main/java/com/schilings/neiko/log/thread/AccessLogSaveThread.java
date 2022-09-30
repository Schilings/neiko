package com.schilings.neiko.log.thread;

import com.schilings.neiko.common.core.thread.AbstractBlockingQueueThread;
import com.schilings.neiko.log.model.entity.AccessLog;
import com.schilings.neiko.log.service.AccessLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 *
 * <p>
 * </p>
 *
 * @author Schilings
 */
@Slf4j
@RequiredArgsConstructor
public class AccessLogSaveThread extends AbstractBlockingQueueThread<AccessLog> {

	private final AccessLogService accessLogService;

	/**
	 * 线程启动时的日志打印
	 */
	@Override
	public void init() {
		log.info("后台访问日志存储线程已启动===");
	}

	/**
	 * 错误日志打印
	 * @param e 错误堆栈
	 * @param list 后台访问日志列表
	 */
	@Override
	public void error(Throwable e, List<AccessLog> list) {
		log.error("后台访问日志记录异常, [msg]:{}, [data]:{}", e.getMessage(), list);
	}

	/**
	 * 数据保存
	 * @param list 后台访问日志列表
	 */
	@Override
	public void process(List<AccessLog> list) throws Exception {
		accessLogService.saveBatch(list);
	}

}
