package com.schilings.neiko.remoting.netty;

import io.netty.channel.Channel;

/**
 * 
 * <p>监听Netty事件，从而实现实际业务解耦</p>
 * 
 * @author Schilings
*/
public interface ChannelEventListener {

    void onChannelConnect(final String remoteAddr, final Channel channel);

    void onChannelClose(final String remoteAddr, final Channel channel);

    void onChannelException(final String remoteAddr, final Channel channel);

    void onChannelIdle(final String remoteAddr, final Channel channel);
}
