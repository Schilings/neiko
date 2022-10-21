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


    /**
     * 同步发送
     * @param addr 服务端地址
     * @param request 请求内容
     * @param timeoutMillis 超时
     * @return
     * @throws InterruptedException
     * @throws RemotingConnectException
     * @throws RemotingSendRequestException
     * @throws RemotingTimeoutException
     */
    RemotingCommand invokeSync(final String addr, final RemotingCommand request,
                               final long timeoutMillis) throws InterruptedException, RemotingConnectException,
            RemotingSendRequestException, RemotingTimeoutException;

    /**
     * 异步发送
     * @param addr 服务端地址
     * @param request 请求内容
     * @param timeoutMillis 超时
     * @param invokeCallback 回调操作
     * @throws InterruptedException
     * @throws RemotingConnectException
     * @throws RemotingTooMuchRequestException
     * @throws RemotingTimeoutException
     * @throws RemotingSendRequestException
     */
    void invokeAsync(final String addr, final RemotingCommand request, final long timeoutMillis,
                     final InvokeCallback invokeCallback) throws InterruptedException, RemotingConnectException,
            RemotingTooMuchRequestException, RemotingTimeoutException, RemotingSendRequestException;

    /**
     * 单向发送
     * @param addr 服务端地址
     * @param request 请求内容
     * @param timeoutMillis 超时
     * @throws InterruptedException
     * @throws RemotingConnectException
     * @throws RemotingTooMuchRequestException
     * @throws RemotingTimeoutException
     * @throws RemotingSendRequestException
     */
    void invokeOneway(final String addr, final RemotingCommand request, final long timeoutMillis)
            throws InterruptedException, RemotingConnectException, RemotingTooMuchRequestException,
            RemotingTimeoutException, RemotingSendRequestException;

    /**
     * 注册业务处理器
     * @param requestCode 处理的命令类型表示
     * @param processor 处理器
     * @param executor 线程执行器
     */
    void registerProcessor(final int requestCode, final NettyRequestProcessor processor,
                           final ExecutorService executor);

    /**
     * 设置回调操作执行器
     * @param callbackExecutor
     */
    void setCallbackExecutor(final ExecutorService callbackExecutor);

    /**
     * 回调操作执行器
     * @return
     */
    ExecutorService getCallbackExecutor();

    /**
     * 返回该通道是否可写
     * @param addr
     * @return
     */
    boolean isChannelWritable(final String addr);
    
}
