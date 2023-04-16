package com.schilings.neiko.store.manage;


import java.util.concurrent.locks.ReentrantLock;

/**
 * 排他锁实现
 */
public class ReentrantStoreLock implements StoreLock{

    private ReentrantLock putMessageNormalLock = new ReentrantLock(); // NonfairSync

    @Override
    public void lock() {
        putMessageNormalLock.lock();
    }

    @Override
    public void unlock() {
        putMessageNormalLock.unlock();
    }
}
