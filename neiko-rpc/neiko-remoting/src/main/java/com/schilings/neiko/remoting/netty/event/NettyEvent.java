package com.schilings.neiko.remoting.netty.event;

import io.netty.channel.Channel;

/**
 *
 * <p>
 * Netty事件
 * </p>
 * <p>
 * 通过Netty事件驱动业务操作
 * </p>
 *
 * @author Schilings
 */
public class NettyEvent {

	private final NettyEventType type;

	private final String remoteAddr;

	private final Channel channel;

	public NettyEvent(NettyEventType type, String remoteAddr, Channel channel) {
		this.type = type;
		this.remoteAddr = remoteAddr;
		this.channel = channel;
	}

	public NettyEventType getType() {
		return type;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public Channel getChannel() {
		return channel;
	}

	@Override
	public String toString() {
		return "NettyEvent [type=" + type + ", remoteAddr=" + remoteAddr + ", channel=" + channel + "]";
	}

}
