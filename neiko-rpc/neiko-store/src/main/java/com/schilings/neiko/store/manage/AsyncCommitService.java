package com.schilings.neiko.store.manage;


/**
 * <h1>异步堆外缓存刷盘</h1>
 * <p>可以发现，异步堆外缓存刷盘和普通异步刷盘的逻辑都差不多，最主要的区别就是异步堆外缓存刷盘服务并不会真正的执行flush刷盘，而是调用commit方法提交数据到fileChannel。</p>
 * <p>开启了异步堆外缓存服务之后，消息会先被追加到堆外内存writebuffer，
 * 然后异步（每最多200ms执行一次）的提交到文件通道FileChannel中，
 * 然后唤醒异步刷盘服务AsyncFlushService，由该AsyncFlushService服务（每最多500ms执行一次）
 * 最终异步的将MappedByteBuffer中的数据刷到磁盘。</p>
 * <p>开启了异步堆外缓存服务之后，写数据的时候写入堆外缓存writeBuffer中，而读取数据始终从MappedByteBuffer中读取，
 * 二者通过异步堆外缓存刷盘服务AsyncCommitService实现数据同步，
 * 该服务异步（最多200ms执行一次）的将堆外缓存writeBuffer中的脏数据提交到文件通道FileChannel中，
 * 而该文件被执行了内存映射mmap操作，因此可以从对应的MappedByteBuffer中直接获取提交到FileChannel的数据，但仍有延迟。/p>
 */
public class AsyncCommitService extends FlushCommitService {

    private long lastCommitTimestamp = 0;

    private final StoreRepository storeRepository;

    public AsyncCommitService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override

    public String getServiceName() {
        return AsyncCommitService.class.getSimpleName();
    }


    @Override
    public void run() {
        StoreRepository.log.info(this.getServiceName() + " service started");
        /*
         * 运行时逻辑
         * 如果服务没有停止，则在死循环中执行刷盘的操作
         */
        while (!this.isStopped()) {
            //获取刷盘间隔时间，默认200ms，可通过commitIntervalCommitLog配置
            int interval = 200;
            //获取刷盘的最少页数，默认4，即16k，可通过commitCommitLogLeastPages配置
            int commitDataLeastPages = 4;
            //最长刷盘延迟间隔时间，默认200ms，可通过commitCommitLogThoroughInterval配置，即距离上一次刷盘超过200ms时，不管页数是否超过4，都会刷盘
            int commitDataThoroughInterval = 200;

            long begin = System.currentTimeMillis();
            //如果当前时间距离上次刷盘时间大于等于200ms，那么必定刷盘，因此设置刷盘的最少页数为0，更新刷盘时间戳为当前时间
            if (begin >= (this.lastCommitTimestamp + commitDataThoroughInterval)) {
                this.lastCommitTimestamp = begin;
                //如果超出间隔了,有多少都提交
                commitDataLeastPages = 0;
            }

            try {
                /*
                 * 调用commit方法提交数据，而不是直接flush
                 */
                //提交
                boolean result = storeRepository.mappedFileQueue.commit(commitDataLeastPages);
                long end = System.currentTimeMillis();
                //调用mappedFileQueue.commit方法提交数据到fileChannel，而不是直接flush，
                //如果已经提交了一些脏数据到fileChannel，那么更新最后提交的时间戳，并且唤醒flushService异步刷盘服务进行真正的刷盘操作。
                if (!result) {
                    //更新最后提交的时间戳
                    this.lastCommitTimestamp = end; //result = false 表示提交了一些数据。
                    //唤醒flushCommitLogService异步刷盘服务进行刷盘操作
                    //现在唤醒刷新线程。
                    storeRepository.flushService.wakeup();
                }

                if (end - begin > 500) {
                    StoreRepository.log.info("Commit data to file costs {} ms", end - begin);
                }
                //调用waitForRunning方法，线程最多阻塞指定的间隔时间，但可以被中途的wakeup方法唤醒进而进行下一轮循环
                this.waitForRunning(interval);
            } catch (Throwable e) {
                StoreRepository.log.error(this.getServiceName() + " service has exception. ", e);
            }
        }
        /*
         * 停止时逻辑
         * 在正常情况下服务关闭时，一次性执行10次刷盘操作
         * 当刷盘服务被关闭时，默认执行10次刷盘（提交）操作，让消息尽量少丢失
         */
        boolean result = false;
        for (int i = 0; i < RETRY_TIMES_OVER && !result; i++) {
            result = storeRepository.mappedFileQueue.commit(0);
            StoreRepository.log.info(this.getServiceName() + " service shutdown, retry " + (i + 1) + " times " + (result ? "OK" : "Not OK"));
        }
        StoreRepository.log.info(this.getServiceName() + " service end");
    }
}
