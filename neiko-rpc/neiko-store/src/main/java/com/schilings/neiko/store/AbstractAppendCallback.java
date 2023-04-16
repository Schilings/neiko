package com.schilings.neiko.store;


import com.schilings.neiko.store.manage.StoreData;

import java.nio.ByteBuffer;

public abstract class AbstractAppendCallback implements AppendCallback {

    /**
     * 文件末尾最小固定长度为空
     */
    private static final int END_FILE_MIN_BLANK_LENGTH = 4 + 4;

    /**
     * 文件结尾为空 MAGIC CODE cbd43194
     */
    protected final static int BLANK_MAGIC_CODE = -875286124;

    /**
     * 存储消息内容
     */
    private final ByteBuffer msgStoreItemMemory;
    /**
     * 最大长度
     */
    private final int maxMessageSize;

    public AbstractAppendCallback(final int size) {
        this.msgStoreItemMemory = ByteBuffer.allocate(END_FILE_MIN_BLANK_LENGTH);
        this.maxMessageSize = size;
    }


    /**
     * @param fileFromOffset
     * @param byteBuffer
     * @param maxBlank
     * @return
     */
    @Override
    public AppendResult doAppend(long fileFromOffset, ByteBuffer byteBuffer, int maxBlank, StoreData data) {
        // PHY OFFSET
        long wroteOffset = fileFromOffset + byteBuffer.position();
        final int msgLen = data.getBody().length;
        final long beginTimeMills = System.currentTimeMillis();
        //确定是否有足够的可用空间
        if ((msgLen + END_FILE_MIN_BLANK_LENGTH) > maxBlank) {
            this.msgStoreItemMemory.clear();
            // 1 总大小
            this.msgStoreItemMemory.putInt(maxBlank);
            // 2 魔数
            this.msgStoreItemMemory.putInt(BLANK_MAGIC_CODE);
            // 3 剩余空间可以是任意值 这里是专门设置的maxBlank的长度
            byteBuffer.put(this.msgStoreItemMemory.array(), 0, 8);
            return new AppendResult(AppendStatus.END_OF_FILE,
                    wroteOffset,
                    maxBlank,//只写了 8 个字节，但声明为计算写入位置写入了 maxBlank
                    System.currentTimeMillis(),
                    System.currentTimeMillis() - beginTimeMills);
        }

        byteBuffer.put(data.getBody());
        return new AppendResult(AppendStatus.PUT_OK, wroteOffset, msgLen,
                System.currentTimeMillis(), System.currentTimeMillis() - beginTimeMills);
    }
    
}

