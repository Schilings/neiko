package com.schilings.neiko.remoting.protocol;


import com.schilings.neiko.remoting.RemotingClient;
import com.schilings.neiko.remoting.RemotingServer;
import com.schilings.neiko.remoting.annotation.CFNullable;
import com.schilings.neiko.remoting.exception.RemotingCommandException;
import com.schilings.neiko.remoting.netty.AsyncNettyRequestProcessor;
import com.schilings.neiko.remoting.netty.NettyRemotingServer;
import com.schilings.neiko.remoting.netty.config.NettyServerConfig;
import io.netty.channel.ChannelHandlerContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.concurrent.Executors;

public class RemotingServerTest {

    private static RemotingServer remotingServer;
    private static RemotingClient remotingClient;

    public static RemotingServer createRemotingServer() throws InterruptedException {
        NettyServerConfig config = new NettyServerConfig();
        RemotingServer remotingServer = new NettyRemotingServer(config);
        remotingServer.registerProcessor(0, new AsyncNettyRequestProcessor() {
            @Override
            public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) {
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

    @BeforeClass
    public static void setup() throws InterruptedException {
        remotingServer = createRemotingServer();
    }

    @AfterClass
    public static void destroy() {
        remotingServer.shutdown();
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

