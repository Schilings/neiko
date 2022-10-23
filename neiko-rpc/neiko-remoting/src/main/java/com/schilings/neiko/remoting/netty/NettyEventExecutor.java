package com.schilings.neiko.remoting.netty;


import com.schilings.neiko.logging.InternalLogger;
import com.schilings.neiko.logging.InternalLoggerFactory;
import com.schilings.neiko.remoting.common.RemotingHelper;
import com.schilings.neiko.svrutil.ServiceThread;
import com.schilings.neiko.remoting.netty.event.NettyEvent;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * <p>监听NettyEvent的线程，实现业务解耦</p>
 *
 * @author Schilings
 */
public class NettyEventExecutor extends ServiceThread {

    private static final InternalLogger log = InternalLoggerFactory.getLogger(RemotingHelper.NEIKO_REMOTING);
    private final LinkedBlockingQueue<NettyEvent> eventQueue = new LinkedBlockingQueue<NettyEvent>();
    private final int maxSize = 10000;

    private final NettyRemotingAbstract nettyRemoting;

    public NettyEventExecutor(NettyRemotingAbstract nettyRemoting) {
        this.nettyRemoting = nettyRemoting;
    }

    public void putNettyEvent(final NettyEvent event) {
        int currentSize = this.eventQueue.size();
        if (currentSize <= maxSize) {
            this.eventQueue.add(event);
        } else {
            log.warn("event queue size [{}] over the limit [{}], so drop this event {}", currentSize, maxSize, event.toString());
        }
    }


    @Override
    public String getServiceName() {
        return NettyEventExecutor.class.getSimpleName();
    }

    @Override
    public void run() {
        log.info(this.getServiceName() + " service started");

        final ChannelEventListener listener = this.nettyRemoting.getChannelEventListener();

        while (!this.isStopped()) {
            try {
                NettyEvent event = this.eventQueue.poll(3000, TimeUnit.MILLISECONDS);
                if (event != null && listener != null) {
                    switch (event.getType()) {
                        case IDLE:
                            listener.onChannelIdle(event.getRemoteAddr(), event.getChannel());
                            break;
                        case CLOSE:
                            listener.onChannelClose(event.getRemoteAddr(), event.getChannel());
                            break;
                        case CONNECT:
                            listener.onChannelConnect(event.getRemoteAddr(), event.getChannel());
                            break;
                        case EXCEPTION:
                            listener.onChannelException(event.getRemoteAddr(), event.getChannel());
                            break;
                        default:
                            break;

                    }
                }
            } catch (Exception e) {
                log.warn(this.getServiceName() + " service has exception. ", e);
            }
        }

        log.info(this.getServiceName() + " service end");
    }
}
