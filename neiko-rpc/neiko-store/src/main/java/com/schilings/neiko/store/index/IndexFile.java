/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.schilings.neiko.store.index;

import com.schilings.neiko.logging.InternalLogger;
import com.schilings.neiko.logging.InternalLoggerFactory;
import com.schilings.neiko.store.MappedFile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.List;

public class IndexFile {

	private static final InternalLogger log = InternalLoggerFactory.getLogger(IndexFile.class);

	private static int hashSlotSize = 4;// 槽的大小，4个字节

	private static int indexSize = 20; // 索引大小，20字节 4+4+8+4

	private static int invalidIndex = 0; // 无效索引

	/**
	 * IndexFile ----> Hash Map
	 * 一个槽对应多个索引（索引单链表），每个索引会记录上一个索引的顺序值（顺序下来在第几个的，从1开始，0为无效索引，第一个的索引记录的前缀就是0）
	 * 每个槽的值记录的是索引单链表最后一个元素的顺序值，
	 */

	private final int hashSlotNum;

	private final int indexNum;

	private final MappedFile mappedFile;

	private final FileChannel fileChannel;

	private final MappedByteBuffer mappedByteBuffer;

	private final IndexHeader indexHeader;

	public IndexFile(final String fileName, final int hashSlotNum, final int indexNum, final long endPhyOffset,
			final long endTimestamp) throws IOException {
		int fileTotalSize = IndexHeader.INDEX_HEADER_SIZE + (hashSlotNum * hashSlotSize) + (indexNum * indexSize);
		this.mappedFile = new MappedFile(fileName, fileTotalSize);
		this.fileChannel = this.mappedFile.getFileChannel();
		this.mappedByteBuffer = this.mappedFile.getMappedByteBuffer();
		this.hashSlotNum = hashSlotNum;
		this.indexNum = indexNum;

		// 将index文件的直接内存mappedByteBuffer传入给indexHeader操作，用于实时同步更新Index文件的头部信息
		ByteBuffer byteBuffer = this.mappedByteBuffer.slice();
		this.indexHeader = new IndexHeader(byteBuffer);

		if (endPhyOffset > 0) {
			this.indexHeader.setBeginPhyOffset(endPhyOffset);
			this.indexHeader.setEndPhyOffset(endPhyOffset);
		}

		if (endTimestamp > 0) {
			this.indexHeader.setBeginTimestamp(endTimestamp);
			this.indexHeader.setEndTimestamp(endTimestamp);
		}
	}

	public String getFileName() {
		return this.mappedFile.getFileName();
	}

	public void load() {
		this.indexHeader.load();
	}

	public void flush() {
		long beginTime = System.currentTimeMillis();
		if (this.mappedFile.hold()) {
			this.indexHeader.updateByteBuffer();
			this.mappedByteBuffer.force();
			this.mappedFile.release();
			log.info("flush index file elapsed time(ms) " + (System.currentTimeMillis() - beginTime));
		}
	}

	public boolean isWriteFull() {
		return this.indexHeader.getIndexCount() >= this.indexNum;
	}

	public boolean destroy(final long intervalForcibly) {
		return this.mappedFile.destroy(intervalForcibly);
	}

	public boolean putKey(final String key, final long phyOffset, final long storeTimestamp) {
			// 还没满
		if (this.indexHeader.getIndexCount() < this.indexNum) {
			int keyHash = indexKeyHashMethod(key);// key的hash值
			int slotPos = keyHash % this.hashSlotNum;// 对应的第几个hash槽
			int absSlotPos = IndexHeader.INDEX_HEADER_SIZE + slotPos * hashSlotSize;// 对应的hash槽位置

			FileLock fileLock = null;

			try {

				
				fileLock = this.fileChannel.lock(absSlotPos, hashSlotSize,false);

				// hash槽内的值,hash槽内的放置的是对应的索引的位置
				int slotValue = this.mappedByteBuffer.getInt(absSlotPos);
				// 如果槽的值是无效索引，则标记为无效索引0
				if (slotValue <= invalidIndex || slotValue > this.indexHeader.getIndexCount()) {
					slotValue = invalidIndex;// 如果取出来的索引值无效，则标记为无效索引0
				}
				// 时间差
				long timeDiff = storeTimestamp - this.indexHeader.getBeginTimestamp();
				// 舍入到秒，减少存储空间
				timeDiff = timeDiff / 1000;
				// 如果indexHeader记录的开始时间戳<=0，说明这是第一次putKey
				if (this.indexHeader.getBeginTimestamp() <= 0) {
					timeDiff = 0;
				}
				else if (timeDiff > Integer.MAX_VALUE) {
					timeDiff = Integer.MAX_VALUE;
				}
				else if (timeDiff < 0) {
					timeDiff = 0;
				}
				// 索引的存放末端位置,用于在后面添加索引
				int absIndexPos = IndexHeader.INDEX_HEADER_SIZE + this.hashSlotNum * hashSlotSize
						+ this.indexHeader.getIndexCount() * indexSize;
				// 放置索引值
				this.mappedByteBuffer.putInt(absIndexPos, keyHash);// 4个字节：key的hash值
				this.mappedByteBuffer.putLong(absIndexPos + 4, phyOffset);// 8个字节:要存放的偏移量
				this.mappedByteBuffer.putInt(absIndexPos + 4 + 8, (int) timeDiff);// 4个字节：与上次存放时间的差，第一次存放为0
				this.mappedByteBuffer.putInt(absIndexPos + 4 + 8 + 4, slotValue);// 4个字节，同一个槽的索引单链表下的前缀
				// 放置hash槽的值
				this.mappedByteBuffer.putInt(absSlotPos, this.indexHeader.getIndexCount());// 4个字节，该索引对应的第几个索引(记录的是链表尾结点)
				// 如果是第一个索引
				if (this.indexHeader.getIndexCount() <= 1) {
					// 设置开始偏移量和时间，以存储的第一个索引为基准
					this.indexHeader.setBeginPhyOffset(phyOffset);
					this.indexHeader.setBeginTimestamp(storeTimestamp);
				}
				// 如果是第一个索引
				if (invalidIndex == slotValue) {
					this.indexHeader.incHashSlotCount();// 槽的使用数+1
				}
				this.indexHeader.incIndexCount();// 索引数+1
				this.indexHeader.setEndPhyOffset(phyOffset);
				this.indexHeader.setEndTimestamp(storeTimestamp);

				return true;
			}
			catch (Exception e) {
				log.error("putKey exception, Key: " + key + " KeyHashCode: " + key.hashCode(), e);
			}
			finally {
				if (fileLock != null) {
					try {
						fileLock.release();
					}
					catch (IOException e) {
						log.error("Failed to release the lock", e);
					}
				}
			}
		}
		else {
			log.warn("Over index file capacity: index count = " + this.indexHeader.getIndexCount()
					+ "; index max num = " + this.indexNum);
		}

		return false;
	}

