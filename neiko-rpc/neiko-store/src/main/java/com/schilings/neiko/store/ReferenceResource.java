package com.schilings.neiko.store;

import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * <p>
 * 引用资源对象
 * </p>
 *
 * @author Schilings
 */
public abstract class ReferenceResource {

	/**
	 * 引用计数,创建对象强引用，即初始化为1次
	 */
	protected final AtomicLong refCount = new AtomicLong(1);

	/**
	 * 是否可用
	 */
	protected volatile boolean available = true;

	/**
	 * 是否已清理
	 */
	protected volatile boolean cleanupOver = false;

	private volatile long firstShutdownTimestamp = 0;

	public boolean isAvailable() {
		return this.available;
	}

	/**
	 * 占用该资源
	 * @return
	 */
	public synchronized boolean hold() {
		// 如果可用
		if (this.isAvailable()) {
			// 引用次数+1
			if (this.refCount.getAndIncrement() > 0) {
				return true;
			}
			else {
				this.refCount.getAndDecrement();
			}
		}
		return false;
	}

	/**
	 * 释放该资源
	 * @return
	 */
	public void release() {
		// 引用次数-1
		long value = this.refCount.decrementAndGet();
		// 还存在引用
		if (value > 0)
			return;
		// 引用次数为0,清理资源
		synchronized (this) {
			this.cleanupOver = this.cleanup(value);
		}
	}

	/**
	 * 强引用
	 * @param intervalForcibly
	 */
	public void shutdown(final long intervalForcibly) {
		if (this.available) {
			this.available = false;
			this.firstShutdownTimestamp = System.currentTimeMillis();
			this.release();
		}
		else if (this.getRefCount() > 0) {
			//
			if ((System.currentTimeMillis() - this.firstShutdownTimestamp) >= intervalForcibly) {
				this.refCount.set(-1000 - this.getRefCount());
				this.release();
			}
		}
	}

	public long getRefCount() {
		return this.refCount.get();
	}

	public abstract boolean cleanup(final long currentRef);

	public boolean isCleanupOver() {
		return this.refCount.get() <= 0 && this.cleanupOver;
	}

}
