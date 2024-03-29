package com.schilings.neiko.store;

import com.schilings.neiko.logging.InternalLogger;
import com.schilings.neiko.logging.InternalLoggerFactory;
import com.schilings.neiko.store.config.FlushDiskType;
import com.schilings.neiko.svrutil.ServiceThread;
import com.schilings.neiko.svrutil.UtilAll;

import java.io.File;
import java.io.IOException;
import java.util.ServiceLoader;
import java.util.concurrent.*;

public class AllocateMappedFileService extends ServiceThread {

	private static final InternalLogger log = InternalLoggerFactory.getLogger(AllocateMappedFileService.class);

	private static int waitTimeOut = 1000 * 5;

	private ConcurrentMap<String, AllocateRequest> requestTable = new ConcurrentHashMap<>();

	private PriorityBlockingQueue<AllocateRequest> requestQueue = new PriorityBlockingQueue<>();

	private FlushDiskType flushDiskType = FlushDiskType.ASYNC_FLUSH;

	private TransientStorePool transientStorePool;

	private boolean warmMappedFileEnbaled = false;

	private int mappedFileSize = 1024 * 1024 * 1024;

	// 磁盘处于预热状态时刷新页面大小
	private int flushLeastPagesWhenWarmMapedFile = 1024 / 4 * 16;

	private boolean fastFailIfNoBufferInStorePool = false;

	private volatile boolean hasException = false;

	public AllocateMappedFileService(TransientStorePool transientStorePool) {
		this.transientStorePool = transientStorePool;
	}

	@Override
	public String getServiceName() {
		return AllocateMappedFileService.class.getSimpleName();
	}

	@Override
	public void shutdown() {
		super.shutdown(true);
		for (AllocateRequest req : this.requestTable.values()) {
			if (req.mappedFile != null) {
				log.info("delete pre allocated maped file, {}", req.mappedFile.getFileName());
				req.mappedFile.destroy(1000);
			}
		}
	}

	@Override
	public void run() {
		log.info(this.getServiceName() + " service started");

		// 死循环
		// 如果服务没有停止，并且没有被线程中断，那么一直循环执行mmapOperation方法
		while (!this.isStopped() && this.mmapOperation()) {

		}
		log.info(this.getServiceName() + " service end");
	}

	/**
	 * 只有被外线程中断，才会返回false
	 */
	private boolean mmapOperation() {
		boolean isSuccess = false;
		AllocateRequest req = null;
		try {
			// 从requestQueue中获取优先级最高的一个请求，即文件名最小或者说起始offset最小的请求
			// requestQueue是一个优先级队列
			// 取出创建请求 与 文件路径
			req = this.requestQueue.take();
			// 从requestTable获取对应的请求
			AllocateRequest expectedRequest = this.requestTable.get(req.getFilePath());
			if (null == expectedRequest) {
				log.warn("this mmap request expired, maybe cause timeout " + req.getFilePath() + " "
						+ req.getFileSize());
				return true;
			}
			if (expectedRequest != req) {
				log.warn("never expected here,  maybe cause timeout " + req.getFilePath() + " " + req.getFileSize()
						+ ", req:" + req + ", expectedRequest:" + expectedRequest);
				return true;
			}

			// 获取对应的mappedFile，如果为null则创建
			if (req.getMappedFile() == null) {
				// 起始时间
				long beginTime = System.currentTimeMillis();

				MappedFile mappedFile;
				// 如果当前节点不是从节点，并且是异步刷盘策略，并且transientStorePoolEnable参数配置为true，那么使用堆外内存，默认不使用
				// 引入的 transientStorePoolEnable 能缓解 pagecache
				// 的压力，其原理是基于DirectByteBuffer和MappedByteBuffer的读写分离
				// 消息先写入DirectByteBuffer（堆外内存），随后从MappedByteBuffer（pageCache）读取。
				if (transientStorePool.isTransientStorePoolEnable()) {
					try {
						// 可以基于SPI机制获取自定义的MappedFile
						mappedFile = ServiceLoader.load(MappedFile.class).iterator().next();
						// 初始化
						mappedFile.init(req.getFilePath(), req.getFileSize(), transientStorePool);
					}
					catch (RuntimeException e) {
						log.warn("Use default implementation.");
						mappedFile = new MappedFile(req.getFilePath(), req.getFileSize(), transientStorePool);
					}
				}
				else {
					// 普通方式创建mappedFile，并且进行mmap
					mappedFile = new MappedFile(req.getFilePath(), req.getFileSize());
				}
				// 创建mappedFile消耗的时间
				long elapsedTime = UtilAll.computeElapsedTimeMilliseconds(beginTime);
				if (elapsedTime > 10) {
					int queueSize = this.requestQueue.size();
					log.warn("create mappedFile spent time(ms) " + elapsedTime + " queue size " + queueSize + " "
							+ req.getFilePath() + " " + req.getFileSize());
				}

				// 预写映射文件 mappedFile
				// 如果mappedFile大小大于等于1G并且warmMapedFileEnable参数为true，那么预写mappedFile，也就是所谓的内存预热或者文件预热
				// 注意warmMapedFileEnable参数默认为false，即默认不开启文件预热，因此选哟手动开启
				if (mappedFile.getFileSize() >= this.mappedFileSize && this.warmMappedFileEnbaled) {
					// 预热文件
					mappedFile.warmMappedFile(this.flushDiskType, this.flushLeastPagesWhenWarmMapedFile);
				}
				// 请求成功
				req.setMappedFile(mappedFile);
				this.hasException = false;
				isSuccess = true;
			}
		}
		catch (InterruptedException e) {
			log.warn(this.getServiceName() + " interrupted, possibly by shutdown.");
			this.hasException = true;
			return false;
		}
		catch (IOException e) {
			log.warn(this.getServiceName() + " service has exception. ", e);
			this.hasException = true;
			if (null != req) {
				requestQueue.offer(req);
				try {
					Thread.sleep(1);
				}
				catch (InterruptedException ignored) {
				}
			}
		}
		finally {
			if (req != null && isSuccess)
				req.getCountDownLatch().countDown();
		}
		return true;
	}

