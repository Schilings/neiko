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

package com.schilings.neiko.store;

import com.schilings.neiko.store.manage.StoreData;
import com.schilings.neiko.svrutil.UtilAll;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
//--add-opens java.base/java.nio=ALL-UNNAMED --add-exports java.base/jdk.internal.ref=ALL-UNNAMED --add-exports java.base/sun.nio.ch=ALL-UNNAMED
public class MappedFileQueueTest {

	private static final int STORE_UNIT_SIZE = 20;

	@After
	public void destory() {
		File file = new File("target/unit_test_store");
		UtilAll.deleteFile(file);
	}

	@Test
	public void testGetLastMappedFile() {
		final String fixedMsg = "0123456789abcdef";

		MappedFileQueue mappedFileQueue = new MappedFileQueue("target\\unit_test_store\\a", 1024, null);

		for (int i = 0; i < 1024; i++) {
			MappedFile mappedFile = mappedFileQueue.getLastMappedFile(0);
			assertThat(mappedFile).isNotNull();
			//assertThat(mappedFile.append(fixedMsg.getBytes())).isTrue();
			mappedFile.append(new StoreData(fixedMsg.getBytes()),new AppendCallback() {
				@Override
				public AppendResult doAppend(long fileFromOffset, ByteBuffer byteBuffer, int maxBlank, StoreData data) {
					final long beginTimeMills = System.currentTimeMillis();
					// PHY OFFSET
					long wroteOffset = fileFromOffset + byteBuffer.position();
					byteBuffer.put(data.getBody());
					return new AppendResult(AppendStatus.PUT_OK, wroteOffset, data.getBody().length,
							System.currentTimeMillis(), System.currentTimeMillis() - beginTimeMills);
				}
			});
		}
		
		mappedFileQueue.shutdown(1000);
		mappedFileQueue.destroy();
	}

	@Test
	public void testMappedFileAppendWithWriteBuffer() {
		final String fixedMsg = "0123456789abcdef";

		TransientStorePool transientStorePool = new TransientStorePool(5, 1024 * 1024);
		transientStorePool.init();
		AllocateMappedFileService allocateMappedFileService = new AllocateMappedFileService(transientStorePool);
		allocateMappedFileService.setWarmMappedFileEnbaled(true);
		allocateMappedFileService.setMappedFileSize(1024);
		allocateMappedFileService.setFlushLeastPagesWhenWarmMapedFile(1024);
		allocateMappedFileService.start();
		MappedFileQueue mappedFileQueue = new MappedFileQueue("target\\unit_test_store\\a", 1024,
				allocateMappedFileService);
		
		for (int i = 0; i < 1024; i++) {
			MappedFile mappedFile = mappedFileQueue.getLastMappedFile(0);
			assertThat(mappedFile).isNotNull();
			mappedFile.append(new StoreData(fixedMsg.getBytes()),new AppendCallback() {
				@Override
				public AppendResult doAppend(long fileFromOffset, ByteBuffer byteBuffer, int maxBlank, StoreData data) {
					final long beginTimeMills = System.currentTimeMillis();
					// PHY OFFSET
					long wroteOffset = fileFromOffset + byteBuffer.position();
					byteBuffer.put(data.getBody());
					return new AppendResult(AppendStatus.PUT_OK, wroteOffset, data.getBody().length,
							System.currentTimeMillis(), System.currentTimeMillis() - beginTimeMills);
				}
			});

			if ((i + 1) % 20 == 0) {
				mappedFileQueue.commit(0);
				mappedFileQueue.flush(0);
			}
		}
		mappedFileQueue.commit(0);
		mappedFileQueue.flush(0);
		mappedFileQueue.shutdown(1000);
		//mappedFileQueue.destroy();
	}

	@Test
	public void testGetLastMappedFile_With_AllocateMappedFileService() {
		final String fixedMsg = "0123456789abcdef";
		TransientStorePool transientStorePool = new TransientStorePool();
		AllocateMappedFileService allocateMappedFileService = new AllocateMappedFileService(transientStorePool);
		allocateMappedFileService.start();
		MappedFileQueue mappedFileQueue = new MappedFileQueue("target\\unit_test_store\\a", 1024,
				allocateMappedFileService);

		for (int i = 0; i < 1024; i++) {
			MappedFile mappedFile = mappedFileQueue.getLastMappedFile(0);
			assertThat(mappedFile).isNotNull();
			assertThat(mappedFile.append(fixedMsg.getBytes())).isTrue();
		}

		mappedFileQueue.shutdown(1000);
		mappedFileQueue.destroy();
	}

