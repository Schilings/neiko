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
package com.schilings.neiko.remoting.netty.handler;

import com.schilings.neiko.logging.InternalLogger;
import com.schilings.neiko.logging.InternalLoggerFactory;
import com.schilings.neiko.remoting.common.RemotingHelper;
import com.schilings.neiko.remoting.common.RemotingUtil;
import com.schilings.neiko.remoting.protocol.RemotingCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.ByteBuffer;

@ChannelHandler.Sharable
public class NettyEncoder extends MessageToByteEncoder<RemotingCommand> {

	private static final InternalLogger log = InternalLoggerFactory.getLogger(RemotingHelper.NEIKO_REMOTING);

	@Override
	public void encode(ChannelHandlerContext ctx, RemotingCommand remotingCommand, ByteBuf out) throws Exception {
		try {
			ByteBuffer header = remotingCommand.encodeHeader();
			out.writeBytes(header);
			byte[] body = remotingCommand.getBody();
			if (body != null) {
				out.writeBytes(body);
			}
		}
		catch (Exception e) {
			log.error("encode exception, " + RemotingHelper.parseChannelRemoteAddr(ctx.channel()), e);
			if (remotingCommand != null) {
				log.error(remotingCommand.toString());
			}
			RemotingUtil.closeChannel(ctx.channel());
		}
	}

}
