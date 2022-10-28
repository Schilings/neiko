package com.schilings.neiko.store;

import com.schilings.neiko.logging.InternalLogger;
import com.schilings.neiko.logging.InternalLoggerFactory;
import com.schilings.neiko.store.util.CLibrary;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 *
 * <p>
 * 临时存储内存池
 * </p>
 *
 * @author Schilings
 */
public class TransientStorePool {

	private static final InternalLogger log = InternalLoggerFactory.getLogger(TransientStorePool.class);

	/**
	 * 内存池大小 内存块数量
	 */
	private final int poolSize;

	/**
	 * CommitLog文件大小，默认1G
	 */
	private final int fileSize;

	/**
	 * 可用的内存块
	 */
	private final Deque<ByteBuffer> availableBuffers;

	private boolean transientStorePoolEnable;

	public TransientStorePool() {
		this(5, 1024 * 1024 * 1024);
	}

	public TransientStorePool(int poolSize, int fileSize) {
		this(poolSize, fileSize, true);
	}

	public TransientStorePool(int poolSize, int fileSize, boolean transientStorePoolEnable) {
		this.poolSize = poolSize;
		this.fileSize = fileSize;
		this.availableBuffers = new ConcurrentLinkedDeque<>();
		this.transientStorePoolEnable = transientStorePoolEnable;
	}

	/**
	 * 初始化 这是一个沉重的初始化方法 该方法默认会初始化5个1G大小的堆外内存并且锁定住。这是一个重量级初始化操作，将会延长启动时间。
	 */
	public void init() {
		// 默认5个
		for (int i = 0; i < poolSize; i++) {
			// 分配直接内存 fileSize
			// 堆外内存就是通过ByteBuffer.allocateDirect方法分配的，这5块内存可以被重复利用。
			ByteBuffer byteBuffer = ByteBuffer.allocateDirect(fileSize);
			// 内存地址
			final long address = ((DirectBuffer) byteBuffer).address();
			// 本机指针
			Pointer pointer = new Pointer(address);
			// 锁定堆外内存，确保不会被置换到虚拟内存中去
			CLibrary.INSTANCE.mlock(pointer, new NativeLong(fileSize));
			// 存入队列中
			availableBuffers.offer(byteBuffer);
		}
	}

	/**
	 * 销毁占用的内存
	 */
	public void destroy() {
		for (ByteBuffer byteBuffer : availableBuffers) {
			final long address = ((DirectBuffer) byteBuffer).address();
			Pointer pointer = new Pointer(address);
			CLibrary.INSTANCE.munlock(pointer, new NativeLong(fileSize));
		}
	}

	/**
	 * 归还内存块
	 * @param byteBuffer
	 */
	public void returnBuffer(ByteBuffer byteBuffer) {
		byteBuffer.position(0);
		byteBuffer.limit(fileSize);
		this.availableBuffers.offerFirst(byteBuffer);
	}

	/**
	 * 使用内存块
	 * @return
	 */
	public ByteBuffer borrowBuffer() {
		ByteBuffer buffer = availableBuffers.pollFirst();
		// 剩余内存块小于四成时
		if (availableBuffers.size() < poolSize * 0.4) {
			log.warn("TransientStorePool only remain {} sheets.", availableBuffers.size());
		}
		return buffer;
	}

	/**
	 * 可用的内存块数量
	 * @return
	 */
	public int availableBufferNums() {
		if (transientStorePoolEnable) {
			return availableBuffers.size();
		}
		return Integer.MAX_VALUE;
	}

	public boolean isTransientStorePoolEnable() {
		return this.transientStorePoolEnable;
	}

}
