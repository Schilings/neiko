package com.schilings.neiko.store.ha;


import com.schilings.neiko.logging.InternalLogger;
import com.schilings.neiko.logging.InternalLoggerFactory;
import com.schilings.neiko.remoting.common.RemotingUtil;
import com.schilings.neiko.store.SelectMappedBufferResult;
import com.schilings.neiko.svrutil.ServiceThread;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class HAConnection {

    private static final InternalLogger log = InternalLoggerFactory.getLogger("HAConnection");

    private final HAService haService;

    private final SocketChannel socketChannel;

    private final String clientAddr;

    private WriteSocketService writeSocketService;
    private ReadSocketService readSocketService;

    private volatile long slaveRequestOffset = -1;
    private volatile long slaveAckOffset = -1;

    public HAConnection(final HAService haService, final SocketChannel socketChannel) throws IOException {
        this.haService = haService;
        this.socketChannel = socketChannel;
        this.clientAddr = this.socketChannel.socket().getRemoteSocketAddress().toString();
        this.socketChannel.socket().setSoLinger(false, -1);
        this.socketChannel.socket().setTcpNoDelay(true);
        
        
    }

    public void start() {

    }

    public void shutdown() {

        this.close();
    }

    public void close() {
        if (this.socketChannel != null) {
            try {
                this.socketChannel.close();
            } catch (IOException e) {
                HAConnection.log.error("", e);
            }
        }
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }


    class ReadSocketService extends ServiceThread {
        private static final int READ_MAX_BUFFER_SIZE = 1024 * 1024;
        private final Selector selector;
        private final SocketChannel socketChannel;
        private final ByteBuffer byteBufferRead = ByteBuffer.allocate(READ_MAX_BUFFER_SIZE);
        private int processPosition = 0;
        private volatile long lastReadTimestamp = System.currentTimeMillis();

        public ReadSocketService(final SocketChannel socketChannel) throws IOException {
            this.selector = RemotingUtil.openSelector();
            this.socketChannel = socketChannel;
            this.socketChannel.register(this.selector, SelectionKey.OP_READ);
            this.setDaemon(true);
        }

        @Override
        public void run() {
            HAConnection.log.info(this.getServiceName() + " service started");

            while (!this.isStopped()) {
                try {
                    this.selector.select(1000);
                    boolean ok = this.processReadEvent();
                    if (!ok) {
                        HAConnection.log.error("processReadEvent error");
                        break;
                    }

                    long interval = System.currentTimeMillis() - this.lastReadTimestamp;
                    if (interval > HAConnection.this.haService.getDefaultMessageStore().getStoreConfig().getHaHousekeepingInterval()) {
                        log.warn("ha housekeeping, found this connection[" + HAConnection.this.clientAddr + "] expired, " + interval);
                        break;
                    }
                } catch (Exception e) {
                    HAConnection.log.error(this.getServiceName() + " service has exception.", e);
                    break;
                }
            }

            this.makeStop();

            writeSocketService.makeStop();

            haService.removeConnection(HAConnection.this);

            HAConnection.this.haService.getConnectionCount().decrementAndGet();

            SelectionKey sk = this.socketChannel.keyFor(this.selector);
            if (sk != null) {
                sk.cancel();
            }

            try {
                this.selector.close();
                this.socketChannel.close();
            } catch (IOException e) {
                HAConnection.log.error("", e);
            }

            HAConnection.log.info(this.getServiceName() + " service end");
        }

        @Override
        public String getServiceName() {
            return ReadSocketService.class.getSimpleName();
        }

        private boolean processReadEvent() {
            int readSizeZeroTimes = 0;

            if (!this.byteBufferRead.hasRemaining()) {
                this.byteBufferRead.flip();
                this.processPosition = 0;
            }

            while (this.byteBufferRead.hasRemaining()) {
                try {
                    int readSize = this.socketChannel.read(this.byteBufferRead);
                    if (readSize > 0) {
                        readSizeZeroTimes = 0;
                        this.lastReadTimestamp = System.currentTimeMillis();
                        //检查读取到的字节数是否大于等于 8
                        if ((this.byteBufferRead.position() - this.processPosition) >= 8) {
                            //这里模 8 取余是为了定位 ByteBuffer 中存储的最后一个 long 类型数据的位置
                            //因为在 HA 模块中，读取到的数据是以长整型方式存储在 ByteBuffer 中的，每次接收到的数据长度均为 8 字节
                            //而在 processReadEvent 方法中，如果读取到的总长度大于等于 8，则说明 ByteBuffer 中至少有一个可完整解析的长整型数据
                            //在定位最后一个 long 类型数据的位置时，需要将当前 byteBufferRead 的 position 减去它与 8 取模的值，从而确定该数据在 ByteBuffer 中的起始位置
                            //同时，通过这种方式定位最后一个数据的位置可以避免读取到 ByteBuffer 中的不完整数据，从而保证程序的正确性
                            int pos = this.byteBufferRead.position() - (this.byteBufferRead.position() % 8);
                            long readOffset = this.byteBufferRead.getLong(pos - 8);
                            this.processPosition = pos;

                            //HAConnection 对象的 slaveAckOffset 属性主要用于记录 Slave 节点向 Master 节点反馈 ACK 的消息偏移量，
                            // 即 Slave 已经成功接收并处理了 Master 发送过来的消息的偏移量
                            //在 HA 模块中，Master 节点会不断地向 Slave 节点发送消息，如果消息发送成功，则表示该消息的偏移量已经被 Slave 节点接收，
                            // 并且 Slave 节点需要向 Master 节点反馈 ACK 的信息。为了避免重复处理已经处理的消息，
                            // Slave 节点需要记录自己接收并处理的最新偏移量，并将其作为 ACK 的反馈信息发送给 Master 节点，这个最新的偏移量值就是 slaveAckOffset。
                            //当 HA 模块接收到 Master（？？？） 节点发送过来的消息时，它会根据消息的偏移量更新 Slave 节点的 slaveAckOffset 值。
                            //在 ReadSocketService 类的 processReadEvent 方法中，读取到的最后一个长整型数据就是接收到的消息的偏移量
                            //它被转化为 long 类型的 readOffset 并赋值给 slaveAckOffset 属性，表示该消息已经被 Slave 节点成功处理
                            //同时，HA 模块会根据 slaveAckOffset 值维护 Master 与 Slave 之间的数据同步状态。
                            HAConnection.this.slaveAckOffset = readOffset;

                            //当 Slave 节点与 Master 节点建立 HA 连接之后，Slave 节点会不断地向 Master 节点发送同步请求，
                            // 请求内容包括当前的偏移量 slaveRequestOffset 和本次请求的最大长度
                            //Master 节点接收到这些请求之后，会根据 slaveRequestOffset 从磁盘或内存中读取对应偏移量的数据，并将数据返回给 Slave 节点
                            //Slave 节点在接收到 Master 节点返回的数据之后，会更新自己的状态并继续向 Master 节点发送同步请求，直到两者的数据状态保持一致。
                            if (HAConnection.this.slaveRequestOffset < 0) {
                                //只会记录一次，记录第一次的请求偏移量
                                HAConnection.this.slaveRequestOffset = readOffset;
                                log.info("slave[" + HAConnection.this.clientAddr + "] request offset " + readOffset);
                                
                                //在RocketMQ中，主节点只会保留一定时间内的数据，因此，如果从节点请求的偏移量超出了主节点的最大物理偏移量，
                                // 则表示从节点请求的数据已经被主节点删除，无法再进行数据传输。
                            } else if (HAConnection.this.slaveAckOffset > HAConnection.this.haService.getDefaultMessageStore().getMaxPhyOffset()) {
                                log.warn("slave[{}] request offset={} greater than local commitLog offset={}. ",
                                        HAConnection.this.clientAddr,
                                        HAConnection.this.slaveAckOffset,
                                        HAConnection.this.haService.getDefaultMessageStore().getMaxPhyOffset());
                                return false;
                            }

                            //通知HAService从开始传输一部分数据知道达到slaveAckOffset位置以保证从节点和主节点数据一致性。
                            HAConnection.this.haService.notifyTransferSome(HAConnection.this.slaveAckOffset);
                        }
                    } else if (readSize == 0) {
                        if (++readSizeZeroTimes >= 3) {
                            break;
                        }
                    } else {
                        log.error("read socket[" + HAConnection.this.clientAddr + "] < 0");
                        return false;
                    }
                } catch (IOException e) {
                    log.error("processReadEvent exception", e);
                    return false;
                }
            }

            return true;
        }
    }

    class WriteSocketService extends ServiceThread {
        private final Selector selector;
        private final SocketChannel socketChannel;

        /**
         * 消息头的大小，包括消息长度和消息类型等信息
         */
        private final int headerSize = 8 + 4;

        /**
         * ByteBuffer 对象，用于存储消息头及其相关信息。
         */
        private final ByteBuffer byteBufferHeader = ByteBuffer.allocate(headerSize);

        /**
         * 下一条消息在 Master 节点的偏移量。
         */
        private long nextTransferFromWhere = -1;

        /**
         * SelectMappedBufferResult 对象，用于存储从文件中读取的消息数据。
         */
        private SelectMappedBufferResult selectMappedBufferResult;

        /**
         * 上一次写操作是否已经完成的标识，初始值为 true。
         */
        private boolean lastWriteOver = true;

        /**
         * 上一次写操作完成的时间戳，初始值为当前时间戳。
         */
        private long lastWriteTimestamp = System.currentTimeMillis();

        public WriteSocketService(final SocketChannel socketChannel) throws IOException {
            this.selector = RemotingUtil.openSelector();
            this.socketChannel = socketChannel;
            this.socketChannel.register(this.selector, SelectionKey.OP_WRITE);
            this.setDaemon(true);
        }

        @Override
        public void run() {
            HAConnection.log.info(this.getServiceName() + " service started");

            while (!this.isStopped()) {
                try {
                    this.selector.select(1000);

                    // 如果 Slave 请求的偏移量还未设置，即目前为之没有任何一个节点请求同步，则暂停一段时间再继续循环
                    if (-1 == HAConnection.this.slaveRequestOffset) {
                        Thread.sleep(10);
                        continue;
                    }

                    // 如果下一条消息在 Master 节点偏移量还未设置，则根据情况进行设置
                    if (-1 == this.nextTransferFromWhere) {
                        //为下一次传输的起始位置确定一个偏移量
                        // 如果 Slave 请求偏移量为0，则从最后一个文件首位开始传输
                        if (0 == HAConnection.this.slaveRequestOffset) {
                            long masterOffset = HAConnection.this.haService.getDefaultMessageStore().getMaxPhyOffset();
                            masterOffset =
                                    masterOffset
                                            - (masterOffset % HAConnection.this.haService.getDefaultMessageStore().getStoreConfig()
                                            .getMappedFileSize());

                            //如果计算后的偏移量小于 0，则将其设置为 0。
                            if (masterOffset < 0) {
                                masterOffset = 0;
                            }
                            //将计算后的偏移量设置为 nextTransferFromWhere。
                            this.nextTransferFromWhere = masterOffset;
                        } else {
                            // 如果 Slave 已经有请求消息，则将下一条消息的偏移量设置为 Slave 请求的偏移量
                            this.nextTransferFromWhere = HAConnection.this.slaveRequestOffset;
                        }

                        log.info("master transfer data from " + this.nextTransferFromWhere + " to slave[" + HAConnection.this.clientAddr
                                + "], and slave request " + HAConnection.this.slaveRequestOffset);
                    }

                    // 如果上一次发送操作已完成，则判断是否需要进行  心跳检测
                    if (this.lastWriteOver) {

                        long interval = System.currentTimeMillis() - this.lastWriteTimestamp;

                        //则判断是否需要进行  心跳检测
                        if (interval > HAConnection.this.haService.getDefaultMessageStore().getStoreConfig()
                                .getHaSendHeartbeatInterval()) {

                            // 构建消息头
                            this.byteBufferHeader.position(0);
                            this.byteBufferHeader.limit(headerSize);
                            //传输的起始位偏移量
                            this.byteBufferHeader.putLong(this.nextTransferFromWhere);
                            //
                            this.byteBufferHeader.putInt(0);
                            this.byteBufferHeader.flip();
                            // 发送数据，并更新 lastWriteOver
                            this.lastWriteOver = this.transferData();
                            if (!this.lastWriteOver)
                                continue;
                        }
                    } else {
                        // 如果上一次发送操作尚未完成，则继续发送数据，并更新 lastWriteOver
                        this.lastWriteOver = this.transferData();
                        if (!this.lastWriteOver)
                            continue;
                    }

                    // 从文件中读取消息，并构建消息头，然后发送到 Slave 节点上
                    SelectMappedBufferResult selectResult =
                            HAConnection.this.haService.getDefaultMessageStore().getData(this.nextTransferFromWhere);
                    if (selectResult != null) {
                        int size = selectResult.getSize();
                        if (size > HAConnection.this.haService.getDefaultMessageStore().getStoreConfig().getHaTransferBatchSize()) {
                            size = HAConnection.this.haService.getDefaultMessageStore().getStoreConfig().getHaTransferBatchSize();
                        }

                        long thisOffset = this.nextTransferFromWhere;
                        // this.nextTransferFromWhere自动更新
                        this.nextTransferFromWhere += size;

                        selectResult.getByteBuffer().limit(size);
                        this.selectMappedBufferResult = selectResult;

                        // 构建消息头，然后发送数据，并更新 lastWriteOver
                        this.byteBufferHeader.position(0);
                        this.byteBufferHeader.limit(headerSize);
                        //发送的起始偏移量
                        this.byteBufferHeader.putLong(thisOffset);
                        //发送的大小
                        this.byteBufferHeader.putInt(size);
                        this.byteBufferHeader.flip();

                        this.lastWriteOver = this.transferData();
                    } else {
                        // 如果从文件中读取不到消息，则等待一段时间再继续循环
                        HAConnection.this.haService.getWaitNotifyObject().allWaitForRunning(100);
                    }
                } catch (Exception e) {

                    HAConnection.log.error(this.getServiceName() + " service has exception.", e);
                    break;
                }
            }

            HAConnection.this.haService.getWaitNotifyObject().removeFromWaitingThreadTable();

            if (this.selectMappedBufferResult != null) {
                this.selectMappedBufferResult.release();
            }

            this.makeStop();

            readSocketService.makeStop();

            haService.removeConnection(HAConnection.this);

            SelectionKey sk = this.socketChannel.keyFor(this.selector);
            if (sk != null) {
                sk.cancel();
            }

            try {
                this.selector.close();
                this.socketChannel.close();
            } catch (IOException e) {
                HAConnection.log.error("", e);
            }

            HAConnection.log.info(this.getServiceName() + " service end");
        }

        private boolean transferData() throws Exception {
            int writeSizeZeroTimes = 0;
            // Write Header
            //while循环用于将消息的header信息写入到byteBufferHeader缓冲区中，并通过socketChannel写入到对应的从节点中。
            // 如果header信息没有写完，则一直进行循环，直到写完为止
            while (this.byteBufferHeader.hasRemaining()) {
                int writeSize = this.socketChannel.write(this.byteBufferHeader);
                if (writeSize > 0) {
                    writeSizeZeroTimes = 0;
                    this.lastWriteTimestamp = System.currentTimeMillis();
                } else if (writeSize == 0) {
                    if (++writeSizeZeroTimes >= 3) {
                        break;
                    }
                } else {
                    throw new Exception("ha master write header error < 0");
                }
            }

            if (null == this.selectMappedBufferResult) {
                return !this.byteBufferHeader.hasRemaining();
            }

            writeSizeZeroTimes = 0;

            // Write Body
            //while循环将消息的body信息写入到selectMappedBufferResult.getByteBuffer()缓冲区中，同样通过socketChannel写入到对应的从节点中。
            // 如果header信息已经全部写入byteBufferHeader缓冲区，并且selectMappedBufferResult.getByteBuffer()缓冲区还有剩余空间，
            // 则一直进行循环，直到消息全部写完
            if (!this.byteBufferHeader.hasRemaining()) {
                while (this.selectMappedBufferResult.getByteBuffer().hasRemaining()) {
                    int writeSize = this.socketChannel.write(this.selectMappedBufferResult.getByteBuffer());
                    if (writeSize > 0) {
                        writeSizeZeroTimes = 0;
                        this.lastWriteTimestamp = System.currentTimeMillis();
                    } else if (writeSize == 0) {
                        if (++writeSizeZeroTimes >= 3) {
                            break;
                        }
                    } else {
                        throw new Exception("ha master write body error < 0");
                    }
                }
            }

            //如果header和body信息都全部写入完成，则将result设置为true。
            // 如果selectMappedBufferResult.getByteBuffer()缓冲区已经没有剩余空间，则释放该缓冲区并将selectMappedBufferResult设置为null。
            boolean result = !this.byteBufferHeader.hasRemaining() && !this.selectMappedBufferResult.getByteBuffer().hasRemaining();

            if (!this.selectMappedBufferResult.getByteBuffer().hasRemaining()) {
                this.selectMappedBufferResult.release();
                this.selectMappedBufferResult = null;
            }

            return result;
        }

        @Override
        public String getServiceName() {
            return WriteSocketService.class.getSimpleName();
        }

        @Override
        public void shutdown() {
            super.shutdown();
        }
    }
}
