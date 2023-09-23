package com.schilings.neiko.store.manage;


/**
 * <h1>异步刷盘</h1>
 * 
 */
public class AsyncFlushService extends FlushCommitService{

    private long lastFlushTimestamp = 0;
    private long printTimes = 0;

    private final StoreRepository storeRepository;

    public AsyncFlushService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public void run() {
        StoreRepository.log.info(this.getServiceName() + " service started");

        /*
         * 运行时逻辑
         * 如果服务没有停止，则在死循环中执行刷盘的操作
         */
        while (!this.isStopped()) {
            //是否是定时刷盘，默认是false，即不开启
            boolean flushCommitLogTimed = false;
            //获取刷盘间隔时间，默认500ms，可通过flushIntervalCommitLog配置
            int interval = 500;
            //获取刷盘的最少页数，默认4，即16k，可通过flushCommitLogLeastPages配置
            int flushPhysicQueueLeastPages = 4;
            //最长刷盘延迟间隔时间，默认10s，可通过flushCommitLogThoroughInterval配置，即距离上一次刷盘超过10S时，不管页数是否超过4，都会刷盘
            int flushPhysicQueueThoroughInterval = 10;

            boolean printFlushProgress = false;

            // Print flush progress
            long currentTimeMillis = System.currentTimeMillis();
            //如果当前时间距离上次刷盘时间大于等于10s，那么必定刷盘,不管页数是否超过4，都会刷盘。
            if (currentTimeMillis >= (this.lastFlushTimestamp + flushPhysicQueueThoroughInterval)) {
                //更新刷盘时间戳为当前时间
                this.lastFlushTimestamp = currentTimeMillis;
                //最少刷盘页数为0，即不管页数是否超过4，都会刷盘
                flushPhysicQueueLeastPages = 0;
                printFlushProgress = (printTimes++ % 10) == 0;
            }

            try {
                //判断是否是定时刷盘，如果定时刷盘，那么当前线程sleep睡眠指定的间隔时间 第一种机制，间隔多久刷新一次,
                if (flushCommitLogTimed) {
                    //如果定时刷盘，那么当前线程睡眠指定的间隔时间
                    Thread.sleep(interval);
                } else {
                    //第二种机制，等待唤醒
                    //如果不是定时刷盘，那么调用waitForRunning方法，线程最多睡眠500ms
                    //可以被中途的wakeup方法唤醒进而直接尝试进行刷盘
                    this.waitForRunning(interval);
                }

                if (printFlushProgress) {
                    this.printFlushProgress();
                }

                /*
                 * 开始刷盘 
                 */
                long begin = System.currentTimeMillis();
                /*
                 * 刷盘指定的页数 线程醒来后调用mappedFileQueue.flush方法刷盘，指定最少页数
                 */
                storeRepository.mappedFileQueue.flush(flushPhysicQueueLeastPages);
                //获取存储时间戳
                long storeTimestamp = storeRepository.mappedFileQueue.getStoreTimestamp();
                //随后更新最新commitlog文件的刷盘时间戳，单位毫秒，用于启动恢复。
                //修改StoreCheckpoint中的physicMsgTimestamp：最新文件的刷盘时间戳，单位毫秒
                //这里用于重启数据恢复
                if (storeTimestamp > 0) {
                    //设置最新偏移量
                    //storeRepository.defaultMessageStore.getStoreCheckpoint().setPhysicMsgTimestamp(storeTimestamp);
                }
                //刷盘消耗时间
                long past = System.currentTimeMillis() - begin;
                if (past > 500) {
                    StoreRepository.log.info("Flush data to disk costs {} ms", past);
                }
            } catch (Throwable e) {
                StoreRepository.log.warn(this.getServiceName() + " service has exception. ", e);
                this.printFlushProgress();
            }
        }

        /*
         * 停止时逻辑，当刷盘服务被关闭时，默认执行10次刷盘操作，让消息尽量少丢失。
         * 在正常情况下服务关闭时，一次性执行10次刷盘操作
         */
        //正常关机，确保退出前所有flush
        boolean result = false;
        for (int i = 0; i < RETRY_TIMES_OVER && !result; i++) {
            result = storeRepository.mappedFileQueue.flush(0);
            StoreRepository.log.info(this.getServiceName() + " service shutdown, retry " + (i + 1) + " times " + (result ? "OK" : "Not OK"));
        }

        this.printFlushProgress();

        StoreRepository.log.info(this.getServiceName() + " service end");
    }

    @Override
    public String getServiceName() {
        return AsyncFlushService.class.getSimpleName();
    }

    private void printFlushProgress() {
        // CommitLog.log.info("how much disk fall behind memory, "
        // + storeRepository.mappedFileQueue.howMuchFallBehind());
    }

    @Override
    public long getJointime() {
        return 1000 * 60 * 5;
    }
}
