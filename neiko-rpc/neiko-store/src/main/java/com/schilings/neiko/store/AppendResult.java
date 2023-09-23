package com.schilings.neiko.store;


import java.util.function.Supplier;

public class AppendResult {
    // Return code
    private AppendStatus status;

    // Where to start writing
    private long wroteOffset;
    // Write Bytes
    private int wroteBytes;
    // Message storage timestamp
    private long storeTimestamp;
    // Consume queue's offset(step by one)
    private long pagecacheRT = 0;

    public AppendResult(AppendStatus status) {
        this(status, 0, 0, 0, 0);
    }
    public AppendResult(AppendStatus status, long wroteOffset, int wroteBytes,
                               long storeTimestamp, long pagecacheRT) {
        this.status = status;
        this.wroteOffset = wroteOffset;
        this.wroteBytes = wroteBytes;
        this.storeTimestamp = storeTimestamp;
        this.pagecacheRT = pagecacheRT;
    }

    public long getPagecacheRT() {
        return pagecacheRT;
    }

    public void setPagecacheRT(final long pagecacheRT) {
        this.pagecacheRT = pagecacheRT;
    }
    
    public boolean isOk() {
        return this.status == AppendStatus.PUT_OK;
    }

    public AppendStatus getStatus() {
        
        return status;
    }
    public void setStatus(AppendStatus status) {
        
        this.status = status;
    }


    public long getWroteOffset() {
        return wroteOffset;
    }

    public void setWroteOffset(long wroteOffset) {
        this.wroteOffset = wroteOffset;
    }

    public int getWroteBytes() {
        return wroteBytes;
    }

    public void setWroteBytes(int wroteBytes) {
        this.wroteBytes = wroteBytes;
    }


    public long getStoreTimestamp() {
        return storeTimestamp;
    }

    public void setStoreTimestamp(long storeTimestamp) {
        this.storeTimestamp = storeTimestamp;
    }
    

    @Override
    public String toString() {
        return "AppendResult{" +
                "status=" + status +
                ", wroteOffset=" + wroteOffset +
                ", wroteBytes=" + wroteBytes +
                ", storeTimestamp=" + storeTimestamp +
                '}';
    }
}
