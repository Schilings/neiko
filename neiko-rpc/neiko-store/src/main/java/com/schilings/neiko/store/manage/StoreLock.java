package com.schilings.neiko.store.manage;

/**
 * 尝试放置消息时使用
 */
public interface StoreLock {

    void lock();

    void unlock();
}
