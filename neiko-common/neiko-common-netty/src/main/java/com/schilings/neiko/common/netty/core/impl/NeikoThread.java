package com.schilings.neiko.common.netty.core.impl;

import io.netty.util.concurrent.FastThreadLocalThread;

public class NeikoThread extends FastThreadLocalThread {

	/**
	 * 是否为工作线程
	 */
	private final boolean worker;

	/**
	 * 线程上下文
	 */
	private ContextInternal context;

	public NeikoThread(Runnable target, String name, boolean worker) {
		super(target, name);
		this.worker = worker;
	}

	/**
	 * 此线程的当前上下文，必须从当前线程调用此方法
	 * @return
	 */
	public ContextInternal context() {
		return context;
	}

	public boolean isWorker() {
		return worker;
	}

}
