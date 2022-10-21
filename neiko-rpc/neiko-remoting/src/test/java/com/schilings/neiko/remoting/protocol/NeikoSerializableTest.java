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
package com.schilings.neiko.remoting.protocol;


import com.schilings.neiko.remoting.exception.RemotingCommandException;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class NeikoSerializableTest {
    @Test
    public void testRocketMQProtocolEncodeAndDecode_WithoutRemarkWithoutExtFields() {
        System.setProperty(RemotingCommand.REMOTING_VERSION_KEY, "2333");

        //org.apache.rocketmq.common.protocol.RequestCode.REGISTER_BROKER
        int code = 103;
        RemotingCommand cmd = RemotingCommandHelper.createRequestCommand(code, new SampleCommandCustomHeader());
        cmd.setSerializeTypeCurrentRPC(SerializeType.NEIKO);

        byte[] result = NeikoDefaultSerializable.neikoProtocolEncode(cmd);
        int opaque = cmd.getOpaque();

        assertThat(result).hasSize(21);
        assertThat(parseToShort(result, 0)).isEqualTo((short) code); //code
        assertThat(result[2]).isEqualTo(LanguageCode.JAVA.getCode()); //language
        assertThat(parseToShort(result, 3)).isEqualTo((short) 2333); //version
        assertThat(parseToInt(result, 9)).isEqualTo(0); //flag
        assertThat(parseToInt(result, 13)).isEqualTo(0); //empty remark
        assertThat(parseToInt(result, 17)).isEqualTo(0); //empty extFields

        RemotingCommand decodedCommand = null;
        try {
            decodedCommand = NeikoDefaultSerializable.neikoProtocolDecode(result);

            assertThat(decodedCommand.getCode()).isEqualTo(code);
            assertThat(decodedCommand.getLanguage()).isEqualTo(LanguageCode.JAVA);
            assertThat(decodedCommand.getVersion()).isEqualTo(2333);
            assertThat(decodedCommand.getOpaque()).isEqualTo(opaque);
            assertThat(decodedCommand.getFlag()).isEqualTo(0);
            assertThat(decodedCommand.getRemark()).isNull();
            assertThat(decodedCommand.getExtFields()).isNull();
        } catch (RemotingCommandException e) {
            e.printStackTrace();

            Assert.fail("Should not throw IOException");
        }
    }

    @Test
    public void testRocketMQProtocolEncodeAndDecode_WithRemarkWithoutExtFields() {
        System.setProperty(RemotingCommand.REMOTING_VERSION_KEY, "2333");

        //org.apache.rocketmq.common.protocol.RequestCode.REGISTER_BROKER
        int code = 103;
        RemotingCommand cmd = RemotingCommandHelper.createRequestCommand(code,
            new SampleCommandCustomHeader());
        cmd.setSerializeTypeCurrentRPC(SerializeType.NEIKO);
        cmd.setRemark("Sample Remark");

        byte[] result = NeikoDefaultSerializable.neikoProtocolEncode(cmd);
        int opaque = cmd.getOpaque();

        assertThat(result).hasSize(34);
        assertThat(parseToShort(result, 0)).isEqualTo((short) code); //code
        assertThat(result[2]).isEqualTo(LanguageCode.JAVA.getCode()); //language
        assertThat(parseToShort(result, 3)).isEqualTo((short) 2333); //version
        assertThat(parseToInt(result, 9)).isEqualTo(0); //flag
        assertThat(parseToInt(result, 13)).isEqualTo(13); //remark length

        byte[] remarkArray = new byte[13];
        System.arraycopy(result, 17, remarkArray, 0, 13);
        assertThat(new String(remarkArray)).isEqualTo("Sample Remark");

        assertThat(parseToInt(result, 30)).isEqualTo(0); //empty extFields

        try {
            RemotingCommand decodedCommand = NeikoDefaultSerializable.neikoProtocolDecode(result);

            assertThat(decodedCommand.getCode()).isEqualTo(code);
            assertThat(decodedCommand.getLanguage()).isEqualTo(LanguageCode.JAVA);
            assertThat(decodedCommand.getVersion()).isEqualTo(2333);
            assertThat(decodedCommand.getOpaque()).isEqualTo(opaque);
            assertThat(decodedCommand.getFlag()).isEqualTo(0);
            assertThat(decodedCommand.getRemark()).contains("Sample Remark");
            assertThat(decodedCommand.getExtFields()).isNull();
        } catch (RemotingCommandException e) {
            e.printStackTrace();

            Assert.fail("Should not throw IOException");
        }
    }

    @Test
    public void testRocketMQProtocolEncodeAndDecode_WithoutRemarkWithExtFields() {
        System.setProperty(RemotingCommand.REMOTING_VERSION_KEY, "2333");

        //org.apache.rocketmq.common.protocol.RequestCode.REGISTER_BROKER
        int code = 103;
        RemotingCommand cmd = RemotingCommandHelper.createRequestCommand(code,
            new SampleCommandCustomHeader());
        cmd.setSerializeTypeCurrentRPC(SerializeType.NEIKO);
        cmd.addExtField("key", "value");

        byte[] result = NeikoDefaultSerializable.neikoProtocolEncode(cmd);
        int opaque = cmd.getOpaque();

        assertThat(result).hasSize(35);
        assertThat(parseToShort(result, 0)).isEqualTo((short) code); //code
        assertThat(result[2]).isEqualTo(LanguageCode.JAVA.getCode()); //language
        assertThat(parseToShort(result, 3)).isEqualTo((short) 2333); //version
        assertThat(parseToInt(result, 9)).isEqualTo(0); //flag
        assertThat(parseToInt(result, 13)).isEqualTo(0); //empty remark
        assertThat(parseToInt(result, 17)).isEqualTo(14); //extFields length

        byte[] extFieldsArray = new byte[14];
        System.arraycopy(result, 21, extFieldsArray, 0, 14);
        HashMap<String, String> extFields = NeikoDefaultSerializable.mapDeserialize(extFieldsArray);
        assertThat(extFields).contains(new HashMap.SimpleEntry("key", "value"));

        try {
            RemotingCommand decodedCommand = NeikoDefaultSerializable.neikoProtocolDecode(result);
            assertThat(decodedCommand.getCode()).isEqualTo(code);
            assertThat(decodedCommand.getLanguage()).isEqualTo(LanguageCode.JAVA);
            assertThat(decodedCommand.getVersion()).isEqualTo(2333);
            assertThat(decodedCommand.getOpaque()).isEqualTo(opaque);
            assertThat(decodedCommand.getFlag()).isEqualTo(0);
            assertThat(decodedCommand.getRemark()).isNull();
            assertThat(decodedCommand.getExtFields()).contains(new HashMap.SimpleEntry("key", "value"));
        } catch (RemotingCommandException e) {
            e.printStackTrace();

            Assert.fail("Should not throw IOException");
        }
    }

    @Test
    public void testIsBlank_NotBlank() {
        assertThat(NeikoDefaultSerializable.isBlank("bar")).isFalse();
        assertThat(NeikoDefaultSerializable.isBlank("  A  ")).isFalse();
    }

    @Test
    public void testIsBlank_Blank() {
        assertThat(NeikoDefaultSerializable.isBlank(null)).isTrue();
        assertThat(NeikoDefaultSerializable.isBlank("")).isTrue();
        assertThat(NeikoDefaultSerializable.isBlank("  ")).isTrue();
    }

    private short parseToShort(byte[] array, int index) {
        return (short) (array[index] * 256 + array[++index]);
    }

    private int parseToInt(byte[] array, int index) {
        return array[index] * 16777216 + array[++index] * 65536 + array[++index] * 256
            + array[++index];
    }
}