	/**
	 * 请求分配映射文件，两个请求，nextNextFile这个是预处理，还不用实际用到，不用提前返回
	 * @param nextFilePath
	 * @param nextNextFilePath
	 * @param fileSize
	 * @return
	 */
	public MappedFile putRequestAndReturnMappedFile(String nextFilePath, String nextNextFilePath, int fileSize) {
		int canSubmitRequests = 2;// 两个请求
		// 如果当前节点不是从节点，并且是异步刷盘策略，并且transientStorePoolEnable参数配置为true，并且fastFailIfNoBufferInStorePool为true
		// 那么重新计算最多可以提交几个文件创建请求
		if (this.transientStorePool.isTransientStorePoolEnable()) {
			// 内存池满时允许快速失败，请求数为内存池剩余可用块数
			if (this.fastFailIfNoBufferInStorePool) {
				canSubmitRequests = this.transientStorePool.availableBufferNums() - this.requestQueue.size();
			}
		}
		// 根据nextFilePath创建一个请求对象，并将请求对象存入requestTable这个map集合中
		AllocateRequest nextReq = new AllocateRequest(nextFilePath, fileSize);
		boolean nextPutOK = this.requestTable.putIfAbsent(nextFilePath, nextReq) == null;
		// 如果存入成功
		if (nextPutOK) {
			if (canSubmitRequests <= 0) {
				log.warn(
						"[NOTIFYME]TransientStorePool is not enough, so create mapped file error, "
								+ "RequestQueueSize : {}, StorePoolSize: {}",
						this.requestQueue.size(), this.transientStorePool.availableBufferNums());
				this.requestTable.remove(nextFilePath);
				return null;
			}
			// 将请求存入requestQueue中
			boolean offerOK = this.requestQueue.offer(nextReq);
			if (!offerOK) {
				log.warn("never expected here, add a request to preallocate queue failed");
			}
			// 可以提交的请求数量自减
			canSubmitRequests--;
		}
		// 根据nextNextFilePath创建另一个请求对象，并将请求对象存入requestTable这个map集合中
		AllocateRequest nextNextReq = new AllocateRequest(nextNextFilePath, fileSize);
		boolean nextNextPutOK = this.requestTable.putIfAbsent(nextNextFilePath, nextNextReq) == null;
		if (nextNextPutOK) {
			if (canSubmitRequests <= 0) {
				log.warn(
						"[NOTIFYME]TransientStorePool is not enough, so skip preallocate mapped file, "
								+ "RequestQueueSize : {}, StorePoolSize: {}",
						this.requestQueue.size(), this.transientStorePool.availableBufferNums());
				this.requestTable.remove(nextNextFilePath);
			}
			else {
				// 将请求存入requestQueue中
				boolean offerOK = this.requestQueue.offer(nextNextReq);
				if (!offerOK) {
					log.warn("never expected here, add a request to preallocate queue failed");
				}
			}
		}

		// 有异常就直接返回
		if (hasException) {
			log.warn(this.getServiceName() + " service has exception. so return null");
			return null;
		}

		// 获取此前存入的nextFilePath对应的请求
		AllocateRequest result = this.requestTable.get(nextFilePath);
		try {
			if (result != null) {
				// nextFile阻塞等到分配空间 同步等待最多5s
				boolean waitOK = result.getCountDownLatch().await(waitTimeOut, TimeUnit.MILLISECONDS);
				if (!waitOK) {
					// 超时
					log.warn("create mmap timeout " + result.getFilePath() + " " + result.getFileSize());
					return null;
				}
				else {
					// 如果nextFilePath对应的MappedFile创建成功，那么从requestTable移除对应的请求
					this.requestTable.remove(nextFilePath);
					// 返回创建的mappedFile
					return result.getMappedFile();
				}
			}
			else {
				log.error("find preallocate mmap failed, this never happen");
			}
		}
		catch (InterruptedException e) {
			log.warn(this.getServiceName() + " service has exception. ", e);
		}

		return null;
	}

	
	public FlushDiskType getFlushDiskType() {
		return flushDiskType;
	}

