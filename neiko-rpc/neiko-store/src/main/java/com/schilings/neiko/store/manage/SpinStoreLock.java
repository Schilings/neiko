package com.schilings.neiko.store.manage;


import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 自旋锁实现放置消息，建议在低竞争条件下使用
 */
public class SpinStoreLock implements StoreLock{
    //true: Can lock, false : in lock.
    private AtomicBoolean putSpinLock = new AtomicBoolean(true);

    @Override
    public void lock() {
        boolean flag;
        do {
            flag = this.putSpinLock.compareAndSet(true, false);
        }
        while (!flag);
    }

    @Override
    public void unlock() {
        this.putSpinLock.compareAndSet(false, true);
    }
}
