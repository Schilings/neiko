package com.schilings.neiko.store;


import com.schilings.neiko.logging.InternalLogger;
import com.schilings.neiko.logging.InternalLoggerFactory;
import com.schilings.neiko.store.config.FlushDiskType;
import com.schilings.neiko.store.util.CLibrary;
import com.schilings.neiko.svrutil.UtilAll;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import sun.nio.ch.DirectBuffer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * <p></p>
 * 
 * @author Schilings
*/
public class MappedFile extends ReferenceResource{
    
    public static final int OS_PAGE_SIZE = 1024 * 4;

    protected static final InternalLogger log = InternalLoggerFactory.getLogger(MappedFile.class);
    
    //类 final 所有线程可见
    /**
     * 映射的虚拟机内存大小
     */
    private static final AtomicLong TOTAL_MAPPED_VIRTUAL_MEMORY = new AtomicLong(0);
    /**
     * 映射的文件数
     */
    private static final AtomicInteger TOTAL_MAPPED_FILES = new AtomicInteger(0);

    //实例
    /**
     * 已写偏移量
     */
    protected final AtomicInteger wrotePosition = new AtomicInteger(0);

    protected final AtomicInteger committedPosition = new AtomicInteger(0);
    /**
     * 已刷新到磁盘的偏移量
     */
    private final AtomicInteger flushedPosition = new AtomicInteger(0);

    /**
     * 用于写入但未提交的缓冲区，即实际还为写入
     * 会先放到这里，如果 writeBuffer 不为空，再放到 FileChannel 中。
     */
    protected ByteBuffer writeBuffer = null;
    /**
     * 缓存存储池，用于配合writeBuffer
     */
    protected TransientStorePool transientStorePool = null;

    /**
     * 映射的文件大小
     */
    protected int fileSize;
    /**
     * 映射的文件名
     */
    private String fileName;
    /**
     * 映射的文件通道
     */
    protected FileChannel fileChannel;
    /**
     * 映射的文件实例
     */
    private File file;
    /**
     * 映射的文件内存块
     */
    private MappedByteBuffer mappedByteBuffer;
    
    /**
     * 以文件存储起始offset作为文件名，通常是20位的整数,相邻的两个文件差就能算出文件大小
     * 如分片文件保存
     */
    private long fileFromOffset;
    
    
    private volatile long storeTimestamp = 0;
    private boolean firstCreateInQueue = false;

    public MappedFile() {
    }

    public MappedFile(final String fileName, final int fileSize) throws IOException {
        init(fileName, fileSize);
    }
    
    public MappedFile(final String fileName, final int fileSize, final TransientStorePool transientStorePool) throws IOException {
        init(fileName, fileSize, transientStorePool);
    }

    /**
     * 检验目录
     * @param dirName
     */
    public static void ensureDirOK(final String dirName) {
        if (dirName != null) {
            File f = new File(dirName);
            if (!f.exists()) {
                boolean result = f.mkdirs();
                log.info(dirName + " mkdir " + (result ? "OK" : "Failed"));
            }
        }
    }

    /**
     * 清理 直接内存，即拿到直接内存的cleaner实例调用其clean方法，“手动GC该实例” 虚引用
     * @param buffer
     */
    public static void clean(final ByteBuffer buffer) {
        if (buffer == null || !buffer.isDirect() || buffer.capacity() == 0)
            return;
        invoke(invoke(viewed(buffer), "cleaner"), "clean");
    }

    /**
     * 反射调用实例方法
     * @param target
     * @param methodName
     * @param args
     * @return
     */
    private static Object invoke(final Object target, final String methodName, final Class<?>... args) {
        return AccessController.doPrivileged(new PrivilegedAction<Object>() {
            public Object run() {
                try {
                    Method method = method(target, methodName, args);
                    method.setAccessible(true);
                    return method.invoke(target);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
        });
    }

    /**
     * 反射获取实例方法
     * @param target
     * @param methodName
     * @param args
     * @return
     * @throws NoSuchMethodException
     */
    private static Method method(Object target, String methodName, Class<?>[] args) throws NoSuchMethodException {
        try {
            return target.getClass().getMethod(methodName, args);
        } catch (NoSuchMethodException e) {
            return target.getClass().getDeclaredMethod(methodName, args);
        }
    }

    /**
     * 找出最底层直接内存 {@link DirectBuffer}
     * @param buffer
     * @return
     */
    private static ByteBuffer viewed(ByteBuffer buffer) {
        String methodName = "viewedBuffer";
        Method[] methods = buffer.getClass().getMethods();
        /**
         * {@link DirectBuffer#attachment()}
         */
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals("attachment")) {
                methodName = "attachment";
                break;
            }
        }

        ByteBuffer viewedBuffer = (ByteBuffer) invoke(buffer, methodName);
        if (viewedBuffer == null)
            return buffer;
        else
            return viewed(viewedBuffer);
    }

