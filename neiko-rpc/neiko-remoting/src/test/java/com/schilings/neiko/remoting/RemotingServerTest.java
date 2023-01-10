package com.schilings.neiko.remoting;

import com.schilings.neiko.remoting.RemotingClient;
import com.schilings.neiko.remoting.RemotingServer;
import com.schilings.neiko.remoting.annotation.CFNullable;
import com.schilings.neiko.remoting.exception.*;
import com.schilings.neiko.remoting.netty.*;
import com.schilings.neiko.remoting.netty.config.NettyClientConfig;
import com.schilings.neiko.remoting.netty.config.NettyServerConfig;
import com.schilings.neiko.remoting.protocol.CommandHeaderCustomizer;
import com.schilings.neiko.remoting.protocol.LanguageCode;
import com.schilings.neiko.remoting.protocol.RemotingCommand;
import com.schilings.neiko.remoting.protocol.RemotingCommandHelper;
import io.netty.channel.ChannelHandlerContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import static com.schilings.neiko.remoting.protocol.RemotingCommandType.REQUEST_COMMAND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class RemotingServerTest {

	private static RemotingServer remotingServer;

	private static RemotingClient remotingClient;

	public static RemotingServer createRemotingServer() throws InterruptedException {
		NettyServerConfig config = new NettyServerConfig();
		RemotingServer remotingServer = new NettyRemotingServer(config);
		// 注册一个处理请求命令的处理器
		remotingServer.registerProcessor(REQUEST_COMMAND.ordinal(), new AsyncNettyRequestProcessor() {
			@Override
			public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) {
				System.out.println(request);
				request.setRemark("Hi " + ctx.channel().remoteAddress());
				return request;
			}

			@Override
			public boolean rejectRequest() {
				return false;
			}
		}, Executors.newCachedThreadPool());

		remotingServer.start();

		return remotingServer;
	}

	public static RemotingClient createRemotingClient() {
		NettyClientConfig config = new NettyClientConfig();
		config.setUseTLS(false);
		RemotingClient client = new NettyRemotingClient(config);
		client.start();
		return client;
	}

	@BeforeClass
	public static void setup() throws InterruptedException {
		remotingServer = createRemotingServer();
		remotingClient = createRemotingClient();
	}

	@AfterClass
	public static void destroy() {
		remotingClient.shutdown();
		remotingServer.shutdown();
	}

	@Test
	public void testInvokeSync() throws InterruptedException, RemotingConnectException, RemotingSendRequestException,
			RemotingTimeoutException {
		RequestHeader requestHeader = new RequestHeader();
		requestHeader.setCount(1);
		requestHeader.setMessageTitle("Welcome");
		RemotingCommand request = RemotingCommandHelper.createRequestCommand(REQUEST_COMMAND.ordinal(), requestHeader);
		RemotingCommand response = remotingClient.invokeSync("localhost:8888", request, 1000 * 60);
		assertTrue(response != null);
		assertThat(response.getLanguage()).isEqualTo(LanguageCode.JAVA);
		assertThat(response.getExtFields()).hasSize(2);

	}

	@Test
	public void testInvokeAsync() throws InterruptedException, RemotingConnectException, RemotingTimeoutException,
			RemotingTooMuchRequestException, RemotingSendRequestException {

		final CountDownLatch latch = new CountDownLatch(1);
		RemotingCommand request = RemotingCommandHelper.createRequestCommand(REQUEST_COMMAND.ordinal(), null);
		request.setRemark("messi");
		remotingClient.invokeAsync("localhost:8888", request, 1000 * 3, new InvokeCallback() {
			@Override
			public void operationComplete(ResponseFuture responseFuture) {
				latch.countDown();
				assertTrue(responseFuture != null);
				assertThat(responseFuture.getResponseCommand().getLanguage()).isEqualTo(LanguageCode.JAVA);
				assertThat(responseFuture.getResponseCommand().getExtFields()).hasSize(2);
			}
		});
		// 阻塞等待回调释放
		latch.await();
	}

	@Test
	public void testInvokeOneway() throws InterruptedException, RemotingConnectException, RemotingTimeoutException,
			RemotingTooMuchRequestException, RemotingSendRequestException {

		RemotingCommand request = RemotingCommandHelper.createRequestCommand(0, null);
		request.setRemark("messi");
		remotingClient.invokeOneway("localhost:8888", request, 1000 * 3);
	}

}

class RequestHeader implements CommandHeaderCustomizer {

	@CFNullable
	private Integer count;

	@CFNullable
	private String messageTitle;

	@Override
	public void checkFields() throws RemotingCommandException {
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getMessageTitle() {
		return messageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}

}
