package com.schilings.neiko.remoting;

import com.schilings.neiko.remoting.exception.RemotingConnectException;
import com.schilings.neiko.remoting.exception.RemotingSendRequestException;
import com.schilings.neiko.remoting.exception.RemotingTimeoutException;
import com.schilings.neiko.remoting.exception.RemotingTooMuchRequestException;
import com.schilings.neiko.remoting.netty.InvokeCallback;
import com.schilings.neiko.remoting.netty.NettyRequestProcessor;
import com.schilings.neiko.remoting.protocol.RemotingCommand;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 
 * <p>远程服务---客户端</p>
 * 
 * @author Schilings
*/
public interface RemotingClient extends RemotingService{


    void updateNameServerAddressList(final List<String> addrs);

    List<String> getNameServerAddressList();

    RemotingCommand invokeSync(final String addr, final RemotingCommand request,
                               final long timeoutMillis) throws InterruptedException, RemotingConnectException,
            RemotingSendRequestException, RemotingTimeoutException;

    void invokeAsync(final String addr, final RemotingCommand request, final long timeoutMillis,
                     final InvokeCallback invokeCallback) throws InterruptedException, RemotingConnectException,
            RemotingTooMuchRequestException, RemotingTimeoutException, RemotingSendRequestException;

    void invokeOneway(final String addr, final RemotingCommand request, final long timeoutMillis)
            throws InterruptedException, RemotingConnectException, RemotingTooMuchRequestException,
            RemotingTimeoutException, RemotingSendRequestException;

    void registerProcessor(final int requestCode, final NettyRequestProcessor processor,
                           final ExecutorService executor);

    void setCallbackExecutor(final ExecutorService callbackExecutor);

    ExecutorService getCallbackExecutor();

    boolean isChannelWritable(final String addr);
    
}