	public void setFlushDiskType(FlushDiskType flushDiskType) {
		this.flushDiskType = flushDiskType;
	}

	public boolean isWarmMappedFileEnbaled() {
		return warmMappedFileEnbaled;
	}

	public void setWarmMappedFileEnbaled(boolean warmMappedFileEnbaled) {
		this.warmMappedFileEnbaled = warmMappedFileEnbaled;
	}

	public int getMappedFileSize() {
		return mappedFileSize;
	}

	public void setMappedFileSize(int mappedFileSize) {
		this.mappedFileSize = mappedFileSize;
	}

	public boolean isFastFailIfNoBufferInStorePool() {
		return fastFailIfNoBufferInStorePool;
	}

	public void setFastFailIfNoBufferInStorePool(boolean fastFailIfNoBufferInStorePool) {
		this.fastFailIfNoBufferInStorePool = fastFailIfNoBufferInStorePool;
	}

	public int getFlushLeastPagesWhenWarmMapedFile() {
		return flushLeastPagesWhenWarmMapedFile;
	}

	public void setFlushLeastPagesWhenWarmMapedFile(int flushLeastPagesWhenWarmMapedFile) {
		this.flushLeastPagesWhenWarmMapedFile = flushLeastPagesWhenWarmMapedFile;
	}

	static class AllocateRequest implements Comparable<AllocateRequest> {

		// Full file path
		private String filePath;

		private int fileSize;

		private CountDownLatch countDownLatch = new CountDownLatch(1);

		private volatile MappedFile mappedFile = null;

		public AllocateRequest(String filePath, int fileSize) {
			this.filePath = filePath;
			this.fileSize = fileSize;
		}

		
		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}

		public int getFileSize() {
			return fileSize;
		}

		public void setFileSize(int fileSize) {
			this.fileSize = fileSize;
		}

		public CountDownLatch getCountDownLatch() {
			return countDownLatch;
		}

		public void setCountDownLatch(CountDownLatch countDownLatch) {
			this.countDownLatch = countDownLatch;
		}

		public MappedFile getMappedFile() {
			return mappedFile;
		}

		public void setMappedFile(MappedFile mappedFile) {
			this.mappedFile = mappedFile;
		}

		public int compareTo(AllocateRequest other) {
			if (this.fileSize < other.fileSize)
				return 1;
			else if (this.fileSize > other.fileSize) {
				return -1;
			}
			else {
				int mIndex = this.filePath.lastIndexOf(File.separator);
				long mName = Long.parseLong(this.filePath.substring(mIndex + 1));
				int oIndex = other.filePath.lastIndexOf(File.separator);
				long oName = Long.parseLong(other.filePath.substring(oIndex + 1));
				if (mName < oName) {
					return -1;
				}
				else if (mName > oName) {
					return 1;
				}
				else {
					return 0;
				}
			}
			// return this.fileSize < other.fileSize ? 1 : this.fileSize >
			// other.fileSize ? -1 : 0;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
			result = prime * result + fileSize;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			AllocateRequest other = (AllocateRequest) obj;
			if (filePath == null) {
				if (other.filePath != null)
					return false;
			}
			else if (!filePath.equals(other.filePath))
				return false;
			if (fileSize != other.fileSize)
				return false;
			return true;
		}

	}

}
