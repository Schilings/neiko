package com.schilings.neiko.store.manage;


import com.schilings.neiko.logging.InternalLogger;
import com.schilings.neiko.logging.InternalLoggerFactory;
import com.schilings.neiko.store.*;
import com.schilings.neiko.store.config.FlushDiskType;
import com.schilings.neiko.store.manage.GroupFlushService.GroupCommitRequest;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;

public class StoreRepository {

    protected static final InternalLogger log = InternalLoggerFactory.getLogger("StoreRepository");

    protected final MappedFileQueue mappedFileQueue;
    

    /**
     * 刷盘服务
     */
    protected final FlushCommitService flushService;
    
    /**
     * 如果启用了 TransientStorePool，我们必须定期将消息刷新到 FileChannel
     */
    protected final FlushCommitService commitService;
    protected StoreCheckpoint storeCheckpoint;


    protected volatile long confirmOffset = -1L;

    private volatile long beginTimeInLock = 0;
    private final StoreLock putLock;

    private final FlushDiskWatcher flushDiskWatcher;

    private AppendCallback appendCallback = new DefaultAppendCallback();
    

    private final DefaultStoreManager defaultStoreManager;

    public StoreRepository(DefaultStoreManager defaultStoreManager) {
        String storePath = defaultStoreManager.getStoreConfig().getStorePathData();
        this.defaultStoreManager = defaultStoreManager;
        this.mappedFileQueue = new MappedFileQueue(storePath,
                defaultStoreManager.getStoreConfig().getMappedFileSize(),
                defaultStoreManager.getAllocateMappedFileService());

        if (FlushDiskType.SYNC_FLUSH == defaultStoreManager.getStoreConfig().getFlushDiskType()) {
            this.flushService = new GroupFlushService(this);
        } else {
            this.flushService = new AsyncFlushService(this);
        }

        this.commitService = new AsyncCommitService(this);
        
        this.putLock = defaultStoreManager.getStoreConfig().isUseReentrantLockWhenPutMessage() ? 
                new ReentrantStoreLock() : new SpinStoreLock();
        this.flushDiskWatcher = new FlushDiskWatcher();
    }

    
    public boolean load() {
        //调用mappedFileQueue的load方法
        boolean result = this.mappedFileQueue.load();
        log.info("load store log " + (result ? "OK" : "Failed"));
        return result;
    }

    public void start() {
        //刷盘线程
        this.flushService.start();

        flushDiskWatcher.setDaemon(true);
        flushDiskWatcher.start();

        //如果有开启，则开启提交线程
        if (defaultStoreManager.getStoreConfig().isTransientStorePoolEnable()) {
            this.commitService.start();
        }
    }

    public void shutdown() {
        //提交线程
        if (defaultStoreManager.getStoreConfig().isTransientStorePoolEnable()) {
            this.commitService.shutdown();
        }
        //刷盘线程
        this.flushService.shutdown();

        flushDiskWatcher.shutdown(true);
    }

    public long flush() {
        this.mappedFileQueue.commit(0);
        this.mappedFileQueue.flush(0);
        return this.mappedFileQueue.getFlushedWhere();
    }

    public long getMaxOffset() {
        return this.mappedFileQueue.getMaxOffset();
    }

    public long remainHowManyDataToCommit() {
        return this.mappedFileQueue.remainHowManyDataToCommit();
    }

    public long remainHowManyDataToFlush() {
        return this.mappedFileQueue.remainHowManyDataToFlush();
    }

    public int deleteExpiredFile(
            final long expiredTime,
            final int deleteFilesInterval,
            final long intervalForcibly,
            final boolean cleanImmediately
    ) {
        return this.mappedFileQueue.deleteExpiredFileByTime(expiredTime, deleteFilesInterval, intervalForcibly, cleanImmediately);
    }

    /**
     *
     * 获取数据
     */
    public SelectMappedBufferResult getData(final long offset) {
        return this.getData(offset, offset == 0);
    }