	@Test
	public void testFindMappedFileByOffset() {
		// four-byte string.
		final String fixedMsg = "abcd";

		MappedFileQueue mappedFileQueue = new MappedFileQueue("target\\unit_test_store\\b", 1024, null);

		for (int i = 0; i < 1024; i++) {
			MappedFile mappedFile = mappedFileQueue.getLastMappedFile(0);
			assertThat(mappedFile).isNotNull();
			assertThat(mappedFile.append(fixedMsg.getBytes())).isTrue();
		}

		assertThat(mappedFileQueue.getMappedMemorySize()).isEqualTo(fixedMsg.getBytes().length * 1024);

		MappedFile mappedFile = mappedFileQueue.findMappedFileByOffset(0);
		assertThat(mappedFile).isNotNull();
		assertThat(mappedFile.getFileFromOffset()).isEqualTo(0);

		mappedFile = mappedFileQueue.findMappedFileByOffset(100);
		assertThat(mappedFile).isNotNull();
		assertThat(mappedFile.getFileFromOffset()).isEqualTo(0);

		mappedFile = mappedFileQueue.findMappedFileByOffset(1024);
		assertThat(mappedFile).isNotNull();
		assertThat(mappedFile.getFileFromOffset()).isEqualTo(1024);

		mappedFile = mappedFileQueue.findMappedFileByOffset(1024 + 100);
		assertThat(mappedFile).isNotNull();
		assertThat(mappedFile.getFileFromOffset()).isEqualTo(1024);

		mappedFile = mappedFileQueue.findMappedFileByOffset(1024 * 2);
		assertThat(mappedFile).isNotNull();
		assertThat(mappedFile.getFileFromOffset()).isEqualTo(1024 * 2);

		mappedFile = mappedFileQueue.findMappedFileByOffset(1024 * 2 + 100);
		assertThat(mappedFile).isNotNull();
		assertThat(mappedFile.getFileFromOffset()).isEqualTo(1024 * 2);

		// over mapped memory size.
		mappedFile = mappedFileQueue.findMappedFileByOffset(1024 * 4);
		assertThat(mappedFile).isNull();

		mappedFile = mappedFileQueue.findMappedFileByOffset(1024 * 4 + 100);
		assertThat(mappedFile).isNull();

		mappedFileQueue.shutdown(1000);
		mappedFileQueue.destroy();
	}

	@Test
	public void testFindMappedFileByOffset_StartOffsetIsNonZero() {
		MappedFileQueue mappedFileQueue = new MappedFileQueue("target\\unit_test_store\\b", 1024, null);

		// Start from a non-zero offset
		MappedFile mappedFile = mappedFileQueue.getLastMappedFile(1024);
		assertThat(mappedFile).isNotNull();

		assertThat(mappedFileQueue.findMappedFileByOffset(1025)).isEqualTo(mappedFile);

		assertThat(mappedFileQueue.findMappedFileByOffset(0)).isNull();
		assertThat(mappedFileQueue.findMappedFileByOffset(123, false)).isNull();
		assertThat(mappedFileQueue.findMappedFileByOffset(123, true)).isEqualTo(mappedFile);

		assertThat(mappedFileQueue.findMappedFileByOffset(0, false)).isNull();
		assertThat(mappedFileQueue.findMappedFileByOffset(0, true)).isEqualTo(mappedFile);

		mappedFileQueue.shutdown(1000);
		mappedFileQueue.destroy();
	}

	@Test
	public void testappend() {
		final String fixedMsg = "0123456789abcdef";

		MappedFileQueue mappedFileQueue = new MappedFileQueue("target\\unit_test_store\\c", 1024, null);

		for (int i = 0; i < 1024; i++) {
			MappedFile mappedFile = mappedFileQueue.getLastMappedFile(0);
			assertThat(mappedFile).isNotNull();
			assertThat(mappedFile.append(fixedMsg.getBytes())).isTrue();
		}

		assertThat(mappedFileQueue.flush(0)).isFalse();
		assertThat(mappedFileQueue.getFlushedWhere()).isEqualTo(1024);

		assertThat(mappedFileQueue.flush(0)).isFalse();
		assertThat(mappedFileQueue.getFlushedWhere()).isEqualTo(1024 * 2);

		assertThat(mappedFileQueue.flush(0)).isFalse();
		assertThat(mappedFileQueue.getFlushedWhere()).isEqualTo(1024 * 3);

		assertThat(mappedFileQueue.flush(0)).isFalse();
		assertThat(mappedFileQueue.getFlushedWhere()).isEqualTo(1024 * 4);

		assertThat(mappedFileQueue.flush(0)).isFalse();
		assertThat(mappedFileQueue.getFlushedWhere()).isEqualTo(1024 * 5);

		assertThat(mappedFileQueue.flush(0)).isFalse();
		assertThat(mappedFileQueue.getFlushedWhere()).isEqualTo(1024 * 6);

		mappedFileQueue.shutdown(1000);
		mappedFileQueue.destroy();
	}

