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

/**
 * $Id: MappedFileTest.java 1831 2013-05-16 01:39:51Z vintagewang@apache.org $
 */
package com.schilings.neiko.store;

import com.schilings.neiko.svrutil.UtilAll;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class MappedFileTest {

	private static final File file = new File("");
	private final String storeMessage = "Once, there was a chance for me!";

	private final String fileName = file.getAbsolutePath() + "\\target\\unit_test_store\\MappedFileTest\\000";

	@Test
	public void testSelectMappedBuffer() throws IOException {
		MappedFile mappedFile = new MappedFile(fileName, 1024 * 64);
		boolean result = mappedFile.append(storeMessage.getBytes());
		boolean result1 = mappedFile.append(storeMessage.getBytes());
		assertThat(result).isTrue();

		SelectMappedBufferResult selectMappedBufferResult = mappedFile.selectMappedBuffer(0);
		byte[] data = new byte[storeMessage.length()];
		selectMappedBufferResult.getByteBuffer().get(data);
		String readString = new String(data);
		assertThat(readString).isEqualTo(storeMessage);

		mappedFile.shutdown(1000);
		assertThat(mappedFile.isAvailable()).isFalse();
		selectMappedBufferResult.release();
		assertThat(mappedFile.isCleanupOver()).isTrue();
		assertThat(mappedFile.destroy(1000)).isTrue();
	}

	@After
	public void destory() {
		File file = new File(fileName);
		UtilAll.deleteFile(file);
	}

}
