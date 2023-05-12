package com.schilings.neiko.store.ha;


import com.schilings.neiko.logging.InternalLogger;
import com.schilings.neiko.logging.InternalLoggerFactory;
import com.schilings.neiko.remoting.common.RemotingUtil;
import com.schilings.neiko.store.manage.DefaultStoreManager;
import com.schilings.neiko.store.manage.GroupFlushService;
import com.schilings.neiko.store.manage.SpinStoreLock;
import com.schilings.neiko.store.manage.StoreStatus;
import com.schilings.neiko.svrutil.ServiceThread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class HAService {
    private static final InternalLogger log = InternalLoggerFactory.getLogger("HAService");

    private final AtomicInteger connectionCount = new AtomicInteger(0);

    private final List<HAConnection> connectionList = new LinkedList<>();

    private final WaitNotifyObject waitNotifyObject = new WaitNotifyObject();

    private final AtomicLong push2SlaveMaxOffset = new AtomicLong(0);

    private final AcceptSocketService acceptSocketService;

    private final GroupTransferService groupTransferService;

    private final HAClient haClient;

    private final DefaultStoreManager defaultStoreManager;

    public HAService(final DefaultStoreManager defaultStoreManager) throws IOException {
        this.defaultStoreManager = defaultStoreManager;
        this.acceptSocketService =
                new AcceptSocketService(defaultStoreManager.getStoreConfig().getHaListenPort());
        this.groupTransferService = new GroupTransferService();
        this.haClient = new HAClient();
    }

    public void updateMasterAddress(final String newAddr) {
        if (this.haClient != null) {
            this.haClient.updateMasterAddress(newAddr);
        }
    }

    public void putRequest(final GroupFlushService.GroupCommitRequest request) {
        this.groupTransferService.putRequest(request);
    }

    public boolean isSlaveOK(final long masterPutWhere) {
        boolean result = this.connectionCount.get() > 0;
        result =
                result
                        && ((masterPutWhere - this.push2SlaveMaxOffset.get()) < this.defaultStoreManager
                        .getStoreConfig().getHaSlaveFallbehindMax());
        return result;
    }

    public void notifyTransferSome(final long offset) {
        for (long value = this.push2SlaveMaxOffset.get(); offset > value; ) {
            boolean ok = this.push2SlaveMaxOffset.compareAndSet(value, offset);
            if (ok) {
                this.groupTransferService.notifyTransferSome();
                break;
            } else {
                value = this.push2SlaveMaxOffset.get();
            }
        }
    }
    
    public AtomicInteger getConnectionCount() {
        return connectionCount;
    }

    public void start() throws Exception {
        this.acceptSocketService.beginAccept();
        this.acceptSocketService.start();
        this.groupTransferService.start();
        this.haClient.start();
    }
    

    public void destroyConnections() {
        synchronized (this.connectionList) {
            for (HAConnection c : this.connectionList) {
                c.shutdown();
            }

            this.connectionList.clear();
        }
    }

    public void addConnection(final HAConnection conn) {
        synchronized (this.connectionList) {
            this.connectionList.add(conn);
        }
    }

    public void removeConnection(final HAConnection conn) {
        synchronized (this.connectionList) {
            this.connectionList.remove(conn);
        }
    }

    public void shutdown() {
        this.haClient.shutdown();
        this.acceptSocketService.shutdown(true);
        this.destroyConnections();
        this.groupTransferService.shutdown();
    }

    public DefaultStoreManager getDefaultMessageStore() {
        return defaultStoreManager;
    }



    public WaitNotifyObject getWaitNotifyObject() {
        return waitNotifyObject;
    }

    public AtomicLong getPush2SlaveMaxOffset() {
        return push2SlaveMaxOffset;
    }


    /**
     * 侦听从属连接以创建HAConnection
     */
    class AcceptSocketService extends ServiceThread {

        private final SocketAddress socketAddressListen;
        private ServerSocketChannel serverSocketChannel;
        private Selector selector;

        public AcceptSocketService(final int port) {
            this.socketAddressListen = new InetSocketAddress(port);
        }

        public void beginAccept() throws Exception {
            this.serverSocketChannel = ServerSocketChannel.open();
            this.selector = RemotingUtil.openSelector();
            this.serverSocketChannel.socket().setReuseAddress(true);
            this.serverSocketChannel.socket().bind(this.socketAddressListen);
            // 设置非阻塞模式
            this.serverSocketChannel.configureBlocking(false);
            // 将 ServerSocketChannel 注册到 Selector 上，并关心 OP_ACCEPT 事件
            this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        }



        /**
         * AcceptSocketService 类是一个非常重要的服务线程，
         * 它负责监听 Slave 节点的连接请求，接受并处理这些请求，
         * 然后将连接信息存储在 HAService 的连接列表中，保证了 Master 节点和 Slave 节点之间的正常通信。
         * {@inheritDoc}
         */
        @Override
        public void run() {
            log.info(this.getServiceName() + " service started");

            while (!this.isStopped()) {
                try {
                    // 每隔 1s 调用一次 select，等待就绪的 SelectionKey
                    this.selector.select(1000);
                    // 获取就绪的 SelectionKey 集合
                    Set<SelectionKey> selected = this.selector.selectedKeys();

                    if (selected != null) {
                        // 处理每一个就绪的 SelectionKey
                        for (SelectionKey k : selected) {
                            // 连接请求
                            if ((k.readyOps() & SelectionKey.OP_ACCEPT) != 0) {
                                //连接
                                SocketChannel sc = ((ServerSocketChannel) k.channel()).accept();

                                if (sc != null) {
                                    HAService.log.info("HAService receive new connection, "
                                            + sc.socket().getRemoteSocketAddress());

                                    try {
                                        // 创建 HAConnection 线程来处理 Socket 连接请求
                                        HAConnection conn = new HAConnection(HAService.this, sc);
                                        conn.start();
                                        // 将 HAConnection 实例加入 HAService 的连接列表中
                                        HAService.this.addConnection(conn);
                                    } catch (Exception e) {
                                        log.error("new HAConnection exception", e);
                                        sc.close();
                                    }
                                }
                            } else {
                                log.warn("Unexpected ops in select " + k.readyOps());
                            }
                        }

                        // 清空 SelectionKey 集合
                        selected.clear();
                    }
                } catch (Exception e) {
                    log.error(this.getServiceName() + " service has exception.", e);
                }
            }

            log.info(this.getServiceName() + " service end");
        }

        @Override
        public void shutdown(final boolean interrupt) {
            super.shutdown(interrupt);
            try {
                this.serverSocketChannel.close();
                this.selector.close();
            } catch (IOException e) {
                log.error("AcceptSocketService shutdown exception", e);
            }
        }
        @Override
        public String getServiceName() {
            return AcceptSocketService.class.getSimpleName();
        }
    }


    /**
     * 用于实现消息转移功能的服务线程
     */
    class GroupTransferService extends ServiceThread {

        // 等待/通知机制对象，用于控制线程的运行状态
        private final WaitNotifyObject notifyTransferObject = new WaitNotifyObject();

        // 自旋锁对象，用于控制消息的并发写入
        private final SpinStoreLock lock = new SpinStoreLock();

        // 存储消息组提交请求的链表，用于记录需要进行消息转移的消息
        private volatile LinkedList<GroupFlushService.GroupCommitRequest> requestsWrite = new LinkedList<>();

        // 存储消息组提交请求的链表，用于记录已提交的消息
        private volatile LinkedList<GroupFlushService.GroupCommitRequest> requestsRead = new LinkedList<>();


        public void putRequest(final GroupFlushService.GroupCommitRequest request) {
            lock.lock();
            try {
                this.requestsWrite.add(request);
            } finally {
                lock.unlock();
            }
            this.wakeup();
        }

        public void notifyTransferSome() {
            this.notifyTransferObject.wakeup();
        }

        private void swapRequests() {
            lock.lock();
            try {
                LinkedList<GroupFlushService.GroupCommitRequest> tmp = this.requestsWrite;
                this.requestsWrite = this.requestsRead;
                this.requestsRead = tmp;
            } finally {
                lock.unlock();
            }
        }

        /**
         * doWaitTransfer()方法的作用是等待并处理已提交的消息组，它遍历请求队列requestsRead，
         * 检查是否有消息已经成功转移。如果有消息已经成功转移，则通知相应的生产者线程
         */
        private void doWaitTransfer() {
            // 如果 requestsRead 中有消息组提交请求，则进行处理
            if (!this.requestsRead.isEmpty()) {
                for (GroupFlushService.GroupCommitRequest req : this.requestsRead) {
                    boolean transferOK = HAService.this.push2SlaveMaxOffset.get() >= req.getNextOffset();
                    long deadLine = req.getDeadLine();
                    while (!transferOK && deadLine - System.nanoTime() > 0) {
                        // 等待1s，然后再次检查是否已经成功转移
                        this.notifyTransferObject.waitForRunning(1000);
                        transferOK = HAService.this.push2SlaveMaxOffset.get() >= req.getNextOffset();
                    }
                    // 唤醒生产者线程，告诉它们这个消息是否已经成功同步
                    req.wakeupCustomer(transferOK ? StoreStatus.PUT_OK : StoreStatus.FLUSH_SLAVE_TIMEOUT);
                }
                // 清空 requestsRead 队列
                this.requestsRead = new LinkedList<>();
            }
        }

        public void run() {
            log.info(this.getServiceName() + " service started");

            while (!this.isStopped()) {
                try {
                    this.waitForRunning(10);
                    this.doWaitTransfer();
                } catch (Exception e) {
                    log.warn(this.getServiceName() + " service has exception. ", e);
                }
            }

            log.info(this.getServiceName() + " service end");
        }

        @Override
        protected void onWaitEnd() {
            this.swapRequests();
        }

        @Override
        public String getServiceName() {
            return GroupTransferService.class.getSimpleName();
        }
    }


    class HAClient extends ServiceThread {
        /**
         * 定义一个缓冲区的最大大小 4M
         */
        private static final int READ_MAX_BUFFER_SIZE = 1024 * 1024 * 4;

        /**
         * atomic 引用，用于保存当前连接的主节点地址。
         */
        private final AtomicReference<String> masterAddress = new AtomicReference<>();
        /**
         * 用于发送消息给Master的字节缓冲区
         */
        private final ByteBuffer reportOffset = ByteBuffer.allocate(8);
        /**
         * 与Master的连接通道
         */
        private SocketChannel socketChannel;
        private Selector selector;
        /**
         * 上一次发送的时间
         */
        private long lastWriteTimestamp = System.currentTimeMillis();

        /**
         * 位于当前slave节点的已经提交给master的偏移量
         */
        private long currentReportedOffset = 0;

        /**
         * 消费位置，表示消费队列中消息的位置。
         */
        private int dispatchPosition = 0;

        /**
         * 用于从通道中读取数据的缓冲区
         */
        private ByteBuffer byteBufferRead = ByteBuffer.allocate(READ_MAX_BUFFER_SIZE);

        /**
         * 在进行消息解码之前，保存从通道中读取的字节的缓冲区
         */
        private ByteBuffer byteBufferBackup = ByteBuffer.allocate(READ_MAX_BUFFER_SIZE);

        public HAClient() throws IOException {
            this.selector = RemotingUtil.openSelector();
        }

        /**
         * 更新master地址
         * @param newAddr
         */
        public void updateMasterAddress(final String newAddr) {
            String currentAddr = this.masterAddress.get();
            if (currentAddr == null || !currentAddr.equals(newAddr)) {
                this.masterAddress.set(newAddr);
                log.info("update master address, OLD: " + currentAddr + " NEW: " + newAddr);
            }
        }

        /**
         * 是否到了向master发送更新偏移量的时候
         * @return
         */
        private boolean isTimeToReportOffset() {
            long interval = System.currentTimeMillis() - this.lastWriteTimestamp;
            //距离上一次发送已经大于设定的时间间隔
            boolean needHeart = interval > HAService.this.defaultStoreManager.getStoreConfig().getHaSendHeartbeatInterval();
            return needHeart;
        }

        /**
         * 发送slave的最大偏移量
         * @param maxOffset
         * @return
         */
        private boolean reportSlaveMaxOffset(final long maxOffset) {
            this.reportOffset.position(0);
            this.reportOffset.limit(8);
            //
            this.reportOffset.putLong(maxOffset);
            
            this.reportOffset.position(0);
            this.reportOffset.limit(8);

            //写入SocketChannel
            for (int i = 0; i < 3 && this.reportOffset.hasRemaining(); i++) {
                try {
                    this.socketChannel.write(this.reportOffset);
                } catch (IOException e) {
                    log.error(this.getServiceName()
                            + "reportSlaveMaxOffset this.socketChannel.write exception", e);
                    return false;
                }
            }
            //更新上一次发送时间为此时
            lastWriteTimestamp = System.currentTimeMillis();
            return !this.reportOffset.hasRemaining();
        }


        /**
         * 将当前 byteBufferRead 的已经处理的部分复制到备用缓冲区中。
         */
        private void reallocateByteBuffer() {
            // 计算未处理字节数
            int remain = READ_MAX_BUFFER_SIZE - this.dispatchPosition;
            if (remain > 0) {
                // 设置 read ByteBuffer 的读取位置
                this.byteBufferRead.position(this.dispatchPosition);

                // 设置备份 ByteBuffer 的读写位置
                this.byteBufferBackup.position(0);
                this.byteBufferBackup.limit(READ_MAX_BUFFER_SIZE);

                // 将 read ByteBuffer 已经处理的数据复制到备份 ByteBuffer 中
                this.byteBufferBackup.put(this.byteBufferRead);
            }

            // 交换缓冲区，以便后续处理
            this.swapByteBuffer();

            // 设置 read ByteBuffer 的读写位置
            this.byteBufferRead.position(remain);
            this.byteBufferRead.limit(READ_MAX_BUFFER_SIZE);

            // 设置当前消费位置为 0
            this.dispatchPosition = 0;
        }

        /**
         * 用于交换 read ByteBuffer 和备份 ByteBuffer
         */
        private void swapByteBuffer() {
            // 交换 ByteBuffer 引用
            ByteBuffer tmp = this.byteBufferRead;
            this.byteBufferRead = this.byteBufferBackup;
            this.byteBufferBackup = tmp;
        }

        /**
         * 处理Read事件即来自Master的响应
         * @return
         */
        private boolean processReadEvent() {
            int readSizeZeroTimes = 0;
            //如果读取缓存区还有剩余空间
            while (this.byteBufferRead.hasRemaining()) {
                try {
                    //读取字节
                    int readSize = this.socketChannel.read(this.byteBufferRead);
                    //如果有收到字节
                    if (readSize > 0) {
                        readSizeZeroTimes = 0;
                        //分发处理
                        boolean result = this.dispatchReadRequest();
                        //处理失败
                        if (!result) {
                            log.error("HAClient, dispatchReadRequest error");
                            return false;
                        }
                        //没有收到任何字节    
                    } else if (readSize == 0) {
                        if (++readSizeZeroTimes >= 3) {
                            break;
                        }
                    } else {
                        log.info("HAClient, processReadEvent read socket < 0");
                        return false;
                    }
                } catch (IOException e) {
                    log.info("HAClient, processReadEvent read socket exception", e);
                    return false;
                }
            }

            return true;
        }

        /**
         * 读取Master传输数据，并返回是异常
         * 如果读取到数据，写入本地文件
         *  异常原因：
         *   1. Master传输来的数据offset 不等于 Slave的数据最大offset
         *   2. 上报到Master进度失败
         */
        private boolean dispatchReadRequest() {
            final int msgHeaderSize = 8 + 4; // phyoffset + size

            while (true) {
                // 读取到请求
                int diff = this.byteBufferRead.position() - this.dispatchPosition;
                if (diff >= msgHeaderSize) {
                    // 读取masterPhyOffset、bodySize。使用dispatchPostion的原因是：处理数据“粘包”导致数据读取不完整
                    long masterPhyOffset = this.byteBufferRead.getLong(this.dispatchPosition);
                    int bodySize = this.byteBufferRead.getInt(this.dispatchPosition + 8);
                    // 校验 Master传输来的数据offset 是否和 Slave的CommitLog数据最大offset 是否相同。
                    long slavePhyOffset = HAService.this.defaultStoreManager.getMaxPhyOffset();

                    if (slavePhyOffset != 0) {
                        if (slavePhyOffset != masterPhyOffset) {
                            log.error("master pushed offset not equal the max phy offset in slave, SLAVE: "
                                    + slavePhyOffset + " MASTER: " + masterPhyOffset);
                            return false;
                        }
                    }
                    // 读取到消息
                    if (diff >= (msgHeaderSize + bodySize)) {
                        // 写入CommitLog
                        byte[] bodyData = byteBufferRead.array();
                        int dataStart = this.dispatchPosition + msgHeaderSize;
                        HAService.this.defaultStoreManager.appendToRepository(
                                masterPhyOffset, bodyData, dataStart, bodySize);
                        // 设置处理到的位置
                        this.dispatchPosition += msgHeaderSize + bodySize;
                        // 上报到Master进度
                        if (!reportSlaveMaxOffsetPlus()) {
                            return false;
                        }
                        // 继续循环
                        continue;
                    }
                }
                // 空间写满，重新分配空间
                if (!this.byteBufferRead.hasRemaining()) {
                    this.reallocateByteBuffer();
                }

                break;
            }

            return true;
        }

        private boolean reportSlaveMaxOffsetPlus() {
            boolean result = true;
            long currentPhyOffset = HAService.this.defaultStoreManager.getMaxPhyOffset();
            if (currentPhyOffset > this.currentReportedOffset) {
                this.currentReportedOffset = currentPhyOffset;
                result = this.reportSlaveMaxOffset(this.currentReportedOffset);
                if (!result) {
                    this.closeMaster();
                    log.error("HAClient, reportSlaveMaxOffset error, " + this.currentReportedOffset);
                }
            }

            return result;
        }

        /**
         * 连接master
         * @return
         * @throws ClosedChannelException
         */
        private boolean connectMaster() throws ClosedChannelException {
            //如果为null，说明还没有建立连接，就通过master的地址建立连接
            if (null == socketChannel) {
                String addr = this.masterAddress.get();
                if (addr != null) {

                    SocketAddress socketAddress = RemotingUtil.string2SocketAddress(addr);
                    if (socketAddress != null) {
                        this.socketChannel = RemotingUtil.connect(socketAddress);
                        if (this.socketChannel != null) {
                            //绑定Read事件
                            this.socketChannel.register(this.selector, SelectionKey.OP_READ);
                        }
                    }
                }
                //设置目前提交偏移量为最大偏移量
                this.currentReportedOffset = HAService.this.defaultStoreManager.getMaxPhyOffset();
                //设置上一次提交事件为建立连接的时间
                this.lastWriteTimestamp = System.currentTimeMillis();
            }


            return this.socketChannel != null;
        }

        /**
         * 关闭与Master的连接
         */
        private void closeMaster() {
            if (null != this.socketChannel) {
                try {

                    SelectionKey sk = this.socketChannel.keyFor(this.selector);
                    if (sk != null) {
                        sk.cancel();
                    }

                    this.socketChannel.close();

                    this.socketChannel = null;
                } catch (IOException e) {
                    log.warn("closeMaster exception. ", e);
                }

                this.lastWriteTimestamp = 0;
                this.dispatchPosition = 0;

                this.byteBufferBackup.position(0);
                this.byteBufferBackup.limit(READ_MAX_BUFFER_SIZE);

                this.byteBufferRead.position(0);
                this.byteBufferRead.limit(READ_MAX_BUFFER_SIZE);
            }
        }

        @Override
        public void run() {
            log.info(this.getServiceName() + " service started");

            while (!this.isStopped()) {
                try {
                    //尝试连接Master
                    if (this.connectMaster()) {
                        //到了提交master的时间 
                        if (this.isTimeToReportOffset()) {
                            //提交当前记录的偏移量
                            boolean result = this.reportSlaveMaxOffset(this.currentReportedOffset);
                            //如果提交出错返回false
                            if (!result) {
                                //关闭与master的连接
                                this.closeMaster();
                            }
                        }
                        //等到Read事件,即等待Master的响应
                        this.selector.select(1000);
                        //处理响应
                        boolean ok = this.processReadEvent();
                        //如果提交出错返回false
                        if (!ok) {
                            //关闭与master的连接
                            this.closeMaster();
                        }

                        // 若进度有变化，上报到Master进度
                        if (!reportSlaveMaxOffsetPlus()) {
                            continue;
                        }

                        // 如果主节点过久未响应，关闭连接，Master过久未返回数据，关闭连接
                        long interval = System.currentTimeMillis() - this.lastWriteTimestamp;
                        if (interval > HAService.this.getDefaultMessageStore().getStoreConfig()
                                .getHaHousekeepingInterval()) {
                            log.warn("HAClient, housekeeping, found this connection[" + this.masterAddress
                                    + "] expired, " + interval);
                            this.closeMaster();
                            log.warn("HAClient, master not response some time, so close connection");
                        }
                    } else {
                        // 连接主节点失败，等待重连
                        this.waitForRunning(1000 * 5);
                    }
                } catch (Exception e) {
                    log.warn(this.getServiceName() + " service has exception. ", e);
                    this.waitForRunning(1000 * 5);
                }
            }

            log.info(this.getServiceName() + " service end");
        }

        @Override
        public void shutdown() {
            super.shutdown();
            closeMaster();
        }

        @Override
        public String getServiceName() {
            return HAClient.class.getSimpleName();
        }
    }
}
