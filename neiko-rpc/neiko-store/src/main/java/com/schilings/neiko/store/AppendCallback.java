package com.schilings.neiko.store;

import com.schilings.neiko.store.manage.StoreData;

import java.nio.ByteBuffer;

/**
 * 写消息回调接口
 */
public interface AppendCallback {

    /**
     * 消息序列化后，写入MapedByteBuffer
     */
    AppendResult doAppend(final long fileFromOffset, final ByteBuffer byteBuffer, final int maxBlank,final StoreData data);

}
