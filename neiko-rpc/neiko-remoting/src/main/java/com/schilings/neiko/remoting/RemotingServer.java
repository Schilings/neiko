package com.schilings.neiko.remoting;

import com.schilings.neiko.remoting.common.Pair;
import com.schilings.neiko.remoting.exception.RemotingSendRequestException;
import com.schilings.neiko.remoting.exception.RemotingTimeoutException;
import com.schilings.neiko.remoting.exception.RemotingTooMuchRequestException;
import com.schilings.neiko.remoting.netty.InvokeCallback;
import com.schilings.neiko.remoting.netty.NettyRequestProcessor;
import com.schilings.neiko.remoting.protocol.RemotingCommand;
import io.netty.channel.Channel;

import java.util.concurrent.ExecutorService;

/**
 * 
 * <p>远程服务---服务端</p>
 * 
 * @author Schilings
*/
public interface RemotingServer extends RemotingService{

    void registerProcessor(final int requestCode, final NettyRequestProcessor processor,
                           final ExecutorService executor);

    void registerDefaultProcessor(final NettyRequestProcessor processor, final ExecutorService executor);

    int localListenPort();

    Pair<NettyRequestProcessor, ExecutorService> getProcessorPair(final int requestCode);

    RemotingCommand invokeSync(final Channel channel, final RemotingCommand request,
                               final long timeoutMillis) throws InterruptedException, RemotingSendRequestException,
            RemotingTimeoutException;

    void invokeAsync(final Channel channel, final RemotingCommand request, final long timeoutMillis,
                     final InvokeCallback invokeCallback) throws InterruptedException,
            RemotingTooMuchRequestException, RemotingTimeoutException, RemotingSendRequestException;

    void invokeOneway(final Channel channel, final RemotingCommand request, final long timeoutMillis)
            throws InterruptedException, RemotingTooMuchRequestException, RemotingTimeoutException,
            RemotingSendRequestException;
    
}