	@Test
	public void testGetMappedMemorySize() {
		final String fixedMsg = "abcd";

		MappedFileQueue mappedFileQueue = new MappedFileQueue("target\\unit_test_store\\d", 1024, null);

		for (int i = 0; i < 1024; i++) {
			MappedFile mappedFile = mappedFileQueue.getLastMappedFile(0);
			assertThat(mappedFile).isNotNull();
			assertThat(mappedFile.append(fixedMsg.getBytes())).isTrue();
		}

		assertThat(mappedFileQueue.getMappedMemorySize()).isEqualTo(fixedMsg.length() * 1024);
		mappedFileQueue.shutdown(1000);
		mappedFileQueue.destroy();
	}

	@Test
	public void testDeleteExpiredFileByOffset() {
		MappedFileQueue mappedFileQueue = new MappedFileQueue("target\\unit_test_store\\e", 5120, null);

		for (int i = 0; i < 2048; i++) {
			MappedFile mappedFile = mappedFileQueue.getLastMappedFile(0);
			assertThat(mappedFile).isNotNull();
			// 20字节
			ByteBuffer byteBuffer = ByteBuffer.allocate(STORE_UNIT_SIZE);
			byteBuffer.putLong(i);
			byte[] padding = new byte[12];
			Arrays.fill(padding, (byte) '0');
			byteBuffer.put(padding);
			byteBuffer.flip();
			assertThat(mappedFile.append(byteBuffer.array())).isTrue();
		}

		MappedFile first = mappedFileQueue.getFirstMappedFile();
		first.hold();

		assertThat(mappedFileQueue.deleteExpiredFileByOffset(20480, STORE_UNIT_SIZE)).isEqualTo(0);
		first.release();

		assertThat(mappedFileQueue.deleteExpiredFileByOffset(20480, STORE_UNIT_SIZE)).isGreaterThan(0);
		first = mappedFileQueue.getFirstMappedFile();
		assertThat(first.getFileFromOffset()).isGreaterThan(0);

		mappedFileQueue.shutdown(1000);
		mappedFileQueue.destroy();
	}

	@Test
	public void testDeleteExpiredFileByTime() throws Exception {
		MappedFileQueue mappedFileQueue = new MappedFileQueue("target/unit_test_store/f/", 1024, null);

		for (int i = 0; i < 100; i++) {
			MappedFile mappedFile = mappedFileQueue.getLastMappedFile(0);
			assertThat(mappedFile).isNotNull();
			byte[] bytes = new byte[512];
			assertThat(mappedFile.append(bytes)).isTrue();
		}

		assertThat(mappedFileQueue.getMappedFiles().size()).isEqualTo(50);
		long expiredTime = 100 * 1000;
		for (int i = 0; i < mappedFileQueue.getMappedFiles().size(); i++) {
			MappedFile mappedFile = mappedFileQueue.getMappedFiles().get(i);
			if (i < 5) {
				mappedFile.getFile().setLastModified(System.currentTimeMillis() - expiredTime * 2);
			}
			if (i > 20) {
				mappedFile.getFile().setLastModified(System.currentTimeMillis() - expiredTime * 2);
			}
		}
		mappedFileQueue.deleteExpiredFileByTime(expiredTime, 0, 0, false);
		assertThat(mappedFileQueue.getMappedFiles().size()).isEqualTo(45);
	}

	@Test
	public void testFindMappedFile_ByIteration() {
		MappedFileQueue mappedFileQueue = new MappedFileQueue("target/unit_test_store/g", 1024, null);
		for (int i = 0; i < 3; i++) {
			MappedFile mappedFile = mappedFileQueue.getLastMappedFile(1024 * i);
			mappedFile.wrotePosition.set(1024);
		}

		assertThat(mappedFileQueue.findMappedFileByOffset(1028).getFileFromOffset()).isEqualTo(1024);

		// Switch two MappedFiles and verify findMappedFileByOffset method
		MappedFile tmpFile = mappedFileQueue.getMappedFiles().get(1);
		mappedFileQueue.getMappedFiles().set(1, mappedFileQueue.getMappedFiles().get(2));
		mappedFileQueue.getMappedFiles().set(2, tmpFile);
		assertThat(mappedFileQueue.findMappedFileByOffset(1028).getFileFromOffset()).isEqualTo(1024);
	}



}