    public SelectMappedBufferResult getData(final long offset, final boolean returnFirstOnNotFound) {
        //获取CommitLog文件大小，默认1G
        int mappedFileSize = this.defaultStoreManager.getStoreConfig().getMappedFileSize();
        //根据指定的offset从mappedFileQueue中对应的CommitLog文件的MappedFile
        MappedFile mappedFile = this.mappedFileQueue.findMappedFileByOffset(offset, returnFirstOnNotFound);
        if (mappedFile != null) {
            //通过指定物理偏移量，除以文件大小，得到指定的相对偏移量
            int pos = (int) (offset % mappedFileSize);
            //从指定相对偏移量开始截取一段ByteBuffer，这段内存存储着将要重放的消息。
            SelectMappedBufferResult result = mappedFile.selectMappedBuffer(pos);
            return result;
        }
        return null;
    }



    public CompletableFuture<PutStoreResult> asyncAppend(StoreData data){
        long elapsedTimeInLock = 0;
        MappedFile unlockMappedFile = null;
        AppendResult result = null;

        putLock.lock(); 
        try {
            MappedFile mappedFile = mappedFileQueue.getLastMappedFile();
            long beginLockTimestamp = System.currentTimeMillis();
            this.beginTimeInLock = beginLockTimestamp;

            if (null == mappedFile || mappedFile.isFull()) {
                mappedFile = this.mappedFileQueue.getLastMappedFile(0); // NewFile 可能会引起噪音
            }
            if (null == mappedFile) {
                log.error("create mapped file1 error");
                return CompletableFuture.completedFuture(new PutStoreResult(StoreStatus.CREATE_MAPEDFILE_FAILED, null));
            }
            result = mappedFile.append(data, appendCallback);
            switch (result.getStatus()) {
                case PUT_OK -> {
                    //do nothing
                }
                case END_OF_FILE -> {
                    unlockMappedFile = mappedFile;
                    // Create a new file, re-write the message
                    mappedFile = this.mappedFileQueue.getLastMappedFile(0);
                    if (null == mappedFile) {
                        // XXX: warn and notify me
                        log.error("create mapped file2 error");
                        return CompletableFuture.completedFuture(new PutStoreResult(StoreStatus.CREATE_MAPEDFILE_FAILED, result));
                    }
                    result = mappedFile.append(data, appendCallback);
                }
                case MESSAGE_SIZE_EXCEEDED->{}
                case PROPERTIES_SIZE_EXCEEDED->{
                    return CompletableFuture.completedFuture(new PutStoreResult(StoreStatus.MESSAGE_ILLEGAL, result));
                }
                case UNKNOWN_ERROR->{
                    return CompletableFuture.completedFuture(new PutStoreResult(StoreStatus.UNKNOWN_ERROR, result));
                }
                default -> {
                    return CompletableFuture.completedFuture(new PutStoreResult(StoreStatus.UNKNOWN_ERROR, result));
                }
            }
            elapsedTimeInLock = System.currentTimeMillis() - beginLockTimestamp;
        }finally {
            beginTimeInLock = 0;
            putLock.unlock();
        }

        if (elapsedTimeInLock > 500) {
            log.warn("[NOTIFYME]putMessage in lock cost time(ms)={}, bodyLength={} AppendMessageResult={}", elapsedTimeInLock, data.getBody().length, result);
        }

        if (null != unlockMappedFile && this.defaultStoreManager.getStoreConfig().isWarmMapedFileEnable()) {
            this.defaultStoreManager.unlockMappedFile(unlockMappedFile);
        }

        PutStoreResult putStoreResult = new PutStoreResult(StoreStatus.PUT_OK, result);

        CompletableFuture<StoreStatus> flushOKFuture = submitFlushRequest(result, data);
        return flushOKFuture.thenApply(flushStatus -> {
            if (flushStatus != StoreStatus.PUT_OK) {
                putStoreResult.setStoreStatus(flushStatus);
            }
            return putStoreResult;
        });

    }




