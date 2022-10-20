package com.schilings.neiko.remoting.netty;

import com.schilings.neiko.remoting.protocol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * <p>专注于实际业务处理</p>
 * 
 * @author Schilings
*/
public interface NettyRequestProcessor {

    RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request)
            throws Exception;

    boolean rejectRequest();
}
