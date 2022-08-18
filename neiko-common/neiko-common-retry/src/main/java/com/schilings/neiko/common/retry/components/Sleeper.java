package com.schilings.neiko.common.retry.components;

import java.io.Serializable;

public interface Sleeper extends Serializable {

	/**
	 * 使用任何可用的方法暂停指定时间段
	 * @param backOffPeriod
	 * @throws InterruptedException
	 */
	void sleep(long backOffPeriod) throws InterruptedException;

}