    public static int getTotalMappedFiles() {
        return TOTAL_MAPPED_FILES.get();
    }

    public static long getTotalMappedVirtualMemory() {
        return TOTAL_MAPPED_VIRTUAL_MEMORY.get();
    }


    public void init(final String fileName, final int fileSize, final TransientStorePool transientStorePool) throws IOException {
        init(fileName, fileSize);
        //设置写buffer，采用堆外内存
        //如果开启了堆外内存，那么将采用此方式创建MappedFile，其相比于mmap的方式，多了一步操作，即会设置一个writeBuffer。
        this.writeBuffer = transientStorePool.borrowBuffer();
        this.transientStorePool = transientStorePool;
    }

    /**
     * 初始化创建一个空文件作为存储文件
     * @param fileName
     * @param fileSize
     * @throws IOException
     */
    private void init(final String fileName, final int fileSize) throws IOException {
        //文件名。长度为20位，左边补零，剩余为起始偏移量，比如00000000000000000000代表了第一个文件，起始偏移量为0
        this.fileName = fileName;
        //文件大小。默认1G=1073741824
        this.fileSize = fileSize;
        //构建file对象
        this.file = new File(fileName);
        //构建文件起始索引，就是取自文件名
        this.fileFromOffset = Long.parseLong(this.file.getName());
        boolean ok = false;
        //确保文件目录存在
        ensureDirOK(this.file.getParent());

        try {
            //构建文件通道fileChannel
            this.fileChannel = new RandomAccessFile(this.file, "rw").getChannel();
            //文件完全的映射到虚拟内存，也就是内存映射，即mmap，提升读写性能
            this.mappedByteBuffer = this.fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
            //内存计数
            TOTAL_MAPPED_VIRTUAL_MEMORY.addAndGet(fileSize);
            //文件计数
            TOTAL_MAPPED_FILES.incrementAndGet();
            ok = true;
        } catch (FileNotFoundException e) {
            log.error("Failed to create file " + this.fileName, e);
            throw e;
        } catch (IOException e) {
            log.error("Failed to map file " + this.fileName, e);
            throw e;
        } finally {
            if (!ok && this.fileChannel != null) {
                this.fileChannel.close();
            }
        }
    }

    @Override
    public boolean cleanup(long currentRef) {
        if (this.isAvailable()) {
            log.error("this file[REF:" + currentRef + "] " + this.fileName
                    + " have not shutdown, stop unmapping.");
            return false;
        }

        if (this.isCleanupOver()) {
            log.error("this file[REF:" + currentRef + "] " + this.fileName
                    + " have cleanup, do not do it again.");
            return true;
        }
        //清理直接内存
        clean(this.mappedByteBuffer);
        //内存计数
        TOTAL_MAPPED_VIRTUAL_MEMORY.addAndGet(this.fileSize * (-1));
        //文件计数
        TOTAL_MAPPED_FILES.decrementAndGet();
        log.info("unmap file[REF:" + currentRef + "] " + this.fileName + " OK");
        return true;
    }

    /**
     * 销毁
     * @param intervalForcibly 距离第一次销毁的时间间隔
     * @return
     */
    public boolean destroy(final long intervalForcibly) {
        //关闭，即不再使用
        this.shutdown(intervalForcibly);
        //检验是否清理完成
        if (this.isCleanupOver()) {
            try {
                //关闭通道
                this.fileChannel.close();
                log.info("close file channel " + this.fileName + " OK");
                //删除映射的文件
                long beginTime = System.currentTimeMillis();
                boolean result = this.file.delete();
                log.info("delete file[REF:" + this.getRefCount() + "] " + this.fileName
                        + (result ? " OK, " : " Failed, ") + "W:" + this.getWrotePosition() + " M:"
                        + this.getFlushedPosition() + ", " + UtilAll.computeElapsedTimeMilliseconds(beginTime));
            } catch (Exception e) {
                log.warn("close file channel " + this.fileName + " Failed. ", e);
            }
            return true;
        } else {
            log.warn("destroy mapped file[REF:" + this.getRefCount() + "] " + this.fileName
                    + " Failed. cleanupOver: " + this.cleanupOver);
        }

        return false;
    }

