package com.schilings.neiko.store.manage;


import com.schilings.neiko.logging.InternalLogger;
import com.schilings.neiko.logging.InternalLoggerFactory;
import com.schilings.neiko.svrutil.ServiceThread;
import com.schilings.neiko.store.manage.GroupFlushService.GroupCommitRequest;

import java.util.concurrent.LinkedBlockingQueue;

public class FlushDiskWatcher extends ServiceThread {

    private static final InternalLogger log = InternalLoggerFactory.getLogger("FlushDiskWatcher");
    private final LinkedBlockingQueue<GroupCommitRequest> commitRequests = new LinkedBlockingQueue<>();

    @Override
    public String getServiceName() {
        return FlushDiskWatcher.class.getSimpleName();
    }
    @Override
    public void run() {
        while (!isStopped()) {
            
            GroupCommitRequest request = null;
            try {
                request = commitRequests.take();
            } catch (InterruptedException e) {
                log.warn("take flush disk commit request, but interrupted, this may caused by shutdown");
                continue;
            }
            //如果存储请求没被处理完成，以超时结束
            while (!request.future().isDone()) {
                long now = System.nanoTime();
                if (now - request.getDeadLine() >= 0) {
                    request.wakeupCustomer(StoreStatus.FLUSH_DISK_TIMEOUT);
                    break;
                }
                // 为了避免频繁的线程切换，这里将future.get替换为sleep，
                long sleepTime = (request.getDeadLine() - now) / 1_000_000;
                sleepTime = Math.min(10, sleepTime);
                if (sleepTime == 0) {
                    request.wakeupCustomer(StoreStatus.FLUSH_DISK_TIMEOUT);
                    break;
                }
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    log.warn(
                            "An exception occurred while waiting for flushing disk to complete. this may caused by shutdown");
                    break;
                }
            }
        }
    }

    public void add(GroupCommitRequest request) {
        commitRequests.add(request);
    }

    public int queueSize() {
        return commitRequests.size();
    }
}
