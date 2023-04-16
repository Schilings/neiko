package com.schilings.neiko.store.config;


import com.schilings.neiko.store.config.FlushDiskType;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class StoreConfig {

    private String storePathRootDir = System.getProperty("user.home") + File.separator + "store";


    private String storePathData = System.getProperty("user.home") + File.separator + "store"
            + File.separator + "data";
    
    /**
     * 判断放消息时是否使用mutex reentrantLock
     */
    private boolean useReentrantLockWhenPutMessage = true;

    // How many pages are to be flushed when flush CommitLog
    private int flushCommitLogLeastPages = 4;
    // How many pages are to be committed when commit data to file
    private int commitCommitLogLeastPages = 4;
    // Flush page size when the disk in warming state
    private int flushLeastPagesWhenWarmMapedFile = 1024 / 4 * 16;
    

    private int mappedFileSize = 1024 * 1024 * 1024;
    
    private FlushDiskType flushDiskType = FlushDiskType.ASYNC_FLUSH;

    private int syncFlushTimeout = 1000 * 5;

    private long flushDelayOffsetInterval = 1000 * 10;
    

    private boolean warmMapedFileEnable = false;

    private boolean transientStorePoolEnable = false;

    private int transientStorePoolSize = 5;
    private boolean fastFailIfNoBufferInStorePool = false;



    /**
     * Enable transient commitLog store pool only if transientStorePoolEnable is true and the FlushDiskType is
     * ASYNC_FLUSH
     * 仅当transientStorePoolEnable为true（默认false）且FlushDiskType为ASYNC_FLUSH且当前broker不是SLAVE角色时，才启用commitLog临时存储池
     *
     * @return <tt>true</tt> or <tt>false</tt>
     */
    public boolean isTransientStorePoolEnable() {
        return transientStorePoolEnable && FlushDiskType.ASYNC_FLUSH == getFlushDiskType();
    }
    
}