    /**
     * 锁定内存
     * 当实现了文件内存预热之后，虽然短时间不会读取数据不会引发缺页异常，
     * 但是当内存不足的时候，一部分不常使用的内存还是会被交换到swap空间中，当程序再次读取交换出去的数据的时候会再次产生缺页异常。
     */
    public void mlock() {
        final long beginTime = System.currentTimeMillis();
        final long address = ((DirectBuffer) (this.mappedByteBuffer)).address();
        Pointer pointer = new Pointer(address);
        {
            //调用系统mlock函数，锁定该文件的Page Cache，防止把预热过的文件被操作系统调到swap空间中。
            int ret = CLibrary.INSTANCE.mlock(pointer, new NativeLong(this.fileSize));
            log.info("mlock {} {} {} ret = {} time consuming = {}", address, this.fileName, this.fileSize, ret, System.currentTimeMillis() - beginTime);
        }
        {
            //另外还会调用系统madvise函数，再次尝试一次性先将一段数据读入到映射内存区域，这样就减少了缺页异常的产生。
            int ret = CLibrary.INSTANCE.madvise(pointer, new NativeLong(this.fileSize), CLibrary.MADV_WILLNEED);
            log.info("madvise {} {} {} ret = {} time consuming = {}", address, this.fileName, this.fileSize, ret, System.currentTimeMillis() - beginTime);
        }
    }

    /**
     * 内存释放锁
     */
    public void munlock() {
        final long beginTime = System.currentTimeMillis();
        final long address = ((DirectBuffer) (this.mappedByteBuffer)).address();
        Pointer pointer = new Pointer(address);
        int ret = CLibrary.INSTANCE.munlock(pointer, new NativeLong(this.fileSize));
        log.info("munlock {} {} {} ret = {} time consuming = {}", address, this.fileName, this.fileSize, ret, System.currentTimeMillis() - beginTime);
    }

    /**
     * 用文件预热，即让操作系统提前分配物理内存空间，防止在写入消息时发生缺页异常才进行分配。
     * 
     * mmap操作减少了传统IO将磁盘文件数据在操作系统内核地址空间的缓冲区和用户应用程序地址空间的缓冲区之间来回进行拷贝的性能开销，这是它的好处。
     *
     * 但是mmap操作对于OS来说只是建立虚拟内存地址至物理地址的映射关系，即将进程使用的虚拟内存地址映射到物理地址上。
     * 实际上并不会加载任何MappedFile数据至内存中，也并不会分配指定的大小的内存。
     * 当程序要访问数据时，如果发现这部分数据页并没有实际加载到内存中，则处理器自动触发一个缺页异常，
     * 进而进入内核空间再分配物理内存，一次分配大小默认4k。
     * 一个G大小的CommitLog，如果靠着缺页中断来分配实际内存，那么需要触发26w多次缺页中断，这是一笔不小的开销。
     * 
     * @param type 消息刷盘类型，默认 FlushDiskType.ASYNC_FLUSH;
     * @param pages 一页大小，默认4k
     */
    public void warmMappedFile(FlushDiskType type, int pages) {
        long beginTime = System.currentTimeMillis();
        // 创建一个新的字节缓冲区
        ByteBuffer byteBuffer = this.mappedByteBuffer.slice();
        int flush = 0;
        long time = System.currentTimeMillis();
        for (int i = 0, j = 0; i < this.fileSize; i += MappedFile.OS_PAGE_SIZE, j++) {
            //每隔4k大小写入一个0
            byteBuffer.put(i, (byte) 0);
            //如果是同步刷盘，则每次写入都要强制刷盘
            if (type == FlushDiskType.SYNC_FLUSH) {
                if ((i / OS_PAGE_SIZE) - (flush / OS_PAGE_SIZE) >= pages) {
                    flush = i;
                    //写入包含映射文件的存储设备
                    mappedByteBuffer.force();
                }
            }

            // 防止 gc
            //调用Thread.sleep(0)当前线程主动放弃CPU资源，立即进入就绪状态
            //防止因为多次循环导致该线程一直抢占着CPU资源不释放，
            if (j % 1000 == 0) {
                log.info("j={}, costTime={}", j, System.currentTimeMillis() - time);
                time = System.currentTimeMillis();
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    log.error("Interrupted", e);
                }
            }
        }

