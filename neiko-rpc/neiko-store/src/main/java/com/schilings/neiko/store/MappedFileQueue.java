package com.schilings.neiko.store;

import com.schilings.neiko.logging.InternalLogger;
import com.schilings.neiko.logging.InternalLoggerFactory;
import com.schilings.neiko.svrutil.UtilAll;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class MappedFileQueue {

	private static final InternalLogger log = InternalLoggerFactory.getLogger("STORE_LOGGER_NAME");

	private static final InternalLogger LOG_ERROR = InternalLoggerFactory.getLogger("STORE_ERROR_LOGGER_NAME");

	private static final int DELETE_FILES_BATCH_MAX = 10;

	// 存储目录
	private final String storePath;

	// 每个文件大小
	protected final int mappedFileSize;

	// 文件列表
	protected final CopyOnWriteArrayList<MappedFile> mappedFiles = new CopyOnWriteArrayList<MappedFile>();

	// 文件创建线程
	private final AllocateMappedFileService allocateMappedFileService;

	protected long flushedWhere = 0;

	private long committedWhere = 0;

	private volatile long storeTimestamp = 0;

	public MappedFileQueue(final String storePath, int mappedFileSize,
			AllocateMappedFileService allocateMappedFileService) {
		this.storePath = storePath;
		this.mappedFileSize = mappedFileSize;
		this.allocateMappedFileService = allocateMappedFileService;
	}

	/**
	 * 自我检查
	 */
	public void checkSelf() {
		if (!this.mappedFiles.isEmpty()) {
			Iterator<MappedFile> iterator = mappedFiles.iterator();
			MappedFile pre = null;
			while (iterator.hasNext()) {
				MappedFile cur = iterator.next();
				if (pre != null) {
					// 前后两个文件偏移量差
					if (cur.getFileFromOffset() - pre.getFileFromOffset() != this.mappedFileSize) {
						LOG_ERROR.error(
								"[BUG]The mappedFile queue's data is damaged, the adjacent mappedFile's offset don't match. pre file {}, cur file {}",
								pre.getFileName(), cur.getFileName());
					}
				}
				pre = cur;
			}
		}
	}

	/**
	 * 根据时间戳查找映射文件
	 * @param timestamp
	 * @return
	 */
	public MappedFile getMappedFileByTime(final long timestamp) {
		Object[] mfs = this.copyMappedFiles(0);
		if (null == mfs)
			return null;

		for (int i = 0; i < mfs.length; i++) {
			MappedFile mappedFile = (MappedFile) mfs[i];
			if (mappedFile.getLastModifiedTimestamp() >= timestamp) {
				return mappedFile;
			}
		}

		return (MappedFile) mfs[mfs.length - 1];
	}

	/**
	 * 复制一份
	 * @param reservedMappedFiles 保留的映射文件
	 * @return
	 */
	private Object[] copyMappedFiles(final int reservedMappedFiles) {
		Object[] mfs;

		if (this.mappedFiles.size() <= reservedMappedFiles) {
			return null;
		}

		mfs = this.mappedFiles.toArray();
		return mfs;
	}

	/**
	 * 截断脏文件
	 * @param offset
	 */
	public void truncateDirtyFiles(long offset) {
		List<MappedFile> willRemoveFiles = new ArrayList<MappedFile>();

		for (MappedFile file : this.mappedFiles) {
			long fileTailOffset = file.getFileFromOffset() + this.mappedFileSize;
			if (fileTailOffset > offset) {
				if (offset >= file.getFileFromOffset()) {
					file.setWrotePosition((int) (offset % this.mappedFileSize));
					file.setCommittedPosition((int) (offset % this.mappedFileSize));
					file.setFlushedPosition((int) (offset % this.mappedFileSize));
				}
				else {
					// 销毁
					file.destroy(1000);
					willRemoveFiles.add(file);
				}
			}
		}
		// 删除文件
		this.deleteExpiredFile(willRemoveFiles);
	}

	/**
	 * 批量删除文件
	 * @param files
	 */
	void deleteExpiredFile(List<MappedFile> files) {
		if (!files.isEmpty()) {
			Iterator<MappedFile> iterator = files.iterator();
			while (iterator.hasNext()) {
				MappedFile cur = iterator.next();
				if (!this.mappedFiles.contains(cur)) {
					iterator.remove();
					log.info("This mappedFile {} is not contained by mappedFiles, so skip it.", cur.getFileName());
				}
			}
			try {
				if (!this.mappedFiles.removeAll(files)) {
					log.error("deleteExpiredFile remove failed.");
				}
			}
			catch (Exception e) {
				log.error("deleteExpiredFile has exception.", e);
			}
		}
	}

	/**
	 * 加载存储目录中的备份文件
	 * @return
	 */
	public boolean load() {
		File dir = new File(this.storePath);
		File[] ls = dir.listFiles();
		if (ls != null) {
			return doLoad(Arrays.asList(ls));
		}
		return true;
	}

	public boolean doLoad(List<File> files) {
		// ascending order
		files.sort(Comparator.comparing(File::getName));
		for (File file : files) {
			if (file.length() != this.mappedFileSize) {
				log.warn(file + "\t" + file.length()
						+ " length not matched message store config value, please check it manually");
				return false;
			}
			try {
				MappedFile mappedFile = new MappedFile(file.getPath(), mappedFileSize);

				mappedFile.setWrotePosition(this.mappedFileSize);
				mappedFile.setFlushedPosition(this.mappedFileSize);
				mappedFile.setCommittedPosition(this.mappedFileSize);
				this.mappedFiles.add(mappedFile);
				log.info("load " + file.getPath() + " OK");
			}
			catch (IOException e) {
				log.error("load file " + file + " error", e);
				return false;
			}
		}
		return true;
	}

	/**
	 * @return
	 */
	public long howMuchFallBehind() {
		if (this.mappedFiles.isEmpty())
			return 0;

		long committed = this.flushedWhere;
		if (committed != 0) {
			MappedFile mappedFile = this.getLastMappedFile(0, false);
			if (mappedFile != null) {
				return (mappedFile.getFileFromOffset() + mappedFile.getWrotePosition()) - committed;
			}
		}

		return 0;
	}

	/**
	 * 获取队尾的文件，一般排在前面的文件都已经用满了
	 * @param startOffset
	 * @param needCreate
	 * @return
	 */
	public MappedFile getLastMappedFile(final long startOffset, boolean needCreate) {
		// 创建偏移量初始化不可用 -1
		long createOffset = -1;
		MappedFile mappedFileLast = getLastMappedFile();

		// 若不存在,需要创建
		if (mappedFileLast == null) {
			//例如每个文件大小为1024kb，要记录索引为startOffset，那么就要找这个startOffset起始后面能写的文件，
			// 例如2000在1024-2047之间，就找0000000000000001024这个文件
			// startOffset不是以mappedFileSize为单位，减去余数，创建索引为mappedFileSize的整数倍数
			createOffset = startOffset - (startOffset % this.mappedFileSize);
		}
		// 若存在，但是这个文件已经用满了，也要创建
		if (mappedFileLast != null && mappedFileLast.isFull()) {
			createOffset = mappedFileLast.getFileFromOffset() + this.mappedFileSize;
		}

		// 创建偏移量可用
		if (createOffset != -1 && needCreate) {
			return tryCreateMappedFile(createOffset);
		}

		return mappedFileLast;
	}

	/**
	 * 获取最后一个映射文件
	 * <p>
	 * 这个方法可以用来初始化头文件，指定起始偏移量
	 * </p>
	 * @param startOffset
	 * @return
	 */
	public MappedFile getLastMappedFile(final long startOffset) {
		return getLastMappedFile(startOffset, true);
	}

	public MappedFile getLastMappedFile() {
		MappedFile mappedFileLast = null;

		while (!this.mappedFiles.isEmpty()) {
			try {
				mappedFileLast = this.mappedFiles.get(this.mappedFiles.size() - 1);
				break;
			}
			catch (IndexOutOfBoundsException e) {
				// continue;
			}
			catch (Exception e) {
				log.error("getLastMappedFile has exception.", e);
				break;
			}
		}

		return mappedFileLast;
	}

	/**
	 * 创建文件
	 * @param createOffset
	 * @return
	 */
	protected MappedFile tryCreateMappedFile(long createOffset) {
		String nextFilePath = this.storePath + File.separator + UtilAll.offset2FileName(createOffset);
		String nextNextFilePath = this.storePath + File.separator
				+ UtilAll.offset2FileName(createOffset + this.mappedFileSize);
		return doCreateMappedFile(nextFilePath, nextNextFilePath);
	}

	protected MappedFile doCreateMappedFile(String nextFilePath, String nextNextFilePath) {
		MappedFile mappedFile = null;

		// 交付于业务线程创建文件
		if (this.allocateMappedFileService != null) {
			mappedFile = this.allocateMappedFileService.putRequestAndReturnMappedFile(nextFilePath, nextNextFilePath,
					this.mappedFileSize);
		}
		else {
			// 直接创建
			try {
				mappedFile = new MappedFile(nextFilePath, this.mappedFileSize);
			}
			catch (IOException e) {
				log.error("create mappedFile exception", e);
			}
		}

		// 创建成功
		if (mappedFile != null) {
			if (this.mappedFiles.isEmpty()) {
				mappedFile.setFirstCreateInQueue(true);
			}
			this.mappedFiles.add(mappedFile);
		}

		return mappedFile;
	}

	/**
	 * 获取总共映射的内存大小
	 * @return
	 */
	public long getMappedMemorySize() {
		long size = 0;

		Object[] mfs = this.copyMappedFiles(0);
		if (mfs != null) {
			for (Object mf : mfs) {
				if (((ReferenceResource) mf).isAvailable()) {
					size += this.mappedFileSize;
				}
			}
		}

		return size;
	}

	/**
	 * 重置偏移量，让读写从offset开始
	 * @param offset
	 * @return
	 */
	public boolean resetOffset(long offset) {
		MappedFile mappedFileLast = getLastMappedFile();
		if (mappedFileLast != null) {
			long lastOffset = mappedFileLast.getFileFromOffset() + mappedFileLast.getWrotePosition();
			// 要切除的
			long diff = lastOffset - offset;

			final int maxDiff = this.mappedFileSize * 2;
			// 若切除的大小超过this.mappedFileSize * 2，拒绝切除
			if (diff > maxDiff)
				return false;
		}

		ListIterator<MappedFile> iterator = this.mappedFiles.listIterator();
		while (iterator.hasPrevious()) {
			mappedFileLast = iterator.previous();
			// 只要找到offset所处的一块,然后break
			if (offset >= mappedFileLast.getFileFromOffset()) {
				// 截断在offset处
				int where = (int) (offset % mappedFileLast.getFileSize());
				mappedFileLast.setFlushedPosition(where);
				mappedFileLast.setWrotePosition(where);
				mappedFileLast.setCommittedPosition(where);
				// break
				break;
			}
			else {
				// 在offset后的移除
				iterator.remove();
			}
		}
		return true;
	}

	/**
	 * 起始偏移量
	 * @return
	 */
	public long getMinOffset() {

		if (!this.mappedFiles.isEmpty()) {
			try {
				return this.mappedFiles.get(0).getFileFromOffset();
			}
			catch (IndexOutOfBoundsException e) {
				// continue;
			}
			catch (Exception e) {
				log.error("getMinOffset has exception.", e);
			}
		}
		return -1;
	}

	/**
	 * 结尾偏移量
	 * @return
	 */
	public long getMaxOffset() {
		MappedFile mappedFile = getLastMappedFile();
		if (mappedFile != null) {
			return mappedFile.getFileFromOffset() + mappedFile.getReadPosition();
		}
		return 0;
	}

	/**
	 * 结尾已写偏移量
	 * @return
	 */
	public long getMaxWrotePosition() {
		MappedFile mappedFile = getLastMappedFile();
		if (mappedFile != null) {
			return mappedFile.getFileFromOffset() + mappedFile.getWrotePosition();
		}
		return 0;
	}

	public long remainHowManyDataToCommit() {
		return getMaxWrotePosition() - committedWhere;
	}

	public long remainHowManyDataToFlush() {
		return getMaxOffset() - flushedWhere;
	}

	public void deleteLastMappedFile() {
		MappedFile lastMappedFile = getLastMappedFile();
		if (lastMappedFile != null) {
			lastMappedFile.destroy(1000);
			this.mappedFiles.remove(lastMappedFile);
			log.info("on recover, destroy a logic mapped file " + lastMappedFile.getFileName());

		}
	}

	public int deleteExpiredFileByTime(final long expiredTime, final int deleteFilesInterval,
			final long intervalForcibly, final boolean cleanImmediately) {
		Object[] mfs = this.copyMappedFiles(0);

		if (null == mfs)
			return 0;

		int mfsLength = mfs.length - 1;
		int deleteCount = 0;
		List<MappedFile> files = new ArrayList<MappedFile>();
		if (null != mfs) {
			for (int i = 0; i < mfsLength; i++) {
				MappedFile mappedFile = (MappedFile) mfs[i];
				long liveMaxTimestamp = mappedFile.getLastModifiedTimestamp() + expiredTime;
				if (System.currentTimeMillis() >= liveMaxTimestamp || cleanImmediately) {
					if (mappedFile.destroy(intervalForcibly)) {
						files.add(mappedFile);
						deleteCount++;

						if (files.size() >= DELETE_FILES_BATCH_MAX) {
							break;
						}

						if (deleteFilesInterval > 0 && (i + 1) < mfsLength) {
							try {
								Thread.sleep(deleteFilesInterval);
							}
							catch (InterruptedException e) {
							}
						}
					}
					else {
						break;
					}
				}
				else {
					// avoid deleting files in the middle
					break;
				}
			}
		}

		deleteExpiredFile(files);

		return deleteCount;
	}

	/**
	 * 通过偏移量删除映射文件
	 * @param offset
	 * @param unitSize 记录大小的长度
	 * @return
	 */
	public int deleteExpiredFileByOffset(long offset, int unitSize) {
		Object[] mfs = this.copyMappedFiles(0);

		List<MappedFile> files = new ArrayList<MappedFile>();
		int deleteCount = 0;
		if (null != mfs) {

			int mfsLength = mfs.length - 1;

			for (int i = 0; i < mfsLength; i++) {
				boolean destroy;
				MappedFile mappedFile = (MappedFile) mfs[i];
				// 获取文件最后 unitSize 个字节
				SelectMappedBufferResult result = mappedFile.selectMappedBuffer(this.mappedFileSize - unitSize);
				if (result != null) {
					// 读取这20个字节
					long maxOffsetInLogicQueue = result.getByteBuffer().getLong();
					result.release();
					// 如果读取的出来的数值小于offset，则说明要销毁
					destroy = maxOffsetInLogicQueue < offset;
					if (destroy) {
						log.info("physic min offset " + offset + ", logics in current mappedFile max offset "
								+ maxOffsetInLogicQueue + ", delete it");
					}
				}
				else if (!mappedFile.isAvailable()) { // Handle hanged file.
					log.warn("Found a hanged consume queue file, attempting to delete it.");
					destroy = true;
				}
				else {
					log.warn("this being not executed forever.");
					break;
				}

				// 统计删除哪些
				if (destroy && mappedFile.destroy(1000 * 60)) {
					files.add(mappedFile);
					deleteCount++;
				}
				else {
					break;
				}
			}
		}
		// 删除
		deleteExpiredFile(files);

		return deleteCount;
	}

	/**
	 * 根据this.flushedWhere的位置，找到对应的文件，刷新到磁盘
	 * 同步和异步刷盘服务，最终都是调用MappedFileQueue#flush方法执行刷盘，该方法内部最终又是通过mappedFile#flush方法刷盘的。
	 * @param flushLeastPages
	 * @return
	 */
	public boolean flush(final int flushLeastPages) {
		boolean result = true;
		//根据最新刷盘物理位置flushedWhere，去找到对应的MappedFile。如果flushedWhere为0，表示还没有开始写消息，则获取第一个MappedFile
		MappedFile mappedFile = this.findMappedFileByOffset(this.flushedWhere, this.flushedWhere == 0);
		if (mappedFile != null) {
			//获取存储时间戳，storeTimestamp在appendMessagesInner方法中被更新
			long tmpTimeStamp = mappedFile.getStoreTimestamp();
			/*
			 * 执行刷盘操作
			 */
			int offset = mappedFile.flush(flushLeastPages);
			//获取最新刷盘物理偏移量
			long where = mappedFile.getFileFromOffset() + offset;
			//刷盘结果
			result = where == this.flushedWhere;
			this.flushedWhere = where;
			if (0 == flushLeastPages) {
				//如果最少刷盘页数为0，则更新存储时间戳
				this.storeTimestamp = tmpTimeStamp;
			}
		}

		return result;
	}

	/**
	 异步堆外缓存刷盘优化：
	 1.在异步刷盘的时候，可以开启异步堆外缓存刷盘机制，异步堆外缓存刷盘服务并不会真正的执行flush刷盘，而是调用commit方法提交数据到fileChannel。
	 2.开启了异步堆外缓存服务之后，写数据的时候写入堆外缓存writeBuffer中，而读取数据始终从MappedByteBuffer中读取，
	 这也是一种读写分离机制。二者通过异步堆外缓存刷盘服务CommitRealTimeService实现数据同步，
	 该服务异步（最多200ms执行一次）的将堆外缓存writeBuffer中的脏数据提交到commitLog文件的文件通道FileChannel中，
	 而该文件被执行了内存映射mmap操作，因此可以从对应的MappedByteBuffer中直接获取提交到FileChannel的数据，但仍有延迟。
	 3.高并发下频繁写入 page cache 可能会造成刷脏页时磁盘压力较高，导致写入时出现毛刺现象。
	 读写分离能缓解频繁写page cache 的压力，但会增加消息不一致的风险，使得数据一致性降低到最低。
	 * @param commitLeastPages 最少提交的页数
	 * @return false表示提交了部分数据
	 */
	public boolean commit(final int commitLeastPages) {
		boolean result = true;
		//根据最新提交物理位置committedWhere，去找到对应的MappedFile。如果committedWhere为0，表示还没有开始提交消息，则获取第一个MappedFile
		MappedFile mappedFile = this.findMappedFileByOffset(this.committedWhere, this.committedWhere == 0);
		if (mappedFile != null) {
			/*
			 * 执行提交操作
			 */
			int offset = mappedFile.commit(commitLeastPages);
			//获取最新提交物理偏移量
			long where = mappedFile.getFileFromOffset() + offset;
			//如果不相等，表示提交了部分数据
			result = where == this.committedWhere;
			//更新提交物理位置
			this.committedWhere = where;
		}

		return result;
	}

	/**
	 * 取出第一个映射文件
	 * @return
	 */
	public MappedFile getFirstMappedFile() {
		MappedFile mappedFileFirst = null;
		if (!this.mappedFiles.isEmpty()) {
			try {
				mappedFileFirst = this.mappedFiles.get(0);
			}
			catch (IndexOutOfBoundsException e) {
				// ignore
			}
			catch (Exception e) {
				log.error("getFirstMappedFile has exception.", e);
			}
		}

		return mappedFileFirst;
	}

	public MappedFile findMappedFileByOffset(final long offset) {
		return findMappedFileByOffset(offset, false);
	}

	/**
	 * 按偏移量查找映射文件
	 * @param offset 偏移量
	 * @param returnFirstOnNotFound 如果未找到映射文件，则返回第一个。
	 * @return 映射文件或 null（当未找到且 returnFirstOnNotFound 为false时）。
	 */
	public MappedFile findMappedFileByOffset(final long offset, final boolean returnFirstOnNotFound) {
		try {
			// 第一个
			MappedFile firstMappedFile = this.getFirstMappedFile();
			// 最后一个
			MappedFile lastMappedFile = this.getLastMappedFile();
			if (firstMappedFile != null && lastMappedFile != null) {
				// 如果偏移量不在已存在的文件偏移量范围内
				if (offset < firstMappedFile.getFileFromOffset()
						|| offset >= lastMappedFile.getFileFromOffset() + this.mappedFileSize) {
					LOG_ERROR.warn(
							"Offset not matched. Request offset: {}, firstOffset: {}, lastOffset: {}, mappedFileSize: {}, mappedFiles count: {}",
							offset, firstMappedFile.getFileFromOffset(),
							lastMappedFile.getFileFromOffset() + this.mappedFileSize, this.mappedFileSize,
							this.mappedFiles.size());
				}
				else {
					// 偏移量存在
					// 存在于第几个文件
					int index = (int) ((offset / this.mappedFileSize)
							- (firstMappedFile.getFileFromOffset() / this.mappedFileSize));
					MappedFile targetFile = null;
					try {
						// 取出文件
						targetFile = this.mappedFiles.get(index);
					}
					catch (Exception ignored) {
					}
					// 偏移量确实在于改文件内
					if (targetFile != null && offset >= targetFile.getFileFromOffset()
							&& offset < targetFile.getFileFromOffset() + this.mappedFileSize) {
						return targetFile;
					}

					// 如果上面还是没找出来，直接遍历
					for (MappedFile tmpMappedFile : this.mappedFiles) {
						if (offset >= tmpMappedFile.getFileFromOffset()
								&& offset < tmpMappedFile.getFileFromOffset() + this.mappedFileSize) {
							return tmpMappedFile;
						}
					}
				}

				if (returnFirstOnNotFound) {
					return firstMappedFile;
				}
			}
		}
		catch (Exception e) {
			log.error("findMappedFileByOffset Exception", e);
		}

		return null;
	}

	/**
	 * 重试删除第一个映射文件
	 * @param intervalForcibly
	 * @return
	 */
	public boolean retryDeleteFirstFile(final long intervalForcibly) {
		MappedFile mappedFile = this.getFirstMappedFile();
		if (mappedFile != null) {
			if (!mappedFile.isAvailable()) {
				log.warn("the mappedFile was destroyed once, but still alive, " + mappedFile.getFileName());
				// 销毁
				boolean result = mappedFile.destroy(intervalForcibly);
				if (result) {
					log.info("the mappedFile re delete OK, " + mappedFile.getFileName());
					List<MappedFile> tmpFiles = new ArrayList<MappedFile>();
					tmpFiles.add(mappedFile);
					// 批量删除文件
					this.deleteExpiredFile(tmpFiles);
				}
				else {
					log.warn("the mappedFile re delete failed, " + mappedFile.getFileName());
				}

				return result;
			}
		}

		return false;
	}

	public void shutdown(final long intervalForcibly) {
		for (MappedFile mf : this.mappedFiles) {
			mf.shutdown(intervalForcibly);
		}
	}

	public void destroy() {
		for (MappedFile mf : this.mappedFiles) {
			mf.destroy(1000 * 3);
		}
		this.mappedFiles.clear();
		this.flushedWhere = 0;

		// delete parent directory
		File file = new File(storePath);
		if (file.isDirectory()) {
			file.delete();
		}
	}

	public long getFlushedWhere() {
		return flushedWhere;
	}

	public void setFlushedWhere(long flushedWhere) {
		this.flushedWhere = flushedWhere;
	}

	public long getStoreTimestamp() {
		return storeTimestamp;
	}

	public List<MappedFile> getMappedFiles() {
		return mappedFiles;
	}

	public int getMappedFileSize() {
		return mappedFileSize;
	}

	public long getCommittedWhere() {
		return committedWhere;
	}

	public void setCommittedWhere(final long committedWhere) {
		this.committedWhere = committedWhere;
	}

	public String getStorePath() {
		return storePath;
	}

	public AllocateMappedFileService getAllocateMappedFileService() {
		return allocateMappedFileService;
	}

}
