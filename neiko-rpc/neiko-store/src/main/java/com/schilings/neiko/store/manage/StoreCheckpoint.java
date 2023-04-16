package com.schilings.neiko.store.manage;

import com.schilings.neiko.logging.InternalLogger;
import com.schilings.neiko.logging.InternalLoggerFactory;
import com.schilings.neiko.store.MappedFile;
import com.schilings.neiko.svrutil.UtilAll;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * <p>
 * </p>
 *
 * @author Schilings
 */
public class StoreCheckpoint {

	private static final InternalLogger log = InternalLoggerFactory.getLogger(StoreCheckpoint.class);

	private final RandomAccessFile randomAccessFile;

	private final FileChannel fileChannel;

	private final MappedByteBuffer mappedByteBuffer;

	/**
	 * 物理时间戳
	 */
	private volatile long physicMsgTimestamp = 0;

	/**
	 * 逻辑时间戳
	 */
	private volatile long logicsMsgTimestamp = 0;

	private volatile long indexMsgTimestamp = 0;

	public StoreCheckpoint(final String scpPath) throws IOException {
		File file = new File(scpPath);
		MappedFile.ensureDirOK(file.getParent());
		boolean fileExists = file.exists();

		this.randomAccessFile = new RandomAccessFile(file, "rw");
		this.fileChannel = this.randomAccessFile.getChannel();
		// 4字节 限制只能操作4字节
		this.mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, MappedFile.OS_PAGE_SIZE);

		if (fileExists) {
			log.info("store checkpoint file exists, " + scpPath);
			this.physicMsgTimestamp = this.mappedByteBuffer.getLong(0);
			this.logicsMsgTimestamp = this.mappedByteBuffer.getLong(8);
			this.indexMsgTimestamp = this.mappedByteBuffer.getLong(16);

			log.info("store checkpoint file physicMsgTimestamp " + this.physicMsgTimestamp + ", "
					+ UtilAll.timeMillisToHumanString(this.physicMsgTimestamp));
			log.info("store checkpoint file logicsMsgTimestamp " + this.logicsMsgTimestamp + ", "
					+ UtilAll.timeMillisToHumanString(this.logicsMsgTimestamp));
			log.info("store checkpoint file indexMsgTimestamp " + this.indexMsgTimestamp + ", "
					+ UtilAll.timeMillisToHumanString(this.indexMsgTimestamp));
		}
		else {
			log.info("store checkpoint file not exists, " + scpPath);
		}
	}

	public void shutdown() {
		// 刷入磁盘
		this.flush();
		// 取消映射 mappedByteBuffer
		MappedFile.clean(this.mappedByteBuffer);
		try {
			// 关闭通道
			this.fileChannel.close();
		}
		catch (IOException e) {
			log.error("Failed to properly close the channel", e);
		}
	}

	public void flush() {
		this.mappedByteBuffer.putLong(0, this.physicMsgTimestamp);
		this.mappedByteBuffer.putLong(8, this.logicsMsgTimestamp);
		this.mappedByteBuffer.putLong(16, this.indexMsgTimestamp);
		this.mappedByteBuffer.force();
	}

	public long getPhysicMsgTimestamp() {
		return physicMsgTimestamp;
	}

	public void setPhysicMsgTimestamp(long physicMsgTimestamp) {
		this.physicMsgTimestamp = physicMsgTimestamp;
	}

	public long getLogicsMsgTimestamp() {
		return logicsMsgTimestamp;
	}

	public void setLogicsMsgTimestamp(long logicsMsgTimestamp) {
		this.logicsMsgTimestamp = logicsMsgTimestamp;
	}

	public long getMinTimestampIndex() {
		return Math.min(this.getMinTimestamp(), this.indexMsgTimestamp);
	}

	/**
	 * @return
	 */
	public long getMinTimestamp() {
		long min = Math.min(this.physicMsgTimestamp, this.logicsMsgTimestamp);
		// 减去3000 ms
		min -= 1000 * 3;
		if (min < 0)
			min = 0;

		return min;
	}

	public long getIndexMsgTimestamp() {
		return indexMsgTimestamp;
	}

	public void setIndexMsgTimestamp(long indexMsgTimestamp) {
		this.indexMsgTimestamp = indexMsgTimestamp;
	}

}