        // 准备加载完成时强制刷新 把剩余的数据强制刷新到磁盘中
        if (type == FlushDiskType.SYNC_FLUSH) {
            log.info("mapped file warm-up done, force to disk, mappedFile={}, costTime={}", this.getFileName(), System.currentTimeMillis() - beginTime);
            mappedByteBuffer.force();
        }
        log.info("mapped file warm-up done. mappedFile={}, costTime={}", this.getFileName(), System.currentTimeMillis() - beginTime);

        //锁定内存
        this.mlock();
    }


    /**
     * 追加内容,作用于{@link MappedFile#mappedByteBuffer}
     * @param data
     * @return
     */
    public boolean append(final byte[] data) {
        int currentPos = this.wrotePosition.get();
        //如果足够
        if ((currentPos + data.length) <= this.fileSize) {
            try {
                //往映射内存写书写
                ByteBuffer buf = this.mappedByteBuffer.slice();
                buf.position(currentPos);
                buf.put(data);
            } catch (Throwable e) {
                log.error("Error occurred when append message to mappedFile.", e);
            }
            this.wrotePosition.addAndGet(data.length);
            return true;
        }

        return false;
    }

    /**
     * 从偏移量到偏移量+长度的数据内容将被写入文件
     *
     * @param offset 要使用的子数组的偏移量
     * @param length 要使用的子数组的长度
     */
    public boolean append(final byte[] data, final int offset, final int length) {
        int currentPos = this.wrotePosition.get();
        //如果足够
        if ((currentPos + length) <= this.fileSize) {
            try {
                ByteBuffer buf = this.mappedByteBuffer.slice();
                buf.position(currentPos);
                buf.put(data, offset, length);
            } catch (Throwable e) {
                log.error("Error occurred when append message to mappedFile.", e);
            }
            this.wrotePosition.addAndGet(length);
            return true;
        }

        return false;
    }


    /**
     * 刷盘
     * @return 当前刷新的位置
     */
    public int flush(final int flushLeastPages) {
        if (this.isAbleToFlush(flushLeastPages)) {
            //占用 对应 释放
            if (this.hold()) {
                int value = getReadPosition();
                try {
                    //我们只将数据附加到 fileChannel 或 mappedByteBuffer，而不是两者。
                    if (writeBuffer != null || this.fileChannel.position() != 0) {
                        //强制将此通道文件的任何更新写入包含它的存储设备。
                        this.fileChannel.force(false);
                    } else {
                        //强制将此通道文件的任何更新写入包含它的存储设备。
                        this.mappedByteBuffer.force();
                    }
                } catch (Throwable e) {
                    log.error("Error occurred when force data to disk.", e);
                }
                //刷新计数
                this.flushedPosition.set(value);
                //释放 对应 占用
                this.release();
            } else {
                log.warn("in flush, hold failed, flush offset = " + this.flushedPosition.get());
                this.flushedPosition.set(getReadPosition());
            }
        }
        //当前刷新的位置
        return this.getFlushedPosition();
    }

    /**
     * 将缓冲区的数据
     * @param commitLeastPages
     * @return
     */
    public int commit(final int commitLeastPages) {
        if (writeBuffer == null) {
            //无需将数据提交到文件通道，因此只需将 writePosition 视为committedPosition。
            return this.wrotePosition.get();
        }
        if (this.isAbleToCommit(commitLeastPages)) {
            //占用
            if (this.hold()) {
                commit0();
                //释放
                this.release();
            } else {
                log.warn("in commit, hold failed, commit offset = " + this.committedPosition.get());
            }
        }

        // 所有脏数据都已提交到 FileChannel。
        if (writeBuffer != null && this.transientStorePool != null && this.fileSize == this.committedPosition.get()) {
            //归还 writeBuffer
            this.transientStorePool.returnBuffer(writeBuffer);
            this.writeBuffer = null;
        }

        return this.committedPosition.get();
    }

    protected void commit0() {
        int writePos = this.wrotePosition.get();
        int lastCommittedPosition = this.committedPosition.get();
        //写 > 提交
        if (writePos - lastCommittedPosition > 0) {
            try {
                ByteBuffer byteBuffer = writeBuffer.slice();
                byteBuffer.position(lastCommittedPosition);
                byteBuffer.limit(writePos);
                this.fileChannel.position(lastCommittedPosition);
                this.fileChannel.write(byteBuffer);
                this.committedPosition.set(writePos);
            } catch (Throwable e) {
                log.error("Error occurred when commit data to FileChannel.", e);
            }
        }
    }

    private boolean isAbleToFlush(final int flushLeastPages) {
        //当前刷新的位置
        int flush = this.flushedPosition.get();
        //有效数据的最大位置
        int write = getReadPosition();
        //内存已写满
        if (this.isFull()) {
            return true;
        }
        //未刷新到磁盘的数据已到达设置的阈值
        if (flushLeastPages > 0) {
            return ((write / OS_PAGE_SIZE) - (flush / OS_PAGE_SIZE)) >= flushLeastPages;
        }
        //存在为刷新的数据
        return write > flush;
    }

    protected boolean isAbleToCommit(final int commitLeastPages) {
        int flush = this.committedPosition.get();
        int write = this.wrotePosition.get();

        if (this.isFull()) {
            return true;
        }

        if (commitLeastPages > 0) {
            return ((write / OS_PAGE_SIZE) - (flush / OS_PAGE_SIZE)) >= commitLeastPages;
        }

        return write > flush;
    }

    /**
     * 获取指定区域访问内数据
     * @param pos
     * @param size
     * @return
     */
    public SelectMappedBufferResult selectMappedBuffer(int pos, int size) {
        int readPosition = getReadPosition();
        //该区域可读
        if ((pos + size) <= readPosition) {
            //占用
            if (this.hold()) {
                ByteBuffer byteBuffer = this.mappedByteBuffer.slice();
                byteBuffer.position(pos);
                ByteBuffer byteBufferNew = byteBuffer.slice();
                byteBufferNew.limit(size);
                return new SelectMappedBufferResult(this.fileFromOffset + pos, byteBufferNew, size, this);
            } else {
                log.warn("matched, but hold failed, request pos: " + pos + ", fileFromOffset: "
                        + this.fileFromOffset);
            }
        } else {
            log.warn("selectMappedBuffer request pos invalid, request pos: " + pos + ", size: " + size
                    + ", fileFromOffset: " + this.fileFromOffset);
        }

        return null;
    }

    public SelectMappedBufferResult selectMappedBuffer(int pos) {
        int readPosition = getReadPosition();
        if (pos < readPosition && pos >= 0) {
            if (this.hold()) {
                ByteBuffer byteBuffer = this.mappedByteBuffer.slice();
                byteBuffer.position(pos);
                int size = readPosition - pos;
                ByteBuffer byteBufferNew = byteBuffer.slice();
                byteBufferNew.limit(size);
                return new SelectMappedBufferResult(this.fileFromOffset + pos, byteBufferNew, size, this);
            }
        }
        return null;
    }
    

    /**
     * 是否已经用完
     * @return
     */
    public boolean isFull() {
        return this.fileSize == this.wrotePosition.get();
    }
    
    public long getLastModifiedTimestamp() {
        return this.file.lastModified();
    }

    public int getFileSize() {
        return fileSize;
    }

    public FileChannel getFileChannel() {
        return fileChannel;
    }
    
    public long getFileFromOffset() {
        return this.fileFromOffset;
    }
    
    public int getFlushedPosition() {
        return flushedPosition.get();
    }

    public void setFlushedPosition(int pos) {
        this.flushedPosition.set(pos);
    }


    public int getWrotePosition() {
        return wrotePosition.get();
    }

    public void setWrotePosition(int pos) {
        this.wrotePosition.set(pos);
    }

    /**
     * @return 有效数据的最大位置
     */
    public int getReadPosition() {
        return this.writeBuffer == null ? this.wrotePosition.get() : this.committedPosition.get();
    }

    public void setCommittedPosition(int pos) {
        this.committedPosition.set(pos);
    }

    public String getFileName() {
        return fileName;
    }

    public MappedByteBuffer getMappedByteBuffer() {
        return mappedByteBuffer;
    }

    public ByteBuffer sliceByteBuffer() {
        return this.mappedByteBuffer.slice();
    }

    public long getStoreTimestamp() {
        return storeTimestamp;
    }

    public boolean isFirstCreateInQueue() {
        return firstCreateInQueue;
    }

    public void setFirstCreateInQueue(boolean firstCreateInQueue) {
        this.firstCreateInQueue = firstCreateInQueue;
    }

    //testable
    File getFile() {
        return this.file;
    }

    @Override
    public String toString() {
        return this.fileName;
    }
    
}
