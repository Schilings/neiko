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

import com.schilings.neiko.svrutil.UtilAll;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


public class MultiPathMappedFileQueueTest {

    @Test
    public void testGetLastMappedFile() {
        final byte[] fixedMsg = new byte[1024];

        String stroePaths = "target\\unit_test_store\\a,target\\unit_test_store\\b,target\\unit_test_store\\c";
        MappedFileQueue mappedFileQueue = new MultiPathMappedFileQueue(stroePaths, 1024, "", null, null);
        String[] storePaths = stroePaths.trim().split(",");
        for (int i = 0; i < 1024; i++) {
            MappedFile mappedFile = mappedFileQueue.getLastMappedFile(fixedMsg.length * i);
            assertThat(mappedFile).isNotNull();
            assertThat(mappedFile.append(fixedMsg)).isTrue();
            int idx = i % storePaths.length;
            assertThat(mappedFile.getFileName().startsWith(storePaths[idx])).isTrue();
        }
        mappedFileQueue.shutdown(1000);
        mappedFileQueue.destroy();
    }

    @Test
    public void testLoadReadOnlyMappedFiles() {
        {
            //create old mapped files
            final byte[] fixedMsg = new byte[1024];
            String stroePaths = "target\\unit_test_store\\a,target\\unit_test_store\\b,target\\unit_test_store\\c";
            MappedFileQueue mappedFileQueue = new MultiPathMappedFileQueue(stroePaths, 1024, "", null, null);
            String[] storePaths = stroePaths.trim().split(",");
            for (int i = 0; i < 1024; i++) {
                MappedFile mappedFile = mappedFileQueue.getLastMappedFile(fixedMsg.length * i);
                assertThat(mappedFile).isNotNull();
                assertThat(mappedFile.append(fixedMsg)).isTrue();
                int idx = i % storePaths.length;
                assertThat(mappedFile.getFileName().startsWith(storePaths[idx])).isTrue();
            }
            mappedFileQueue.shutdown(1000);
        }

        // test load and readonly
        String stroePaths = "target\\unit_test_store\\b";
        String readOnlyStroePaths =  "target\\unit_test_store\\a,target\\unit_test_store\\c";
        MultiPathMappedFileQueue mappedFileQueue = new MultiPathMappedFileQueue(stroePaths, 1024, readOnlyStroePaths,null, null);
        mappedFileQueue.load();

        assertThat(mappedFileQueue.mappedFiles.size()).isEqualTo(1024);
        for (int i = 0; i < 1024; i++) {
            assertThat(mappedFileQueue.mappedFiles.get(i).getFile().getName())
                    .isEqualTo(UtilAll.offset2FileName(1024 * i));
        }
        mappedFileQueue.destroy();

    }

    @Test
    public void testUpdatePathsOnline() {
        final byte[] fixedMsg = new byte[1024];

        String stroePaths = "target\\unit_test_store\\a,target\\unit_test_store\\b,target\\unit_test_store\\c";
        MappedFileQueue mappedFileQueue = new MultiPathMappedFileQueue(stroePaths, 1024, "", null, null);
        String[] storePaths = stroePaths.trim().split(",");
        for (int i = 0; i < 1024; i++) {
            MappedFile mappedFile = mappedFileQueue.getLastMappedFile(fixedMsg.length * i);
            assertThat(mappedFile).isNotNull();
            assertThat(mappedFile.append(fixedMsg)).isTrue();
            int idx = i % storePaths.length;
            assertThat(mappedFile.getFileName().startsWith(storePaths[idx])).isTrue();

            if (i == 500) {
                stroePaths = "target\\unit_test_store\\a,target\\unit_test_store\\b";
                storePaths = stroePaths.trim().split(",");
            }
        }
        mappedFileQueue.shutdown(1000);
        mappedFileQueue.destroy();
    }

    @Test
    public void testFullStorePath() {
        final byte[] fixedMsg = new byte[1024];

        Set<String> fullStorePath = new HashSet<>();
        String stroePaths = "target\\unit_test_store\\a,target\\unit_test_store\\b,target\\unit_test_store\\c";
        MappedFileQueue mappedFileQueue = new MultiPathMappedFileQueue(stroePaths, 1024, "", null, () -> fullStorePath);
        String[] storePaths = stroePaths.trim().split(",");
        assertThat(storePaths.length).isEqualTo(3);

        MappedFile mappedFile = mappedFileQueue.getLastMappedFile(0);
        assertThat(mappedFile).isNotNull();
        assertThat(mappedFile.append(fixedMsg)).isTrue();
        assertThat(mappedFile.getFileName().startsWith(storePaths[0])).isTrue();

        mappedFile = mappedFileQueue.getLastMappedFile(fixedMsg.length);
        assertThat(mappedFile.getFileName().startsWith(storePaths[1])).isTrue();
        assertThat(mappedFile.append(fixedMsg)).isTrue();
        mappedFile = mappedFileQueue.getLastMappedFile(fixedMsg.length * 2);
        assertThat(mappedFile.append(fixedMsg)).isTrue();
        assertThat(mappedFile.getFileName().startsWith(storePaths[2])).isTrue();

        fullStorePath.add("target\\unit_test_store\\b");
        mappedFile = mappedFileQueue.getLastMappedFile(fixedMsg.length * 3);
        assertThat(mappedFile.append(fixedMsg)).isTrue();
        assertThat(mappedFile.getFileName().startsWith(storePaths[2])).isTrue();

        mappedFile = mappedFileQueue.getLastMappedFile(fixedMsg.length * 4);
        assertThat(mappedFile.append(fixedMsg)).isTrue();
        assertThat(mappedFile.getFileName().startsWith(storePaths[0])).isTrue();

        mappedFileQueue.shutdown(1000);
        mappedFileQueue.destroy();
    }
}