    /**
     * CommitLog的方法
     * <p>
     * 提交刷盘请求
     */
    public CompletableFuture<StoreStatus> submitFlushRequest(AppendResult result, StoreData data) {
        // Synchronization flush
        /*
         * 同步刷盘策略
         */
        if (FlushDiskType.SYNC_FLUSH == this.defaultStoreManager.getStoreConfig().getFlushDiskType()) {
            //获取同步刷盘服务GroupCommitService
            final GroupFlushService service = (GroupFlushService) this.flushService;
            //判断消息的配置是否需要等待存储完成后才返回
            if (data.isWaitStoreMsgOK()) {
                //同步刷盘并且需要等待刷刷盘结果
                //构建同步刷盘请求 刷盘偏移量nextOffset = 当前写入偏移量 + 当前消息写入大小
                GroupCommitRequest request = new GroupCommitRequest(result.getWroteOffset() + result.getWroteBytes(),
                        this.defaultStoreManager.getStoreConfig().getSyncFlushTimeout());
                //将请求加入到刷盘监视器内部的commitRequests中
                flushDiskWatcher.add(request);
                //将请求存入内部的requestsWrite，并且唤醒同步刷盘线程
                service.putRequest(request);
                //仅仅返回future，没有填充结果
                return request.future();
            } else {
                //同步刷盘但是不需要等待刷盘结果，那么唤醒同步刷盘线程，随后直接返回PUT_OK
                service.wakeup();
                return CompletableFuture.completedFuture(StoreStatus.PUT_OK);
            }
        }
        // Asynchronous flush
        /*
         * 异步刷盘策略
         */
        else {
            //是否启动了堆外缓存
            if (!this.defaultStoreManager.getStoreConfig().isTransientStorePoolEnable()) {
                //如果没有启动了堆外缓存，那么唤醒异步刷盘服务FlushRealTimeService
                flushService.wakeup();
            } else  {
                //如果启动了堆外缓存，那么唤醒异步转存服务CommitRealTimeService
                commitService.wakeup();
            }
            return CompletableFuture.completedFuture(StoreStatus.PUT_OK);
        }
    }
    
    
    public long getMinOffset() {
        MappedFile mappedFile = this.mappedFileQueue.getFirstMappedFile();
        if (mappedFile != null) {
            if (mappedFile.isAvailable()) {
                return mappedFile.getFileFromOffset();
            } else {
                return this.rollNextFile(mappedFile.getFileFromOffset());
            }
        }

        return -1;
    }
    
    public SelectMappedBufferResult getMessage(final long offset, final int size) {
        int mappedFileSize = this.defaultStoreManager.getStoreConfig().getMappedFileSize();
        MappedFile mappedFile = this.mappedFileQueue.findMappedFileByOffset(offset, offset == 0);
        if (mappedFile != null) {
            int pos = (int) (offset % mappedFileSize);
            return mappedFile.selectMappedBuffer(pos, size);
        }
        return null;
    }

    public long rollNextFile(final long offset) {
        int mappedFileSize = this.defaultStoreManager.getStoreConfig().getMappedFileSize();
        return offset + mappedFileSize - offset % mappedFileSize;
    }

    public boolean appendData(long startOffset, byte[] data, int dataStart, int dataLength) {
        putLock.lock();
        try {
            MappedFile mappedFile = this.mappedFileQueue.getLastMappedFile(startOffset);
            if (null == mappedFile) {
                log.error("appendData getLastMappedFile error  " + startOffset);
                return false;
            }

            return mappedFile.append(data, dataStart, dataLength);
        } finally {
            putLock.unlock();
        }
    }

    public boolean retryDeleteFirstFile(final long intervalForcibly) {
        return this.mappedFileQueue.retryDeleteFirstFile(intervalForcibly);
    }

    public void checkSelf() {
        mappedFileQueue.checkSelf();
    }

    public long lockTimeMills() {
        long diff = 0;
        long begin = this.beginTimeInLock;
        if (begin > 0) {
            diff = System.currentTimeMillis() - begin;
        }

        if (diff < 0) {
            diff = 0;
        }

        return diff;
    }
}
