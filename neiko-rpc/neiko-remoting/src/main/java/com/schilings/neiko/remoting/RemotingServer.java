package com.schilings.neiko.remoting;

import com.schilings.neiko.remoting.common.Pair;
import com.schilings.neiko.remoting.exception.RemotingConnectException;
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
 * <p>
 * 远程服务---服务端
 * </p>
 *
 * @author Schilings
 */
public interface RemotingServer extends RemotingService {

	/**
	 * 添加处理特定命令类型的处理器
	 * @param requestCode
	 * @param processor
	 * @param executor
	 */
	void registerProcessor(final int requestCode, final NettyRequestProcessor processor,
			final ExecutorService executor);

	/**
	 * 添加默认处理器
	 * @param processor
	 * @param executor
	 */
	void registerDefaultProcessor(final NettyRequestProcessor processor, final ExecutorService executor);

	/**
	 * 服务端端口
	 * @return
	 */
	int localListenPort();

	/**
	 * 对应命令类型的处理器
	 * @param requestCode
	 * @return
	 */
	Pair<NettyRequestProcessor, ExecutorService> getProcessorPair(final int requestCode);

	/**
	 * 同步发送
	 * @param channel 通道
	 * @param request 请求
	 * @param timeoutMillis 超时
	 * @return
	 * @throws InterruptedException
	 * @throws RemotingSendRequestException
	 * @throws RemotingTimeoutException
	 */
	RemotingCommand invokeSync(final Channel channel, final RemotingCommand request, final long timeoutMillis)
			throws InterruptedException, RemotingSendRequestException, RemotingTimeoutException;

	/**
	 * 异步发送
	 * @param channel 通道
	 * @param request 请求
	 * @param timeoutMillis 超时
	 * @param invokeCallback 回调
	 * @throws InterruptedException
	 * @throws RemotingTooMuchRequestException
	 * @throws RemotingTimeoutException
	 * @throws RemotingSendRequestException
	 */
	void invokeAsync(final Channel channel, final RemotingCommand request, final long timeoutMillis,
			final InvokeCallback invokeCallback) throws InterruptedException, RemotingTooMuchRequestException,
			RemotingTimeoutException, RemotingSendRequestException;

	/**
	 * 单向发送
	 * @param channel 通道
	 * @param request 请求
	 * @param timeoutMillis 超时
	 * @throws InterruptedException
	 * @throws RemotingTooMuchRequestException
	 * @throws RemotingTimeoutException
	 * @throws RemotingSendRequestException
	 */
	void invokeOneway(final Channel channel, final RemotingCommand request, final long timeoutMillis)
			throws InterruptedException, RemotingTooMuchRequestException, RemotingTimeoutException,
			RemotingSendRequestException;

}
