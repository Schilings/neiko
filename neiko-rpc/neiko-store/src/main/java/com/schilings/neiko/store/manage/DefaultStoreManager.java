package com.schilings.neiko.store.manage;


import com.schilings.neiko.logging.InternalLogger;
import com.schilings.neiko.logging.InternalLoggerFactory;
import com.schilings.neiko.store.AllocateMappedFileService;
import com.schilings.neiko.store.MappedFile;
import com.schilings.neiko.store.SelectMappedBufferResult;
import com.schilings.neiko.store.TransientStorePool;
import com.schilings.neiko.store.config.StoreConfig;
import com.schilings.neiko.store.config.StorePathConfigHelper;
import com.schilings.neiko.store.ha.HAService;
import com.schilings.neiko.svrutil.ThreadFactoryImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@RequiredArgsConstructor
public class DefaultStoreManager {
    private static final InternalLogger log = InternalLoggerFactory.getLogger("StoreManager");
    
    private final StoreConfig storeConfig;

    private final HAService haService;
    
    private final StoreRepository storeRepository;
    private final AllocateMappedFileService allocateMappedFileService;
    private final TransientStorePool transientStorePool;
    
    private final ScheduledExecutorService scheduledExecutorService =
            Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl("StoreScheduledThread"));
    
    private volatile boolean shutdown = true;

    private StoreCheckpoint storeCheckpoint;
    

    private RandomAccessFile lockFile;

    private FileLock lock;

    boolean shutDownNormal = false;

    private final ScheduledExecutorService diskCheckScheduledExecutorService =
            Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl("DiskCheckScheduledThread"));


    public DefaultStoreManager(final StoreConfig storeConfig) throws IOException {
        //存储配置，例如各种文件大小等
        this.storeConfig = storeConfig;
        this.storeRepository = new StoreRepository(this);
        //初始化MappedFile的时候进行ByteBuffer的分配回收
        this.transientStorePool = new TransientStorePool(storeConfig.getTransientStorePoolSize(),storeConfig.getMappedFileSize());
        //创建 MappedFile文件的服务，用于初始化MappedFile和预热MappedFile
        this.allocateMappedFileService = new AllocateMappedFileService(transientStorePool);
        //如果当前节点不是从节点，并且是异步刷盘策略，并且transientStorePoolEnable参数配置为true，则启动该服务
        if (storeConfig.isTransientStorePoolEnable()) {
            this.transientStorePool.init();
        }
        //启动MappedFile文件服务线程
        this.allocateMappedFileService.start();
        //获取锁文件，路径就是配置的{storePathRootDir}/lock
        File file = new File(StorePathConfigHelper.getLockFile(storeConfig.getStorePathRootDir()));
        //确保创建file文件的父目录，即{storePathRootDir}目录
        MappedFile.ensureDirOK(file.getParent());
        //创建lockfile文件，名为lock，权限是"读写"，这是一个锁文件，用于获取文件锁。
        //文件锁用来保证磁盘上的这些存储文件同时只能有一个Broker的messageStore来操作。
        lockFile = new RandomAccessFile(file, "rw");

        haService = null;
    }


    public void unlockMappedFile(final MappedFile mappedFile) {
        this.scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                mappedFile.munlock();
            }
        }, 6, TimeUnit.SECONDS);
    }

 
    public long getMaxPhyOffset() {
        return this.storeRepository.getMaxOffset();
    }

    public boolean appendToRepository(long startOffset, byte[] data, int dataStart, int dataLength) {
        if (this.shutdown) {
            log.warn("message store has shutdown, so appendToPhyQueue is forbidden");
            return false;
        }

        boolean result = this.storeRepository.appendData(startOffset, data, dataStart, dataLength);
        if (result) {
            //this.reputMessageService.wakeup();
        } else {
            log.error("appendToPhyQueue failed " + startOffset + " " + data.length);
        }

        return result;
    }

    public SelectMappedBufferResult getData(final long offset) {
        if (this.shutdown) {
            log.warn("message store has shutdown, so getPhyQueueData is forbidden");
            return null;
        }

        return this.storeRepository.getData(offset);
    }
}
