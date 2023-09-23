package com.schilings.neiko.store.manage;


import com.schilings.neiko.logging.InternalLogger;
import com.schilings.neiko.logging.InternalLoggerFactory;
import com.schilings.neiko.svrutil.ServiceThread;

public abstract class FlushCommitService extends ServiceThread {
    
    protected static final int RETRY_TIMES_OVER = 10;
}