	public int indexKeyHashMethod(final String key) {
		int keyHash = key.hashCode();
		int keyHashPositive = Math.abs(keyHash);
		if (keyHashPositive < 0)
			keyHashPositive = 0;
		return keyHashPositive;
	}

	public long getBeginTimestamp() {
		return this.indexHeader.getBeginTimestamp();
	}

	public long getEndTimestamp() {
		return this.indexHeader.getEndTimestamp();
	}

	public long getEndPhyOffset() {
		return this.indexHeader.getEndPhyOffset();
	}

	public boolean isTimeMatched(final long begin, final long end) {
		boolean result = begin < this.indexHeader.getBeginTimestamp() && end > this.indexHeader.getEndTimestamp();
		result = result
				|| (begin >= this.indexHeader.getBeginTimestamp() && begin <= this.indexHeader.getEndTimestamp());
		result = result || (end >= this.indexHeader.getBeginTimestamp() && end <= this.indexHeader.getEndTimestamp());
		return result;
	}

	/**
	 * 通过时间区间查找偏移量记录
	 * @param phyOffsets 用来存放查出来的偏移量
	 * @param key key
	 * @param maxNum 最多查多少个
	 * @param begin
	 * @param end
	 * @param lock 是否上锁
	 */
	public void selectPhyOffset(final List<Long> phyOffsets, final String key, final int maxNum, final long begin,
			final long end, boolean lock) {
		if (this.mappedFile.hold()) {// +1
			int keyHash = indexKeyHashMethod(key);// key的hash值
			int slotPos = keyHash % this.hashSlotNum;// key对应的hash槽的第几个
			int absSlotPos = IndexHeader.INDEX_HEADER_SIZE + slotPos * hashSlotSize;// key对应的hash槽的位置

			FileLock fileLock = null;
			try {
				if (lock) {
					fileLock = this.fileChannel.lock(absSlotPos, hashSlotSize, true);
				}
				//
				int slotValue = this.mappedByteBuffer.getInt(absSlotPos);
				if (fileLock != null) {
					fileLock.release();
					fileLock = null;
				}
				if (slotValue <= invalidIndex || slotValue > this.indexHeader.getIndexCount()
						|| this.indexHeader.getIndexCount() <= 1) {
					// 什么都不做
				}
				else {
					//
					for (int nextIndexToRead = slotValue;;) {
						if (phyOffsets.size() >= maxNum) {// 查出的个数到达的给定的最大个数
							// 退出
							break;
						}
						// 下一个索引的位置
						int absIndexPos = IndexHeader.INDEX_HEADER_SIZE + this.hashSlotNum * hashSlotSize
								+ nextIndexToRead * indexSize;
						// 4字节：key的hash值
						int keyHashRead = this.mappedByteBuffer.getInt(absIndexPos);
						// 8字节：存放的偏移量
						long phyOffsetRead = this.mappedByteBuffer.getLong(absIndexPos + 4);
						// 4字节：存放的时间差
						long timeDiff = (long) this.mappedByteBuffer.getInt(absIndexPos + 4 + 8);
						// 4字节：链表前缀
						int prevIndexRead = this.mappedByteBuffer.getInt(absIndexPos + 4 + 8 + 4);

						// 时间差小于0，明显有错
						if (timeDiff < 0) {
							break;
						}
						// 换单位成秒
						timeDiff *= 1000L;

						// 加上开始时间，获取存放时间
						long timeRead = this.indexHeader.getBeginTimestamp() + timeDiff;
						// 存放时间是否在查找区间内
						boolean timeMatched = (timeRead >= begin) && (timeRead <= end);
						// 是否符合查找条件
						if (keyHash == keyHashRead && timeMatched) {
							phyOffsets.add(phyOffsetRead);
						}

						// 无效索引 或者 同个索引 或者 时间不符合
						if (prevIndexRead <= invalidIndex || prevIndexRead > this.indexHeader.getIndexCount()
								|| prevIndexRead == nextIndexToRead || timeRead < begin) {
							// 退出
							break;
						}
						// 查找完，查找前一个索引
						nextIndexToRead = prevIndexRead;
					}
				}
			}
			catch (Exception e) {
				log.error("selectPhyOffset exception ", e);
			}
			finally {
				if (fileLock != null) {
					try {
						fileLock.release();
					}
					catch (IOException e) {
						log.error("Failed to release the lock", e);
					}
				}

				this.mappedFile.release();
			}
		}
	}

}
