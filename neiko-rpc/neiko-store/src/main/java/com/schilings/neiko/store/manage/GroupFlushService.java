package com.schilings.neiko.store.manage;


import com.schilings.neiko.logging.InternalLogger;
import com.schilings.neiko.logging.InternalLoggerFactory;

import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

/**
 * <p>GroupCommit Service 同步刷盘服务 </p>
 * 
 * <p>1.在同步刷盘服务中，有两个队列requestsWrite和requestsRead，
 * requestsWrite用于存放putRequest方法写入的刷盘请求，requestsRead用于存放doCommit方法读取的刷盘请求。</p>
 * <p>2.同步刷盘请求会首先调用putRequest方法存入requestsWrite队列中，
 * 而同步刷盘服务会最多每隔10ms就会调用swapRequests方法进行读写队列引用的交换，
 * 即requestsWrite指向原requestsRead指向的队列，requestsRead指向原requestsWrite指向的队列。
 * 并且putRequest方法和swapRequests方法会竞争同一把锁。 </p>
 * <p>3.在swapRequests方法之后的doCommit刷盘方法中，只会获取requestsRead中的刷盘请求进行刷盘，
 * 并且在刷盘的最后会将requestsRead队列重新构建一个空队列，而此过程中的刷盘请求都被提交到requestsWrite。 </p>
 * <p>4.从以上的流程中我们可以得知，调用一次doCommit刷盘方法，可以进行多个请求的批量刷盘。
 * 这里使用两个队列实现读写分离，以及重置队列的操作，可以使得putRequest方法提交刷盘请求与doCommit方法消费刷盘请求同时进行，避免了他们的锁竞争。</p>
 * <p>5.如果是之前的实现，是doCommit方法被加上了锁，将会影响刷盘性能</p>
 */
public class GroupFlushService extends FlushCommitService{


    //存放putRequest方法写入的刷盘请求
    private volatile LinkedList<GroupCommitRequest> requestsWrite = new LinkedList<GroupCommitRequest>();
    //存放doCommit方法读取的刷盘请求
    private volatile LinkedList<GroupCommitRequest> requestsRead = new LinkedList<GroupCommitRequest>();
    //同步服务锁
    private final SpinStoreLock lock = new SpinStoreLock();

    private final StoreRepository storeRepository;

    public GroupFlushService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }
    

    public synchronized void putRequest(final GroupCommitRequest request) {
        //获取锁
        lock.lock();
        try {
            //存入
            this.requestsWrite.add(request);
        } finally {
            lock.unlock();
        }
        //唤醒同步刷盘线程
        this.wakeup();
    }


    private void swapRequests() {
        //加锁
        lock.lock();
        try {
            //交换读写队列
            LinkedList<GroupCommitRequest> tmp = this.requestsWrite;
            //requestsRead是一个空队列
            this.requestsWrite = this.requestsRead;
            this.requestsRead = tmp;
            //在交换了读写队列之后，requestsRead实际上引用到了requestsWrite队列，doCommit方法将会执行刷盘操作
        } finally {
            lock.unlock();
        }
    }

    /**
     * GroupCommitService的方法
     * 执行同步刷盘操作
     */
    private void doCommit() {
        //如果requestsRead读队列不为空，表示有提交请求，那么全部刷盘
        if (!this.requestsRead.isEmpty()) {
            //遍历所有的刷盘请求
            for (GroupCommitRequest req : this.requestsRead) {
                //一个同步刷盘请求最多进行两次刷盘操作，因为文件是固定大小的，第一次刷盘时可能出现上一个文件剩余大小不足的情况
                //消息只能再一次刷到下一个文件中，因此最多会出现两次刷盘的情况
                //如果flushedWhere大于下一个刷盘点位，则表示该位置的数据已经刷刷盘成功了，不再需要刷盘
                //flushedWhere的CommitLog的整体已刷盘物理偏移量
                boolean flushOK = storeRepository.mappedFileQueue.getFlushedWhere() >= req.getNextOffset();
                //最多循环刷盘两次
                for (int i = 0; i < 2 && !flushOK; i++) {
                    /*
                     * 执行强制刷盘操作，最少刷0页，即所有消息都会刷盘
                     */
                    storeRepository.mappedFileQueue.flush(0);
                    //判断是否刷盘成功，如果上一个文件剩余大小不足，则flushedWhere会小于nextOffset，那么海选哦再刷一次
                    flushOK = storeRepository.mappedFileQueue.getFlushedWhere() >= req.getNextOffset();
                }
                //内部调用flushOKFuture.complete方法存入结果，将唤醒因为提交同步刷盘请求而被阻塞的线程
                req.wakeupCustomer(flushOK ? StoreStatus.PUT_OK : StoreStatus.FLUSH_DISK_TIMEOUT);
            }
            //获取存储时间戳
            long storeTimestamp = storeRepository.mappedFileQueue.getStoreTimestamp();
            //修改StoreCheckpoint中的physicMsgTimestamp：最新文件的刷盘时间戳，单位毫秒
            //这里用于重启数据恢复
            if (storeTimestamp > 0) {
                storeRepository.storeCheckpoint.setPhysicMsgTimestamp(storeTimestamp);
            }
            //requestsRead重新创建一个空的队列，当下一次交换队列的时候，requestsWrite又会成为一个空队列
            this.requestsRead = new LinkedList<>();
        } else {
            //某些消息的设置是同步刷盘但是不等待，因此这里直接进行刷盘即可，无需唤醒线程等操作
            storeRepository.mappedFileQueue.flush(0);
        }
    }

    public void run() {
        StoreRepository.log.info(this.getServiceName() + " service started");
        /*
         * 运行时逻辑
         * 如果服务没有停止，则在死循环中执行刷盘的操作
         */
        while (!this.isStopped()) {
            try {
                //等待执行刷盘，固定最多每10ms执行一次
                //阻塞10ms
                this.waitForRunning(10);
                //尝试执行批量刷盘
                this.doCommit();
            } catch (Exception e) {
                StoreRepository.log.warn(this.getServiceName() + " service has exception. ", e);
            }
        }

        // 正常情况下shutdown，等待请求到来，然后flush
        /*
         * 停止时逻辑
         * 在正常情况下服务关闭时，将会线程等待10ms等待请求到达，然后一次性将剩余的request进行刷盘。
         */
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            StoreRepository.log.warn(this.getServiceName() + " Exception, ", e);
        }

        synchronized (this) {
            this.swapRequests();
        }
        this.doCommit();
        StoreRepository.log.info(this.getServiceName() + " service end");
    }

    @Override
    protected void onWaitEnd() {
        //交换请求
        this.swapRequests();
    }

    @Override
    public String getServiceName() {
        return GroupFlushService.class.getSimpleName();
    }

    @Override
    public long getJointime() {
        return 1000 * 60 * 5;
    }

    public static class GroupCommitRequest {
        private final long nextOffset;
        private CompletableFuture<StoreStatus> flushOKFuture = new CompletableFuture<>();
        private final long deadLine;

        public GroupCommitRequest(long nextOffset, long timeoutMillis) {
            this.nextOffset = nextOffset;
            this.deadLine = System.nanoTime() + (timeoutMillis * 1_000_000);
        }

        public long getDeadLine() {
            return deadLine;
        }

        public long getNextOffset() {
            return nextOffset;
        }
        public void wakeupCustomer(final StoreStatus storeStatus) {
            this.flushOKFuture.complete(storeStatus);
        }

        public CompletableFuture<StoreStatus> future() {
            return flushOKFuture;
        }
    }
}
